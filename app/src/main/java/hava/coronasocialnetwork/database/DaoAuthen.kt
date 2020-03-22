package hava.coronasocialnetwork.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DaoManagement {
    private var database: FirebaseDatabase = Dao.getDatabase()
    private var mAuth: FirebaseAuth = Dao.getmAuth()

    fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Boolean {
        return Dao.register(email, password, phone, username, address)
    }

    fun login(email: String, password: String): String? {
        return Dao.login(email, password)
    }
}