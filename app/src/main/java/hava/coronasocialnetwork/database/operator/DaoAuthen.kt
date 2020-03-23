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
        username: String,
        address: String
    ): StatusRegister {
        try {
            DaoContext.authen.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                        .setValue(User(username, email, phone, address, ArrayList()))
                }
                .await()
        } catch (e: FirebaseAuthUserCollisionException) {
            return StatusRegister.EMAIL_ALREADY_EXISTED
        } catch (e: FirebaseAuthWeakPasswordException) {
            return StatusRegister.WEAK_PASSWORD
        } catch (e: FirebaseAuthEmailException) {
            return StatusRegister.WRONG_EMAIL_FORMAT
        }
        return StatusRegister.OK
    }

    suspend fun login(email: String, password: String): StatusLogin {
        try {
            DaoContext.authen.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthInvalidUserException) {
            return StatusLogin.NO_ACCOUNT_FOUND
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            return StatusLogin.INVALID_PASSWORD
        }
        return StatusLogin.OK
    }

    fun signout() {
        DaoContext.authen.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return DaoContext.authen.currentUser
    }

}