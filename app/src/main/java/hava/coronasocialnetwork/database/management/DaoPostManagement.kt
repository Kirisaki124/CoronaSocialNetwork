package hava.coronasocialnetwork.database.management

import hava.coronasocialnetwork.database.operator.DaoPost
import hava.coronasocialnetwork.database.operator.Status
import hava.coronasocialnetwork.model.Post

object DaoPostManagement {
    fun addPost(uid: String, post: Post): Status {
        DaoPost.addPost(uid, post)
        return Status.OK
    }
}