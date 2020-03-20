package hava.coronasocialnetwork.database.management

import com.google.firebase.auth.FirebaseUser
import hava.coronasocialnetwork.database.operator.DaoAuthen
import hava.coronasocialnetwork.database.operator.OnAuthen

object DaoAuthenManagement {
    // Creating a new user using email, password, phone number, username and address
    // return true if success
    fun register(
        callee: OnAuthen,
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ) {
        DaoAuthen.register(
            callee,
            email,
            password,
            phone,
            username,
            address
        )
    }

    // Login using email and password
    // return true if success
    fun login(callee: OnAuthen, email: String, password: String) {
        DaoAuthen.login(callee, email, password)
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