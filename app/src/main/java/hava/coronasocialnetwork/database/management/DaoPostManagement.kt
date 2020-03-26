package hava.coronasocialnetwork.database.management

import android.net.Uri
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.operator.DaoPost
import hava.coronasocialnetwork.database.operator.RegisterStatus
import hava.coronasocialnetwork.model.Post


object DaoPostManagement {
    suspend fun addPost(post: Post, imageUri: Uri = Uri.EMPTY): RegisterStatus {
        DaoPost.addPost(post.ownerUid, post, imageUri)
        return RegisterStatus.OK
    }

    fun getUserPostsById(uid: String): Query {
        return DaoPost.getUserPostsById(uid)
    }

    fun getNewFeed(): Query {
        return DaoPost.getNewFeed()
    }

    suspend fun getPostImage(postId: String): Uri {
        return DaoPost.getPostImage(postId)
    }

    fun likePostById(uid: String, postId: String) {
        return DaoPost.likePostById(uid, postId)
    }

    fun unlikePostById(uid: String, postId: String) {
        return DaoPost.unlikePostById(uid, postId)
    }

    fun getLikeByPostId(postId: String, valueListener: ValueEventListener) {
        return DaoPost.getLikeByPostId(postId, valueListener)
    }

    fun addCommentPostById(uid: String, postId: String, comment: String) {
        return DaoPost.commentPostById(uid, postId, comment)
    }

    fun getAllCommentByPostId(postId: String): Query {
        return DaoPost.getAllCommentByPostId(postId)
    }

}