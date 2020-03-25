package hava.coronasocialnetwork.database.management

import android.net.Uri
import com.google.firebase.database.Query
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

}