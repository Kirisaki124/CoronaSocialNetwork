package hava.coronasocialnetwork.database.operator

import com.google.firebase.auth.*
import hava.coronasocialnetwork.database.context.DaoContext
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
                    try {
                        DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                            .child("username").setValue(username)
                        DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                            .child("email").setValue(email)
                        DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                            .child("phone").setValue(phone)
                        DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
                            .child("avatar").setValue("defaultAvatar.jpg")

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    DaoContext.ref.child("Users").child(task.result?.user?.uid.toString())
//                        .setValue(Uri.EMPTY)
                }
                .await()
        } catch (e: FirebaseAuthUserCollisionException) {
            return RegisterStatus.EMAIL_ALREADY_EXISTED
        } catch (e: FirebaseAuthWeakPasswordException) {
            return RegisterStatus.WEAK_PASSWORD
        } catch (e: FirebaseAuthEmailException) {
            return RegisterStatus.WRONG_EMAIL_FORMAT
        } catch (e: FirebaseAuthInvalidCredentialsException) {
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