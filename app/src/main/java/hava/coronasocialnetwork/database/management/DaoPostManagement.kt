package hava.coronasocialnetwork.database.management

import hava.coronasocialnetwork.database.operator.DaoPost
import hava.coronasocialnetwork.database.operator.RegisterStatus
import hava.coronasocialnetwork.model.Post

object DaoPostManagement {
    fun addPost(uid: String, post: Post): RegisterStatus {
        DaoPost.addPost(uid, post)
        return RegisterStatus.OK
    }

    suspend fun getUserPostsById(uid: String): List<Post> {
        return DaoPost.getUserPostsById(uid)
    }

    suspend fun getNewFeed(): List<Post> {
        return DaoPost.getNewFeed()
    }
}