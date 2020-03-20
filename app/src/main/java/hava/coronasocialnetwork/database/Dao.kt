package hava.coronasocialnetwork.database

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import hava.coronasocialnetwork.model.User

object Dao {
    //    private var mFirebaseAnalytics: FirebaseAnalytics
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var context: Context
    private var ref = database.reference

    fun setContext(context: Context) {
        this.context = context
    }

    fun getDatabase(): FirebaseDatabase {
        return this.database
    }

    fun getmAuth(): FirebaseAuth {
        return this.mAuth
    }

    fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Boolean {
        var isSuccess: Boolean = false
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                isSuccess = true
                if (user != null) {
                    ref.child("Users").push().setValue(User(username, email, phone, address))
                }
            } else {
                Log.i("Error", task.exception.toString())
            }
        }
        return isSuccess
    }

    fun login(email: String, password: String): String? {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
//                    Log.i("Testing", user?.uid)
                } else {
                    Log.i("Testing", task.exception.toString())
                }
            }
        return mAuth.currentUser?.uid
    }

}