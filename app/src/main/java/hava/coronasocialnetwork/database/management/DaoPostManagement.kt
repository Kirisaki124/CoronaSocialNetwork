package hava.coronasocialnetwork.database.management

import hava.coronasocialnetwork.database.operator.DaoPost
import hava.coronasocialnetwork.database.operator.RegisterStatus
import hava.coronasocialnetwork.model.Post

object DaoPostManagement {
    fun addPost(uid: String, post: Post): RegisterStatus {
        DaoPost.addPost(uid, post)
        return RegisterStatus.OK
    }

    suspend fun getUserPostsById(uid: String): ArrayList<Post> {
        return DaoPost.getUserPostsById(uid)
    }

    suspend fun getNewFeed(): ArrayList<Post> {
        return DaoPost.getNewFeed()
    }
}