package hava.coronasocialnetwork.database.management

import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.operator.DaoAuthen

object DaoAuthenManagement {
    // Creating a new user using email, password, phone number, username and address
    // return true if success
    fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Boolean {
        return DaoAuthen.register(
            email,
            password,
            phone,
            username,
            address
        )
    }

    // Login using email and password
    // return true if success
    fun login(email: String, password: String): String? {
        return DaoAuthen.login(email, password)
    }

    // Sign out
    fun signOut() {
        DaoAuthen.signout()
    }

    // Get current signed in user
    // Null if no one signed in
    fun getCurrentUser(): FirebaseUser? {
        return DaoAuthen.getCurrentUser()
    }
}