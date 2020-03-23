package hava.coronasocialnetwork.database.management

import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.operator.DaoAuthen
import hava.coronasocialnetwork.database.operator.LoginStatus
import hava.coronasocialnetwork.database.operator.RegisterStatus

object DaoAuthenManagement {
    suspend fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): RegisterStatus {
        return DaoAuthen.register(
            email,
            password,
            phone,
            username,
            address
        )
    }

    suspend fun login(email: String, password: String): LoginStatus {
        return DaoAuthen.login(email, password)
    }

    fun signOut() {
        DaoAuthen.signout()
    }

    fun getCurrentUser(): FirebaseUser? {
        return DaoAuthen.getCurrentUser()
    }
}