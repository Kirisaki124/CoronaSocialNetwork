package hava.coronasocialnetwork.database.management

import android.net.Uri
import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.operator.DaoPost
import hava.coronasocialnetwork.database.operator.RegisterStatus
import hava.coronasocialnetwork.model.Post


object DaoPostManagement {
    fun addPost(post: Post): RegisterStatus {
        DaoPost.addPost(post.ownerUid, post)
        return RegisterStatus.OK
    }

    fun getPostQuery(uid: String): Query {
        return DaoPost.getPostQuery(uid)
    }

    suspend fun getUserPostsById(uid: String): List<Post> {
        return DaoPost.getUserPostsById(uid)
    }

    suspend fun getNewFeed(): List<Post> {
        return DaoPost.getNewFeed()
    }

    suspend fun getPostImage(postId: String): Uri {
        return DaoPost.getPostImage(postId)
    }

}