package hava.coronasocialnetwork.database.operator

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Post
import java.io.File
import java.io.FileInputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DaoPost {
    fun addPost(currentUid: String, post: Post): RegisterStatus {
        val key = DaoContext.ref.child("Users").child(currentUid).child("posts").push().key
        if (post.image.trim() != "") {
            val image = File(post.image)
            val stream = FileInputStream(image)
            val imageExtension = image.name.split(".")[1]

            DaoContext.ref.child("Users").child(currentUid).child("posts").child(key!!)
                .child("image")
                .setValue("$key.$imageExtension")
            DaoContext.storageRef.child("images/$key.$imageExtension").putStream(stream)
        }

        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key!!).child("caption")
            .setValue(post.caption)

        DaoContext.ref.child("Users").child(currentUid).child("posts").child(key)
            .child("createdDate")
            .setValue(post.createdDate)

        return RegisterStatus.OK
    }

    suspend fun getUserPostsById(uid: String): ArrayList<Post> {
        return suspendCoroutine { cont ->
            DaoContext.ref.child("Users").child(uid).child("posts")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.i("Error", p0.toString())
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val list: ArrayList<Post> = ArrayList()
                        for (post in p0.children) {
                            Log.i("Post", post.getValue(Post::class.java).toString())
                            list.add(post.getValue(Post::class.java)!!)
                        }
                        cont.resume(list)
                    }

                })
        }
    }

    suspend fun getNewFeed(): ArrayList<Post> {
        return suspendCoroutine { cont ->
            DaoContext.ref.child("Users").child(DaoAuthen.getCurrentUser()?.uid!!).child("posts")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.i("Error", p0.toString())
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val list: ArrayList<Post> = ArrayList()
                        for (post in p0.children) {
                            Log.i("Post", post.getValue(Post::class.java).toString())
                            list.add(post.getValue(Post::class.java)!!)
                        }
                        cont.resume(list)
                    }

                })
        }
    }

}