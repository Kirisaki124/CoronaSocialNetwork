package hava.coronasocialnetwork.database.operator

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Post
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DaoPost {
    fun addPost(currentUid: String, post: Post): RegisterStatus {
        val key = DaoContext.ref.child("Users").child(currentUid).child("posts").push().key
        if (post.imageURI != Uri.EMPTY) {
            val imageExtension = post.imageURI.path!!.substringAfterLast(".")
            DaoContext.ref.child("Users").child(currentUid).child("posts").child(key!!)
                .child("image")
                .setValue("$key.$imageExtension")
            DaoContext.storageRef.child("images/$key.$imageExtension").putFile(post.imageURI)
        }

        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key!!)
            .child("ownerUid")
            .setValue(post.ownerUid)

        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key).child("caption")
            .setValue(post.caption)

        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key)
            .child("createdDate")
            .setValue(post.createdDate)

        return RegisterStatus.OK
    }

    fun getPostQuery(uid: String): Query {
        return DaoContext.ref.child("Users").child(uid).child("posts")
    }

    suspend fun getUserPostsById(uid: String): List<Post> {
        return suspendCoroutine { cont ->
            DaoContext.ref.child("Users").child(uid).child("posts")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.i("Error", p0.toString())
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val list =
                            p0.children.map { dataSnapshot -> dataSnapshot.getValue(Post::class.java)!! }

                        cont.resume(list)
                    }

                })
        }
    }

    suspend fun getNewFeed(): List<Post> {
        val friendListUid =
            DaoContext.authen.currentUser?.uid?.let { DaoUserManagement.getFriendList(it) }
        return friendListUid?.map { friendUid -> getUserPostsById(friendUid) }?.flatten()
            ?: listOf()
    }

    suspend fun getPostImage(postId: String): Uri {
        return suspendCoroutine { cont ->
            DaoContext.storageRef.child("images/${postId}.jpg").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("URI", it.result.toString())
                    cont.resume(it.result!!)
                } else {
                    Log.i("Error", it.exception.toString())
                    cont.resumeWithException(it.exception!!)
                }
            }
        }
    }

}