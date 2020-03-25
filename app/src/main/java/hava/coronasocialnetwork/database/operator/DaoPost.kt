package hava.coronasocialnetwork.database.operator

import android.net.Uri
import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Post
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DaoPost {
    suspend fun addPost(currentUid: String, post: Post, imageUri: Uri): RegisterStatus {
        val key = DaoContext.ref.child("Users").child(currentUid).child("posts").push().key!!
        var addedImage = false
        val friendList = arrayListOf<String>()
        friendList.addAll(DaoUser.getFriendList(currentUid))
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
}