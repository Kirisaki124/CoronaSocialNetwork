package hava.coronasocialnetwork.database.operator

import com.google.firebase.auth.*
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User
import kotlinx.coroutines.tasks.await

object DaoAuthen {
    suspend fun register(
        email: String,
        password: String,
        phone: String,
        username: String
    ): RegisterStatus {
        try {
            DaoContext.authen.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                        .setValue(User(username, email, phone))
                }
                .await()
        } catch (e: FirebaseAuthUserCollisionException) {
            return RegisterStatus.EMAIL_ALREADY_EXISTED
        } catch (e: FirebaseAuthWeakPasswordException) {
            return RegisterStatus.WEAK_PASSWORD
        } catch (e: FirebaseAuthEmailException) {
            return RegisterStatus.WRONG_EMAIL_FORMAT
        }
        return RegisterStatus.OK
    }

    suspend fun login(email: String, password: String): LoginStatus {
        try {
            DaoContext.authen.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthInvalidUserException) {
            return LoginStatus.NO_ACCOUNT_FOUND
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return LoginStatus.INVALID_PASSWORD
        }
        return LoginStatus.OK
    }

    fun signout() {
        DaoContext.authen.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return DaoContext.authen.currentUser
    }

}