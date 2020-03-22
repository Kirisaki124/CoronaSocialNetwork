package hava.coronasocialnetwork.database.operator

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User
import kotlinx.coroutines.tasks.await

object DaoAuthen {
    suspend fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Status {
        try {
            DaoContext.authen.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                        .setValue(User(username, email, phone, address))
                }
                .await()

        } catch (e: FirebaseAuthUserCollisionException) {
            return Status.EMAIL_ALREADY_EXISTED
        }
        return Status.OK
    }

    suspend fun login(email: String, password: String): Status {
        try {
            DaoContext.authen.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthInvalidUserException) {
            return Status.NO_ACCOUNT_FOUND
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return Status.INVALID_PASSWORD
        }
        return Status.OK
    }

    fun signout() {
        DaoContext.authen.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return DaoContext.authen.currentUser
    }

}