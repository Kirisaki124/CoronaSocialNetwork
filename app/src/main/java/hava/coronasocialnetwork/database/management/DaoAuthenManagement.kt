package hava.coronasocialnetwork.database.management

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
}