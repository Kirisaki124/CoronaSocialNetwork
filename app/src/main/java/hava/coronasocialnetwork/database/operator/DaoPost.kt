package hava.coronasocialnetwork.database.operator

import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Post
import java.io.FileInputStream

object DaoPost {
    fun addPost(currentUid: String, post: Post): Status {
        var stream = FileInputStream(post.image)
        var key = DaoContext.ref.child("Users").child(currentUid).child("posts").push().key
        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key!!).child("caption")
            .setValue(post.caption)
        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key).child("image")
            .setValue(post.image.name)
        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key)
            .child("createdDate")
            .setValue(post.createdDate)

        var imageExtension = post.image.name.split(".")[1]

        DaoContext.storageRef.child("images/$key.$imageExtension").putStream(stream)
        return Status.OK
    }
}