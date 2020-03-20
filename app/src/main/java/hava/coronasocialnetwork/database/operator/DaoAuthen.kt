package hava.coronasocialnetwork.database.operator

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User

interface OnAuthen {
    fun onSuccess(status: Status)
    fun onFailed(message: String?)
}

object DaoAuthen {
    fun register(
        callee: OnAuthen,
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ) {
        DaoContext.authen.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = DaoContext.authen.currentUser
                    Log.i("Register", "Done")
                    if (user != null) {
                        DaoContext.ref.child("Users").push()
                            .setValue(User(username, email, phone, address))
                    }
                    callee.onSuccess(Status.REGISTER)
                } else {
                    callee.onFailed(task.exception?.message)
                }
            }
    }

    fun login(callee: OnAuthen, email: String, password: String): String? {
        DaoContext.authen.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = DaoContext.authen.currentUser
//                    Log.i("Testing", user?.uid)
                    callee.onSuccess(Status.LOGIN)
                } else {
//                    Log.i("Error", task.exception.toString())
                    callee.onFailed(task.exception.toString())
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