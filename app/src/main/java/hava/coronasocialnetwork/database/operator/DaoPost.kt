package hava.coronasocialnetwork.database.operator

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoUserManagement
import hava.coronasocialnetwork.model.Post
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DaoPost {
    suspend fun addPost(currentUid: String, post: Post, imageUri: Uri): RegisterStatus {
        val key = DaoContext.ref.child("Users").child(currentUid).child("posts").push().key!!
        var addedImage = false
        val friendList = arrayListOf<String>()
        friendList.addAll(DaoUserManagement.getFriendList(currentUid))
        friendList.add(currentUid)

        friendList.forEach { friend ->
            if (imageUri != Uri.EMPTY && !addedImage) {
                DaoContext.storageRef.child("images/$key").putFile(imageUri)
                addedImage = true
            }
            DaoContext.ref.child("Users").child(friend).child("posts").child(key).apply {
                child("ownerUid")
                    .setValue(post.ownerUid)
                child("caption")
                    .setValue(post.caption)
                child("createdDate")
                    .setValue(post.createdDate)
                child("id")
                    .setValue(key)
            }
        }
        return RegisterStatus.OK
    }

    fun getUserPostsById(uid: String): Query {
        return DaoContext.ref.child("Users").child(uid).child("posts").orderByChild("ownerUid")
            .equalTo(uid)
    }

    fun getNewFeed(): Query {
        return DaoContext.ref.child("Users").child(DaoContext.authen.currentUser!!.uid)
            .child("posts")
    }

    suspend fun getPostImage(postId: String): Uri {
        return suspendCoroutine { cont ->
            DaoContext.storageRef.child("images/${postId}").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    cont.resume(it.result!!)
                } else {
                    cont.resume(Uri.EMPTY)
                }
            }
        }
    }

    fun likePostById(uid: String, postId: String) {
        DaoContext.ref.child("Like").child(postId).child(uid).setValue(true)
    }

    fun unlikePostById(uid: String, postId: String) {
        DaoContext.ref.child("Like").child(postId).child(uid).removeValue()
    }

    suspend fun isPostLiked(uid: String, postId: String): Boolean {
        return suspendCoroutine { cont ->
            DaoContext.ref.child("Like").child(postId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.hasChild(uid)) cont.resume(true)
                        else cont.resume(false)
                    }
                })
        }
    }

    fun getLikeByPostId(postId: String, valueListener: ValueEventListener) {
        DaoContext.ref.child("Like").child(postId).addValueEventListener(valueListener)
    }

    fun commentPostById(uid: String, postId: String, comment: String) {
        val key = DaoContext.ref.child("Comment").child(postId).push().key
        DaoContext.ref.child("Comment").child(postId).child(key!!).apply {
            child("uid").setValue(uid)
            child("id").setValue(key)
            child("comment").setValue(comment)
            child("createdDate").setValue(Date().time)
        }
    }

    fun getAllCommentByPostId(postId: String): Query {
        return DaoContext.ref.child("Comment").child(postId).orderByChild("createdDate")
    }
}