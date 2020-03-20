package hava.coronasocialnetwork.database.operator

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User

object DaoAuthen {
    fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Boolean {
        var isSuccess = false
        DaoContext.authen.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = DaoContext.authen.currentUser
                    isSuccess = true
                    if (user != null) {
                        DaoContext.ref.child("Users").push()
                            .setValue(User(username, email, phone, address))
                    }
                } else {
                    Log.i("Error", task.exception.toString())
                }
            }
        return isSuccess
    }

    fun login(email: String, password: String): String? {
        DaoContext.authen.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = DaoContext.authen.currentUser
//                    Log.i("Testing", user?.uid)
                } else {
                    Log.i("Error", task.exception.toString())
                }
            }
        return DaoContext.authen.currentUser?.uid
    }

    fun signout() {
        DaoContext.authen.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return DaoContext.authen.currentUser
    }

}