package hava.coronasocialnetwork.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DaoManagement {
    private var database: FirebaseDatabase = Dao.getDatabase()
    private var mAuth: FirebaseAuth = Dao.getmAuth()

    fun register(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                Log.i("Testing", user?.uid)
            } else {
                Log.i("Testing", task.exception.toString())
            }
        }
    }
}