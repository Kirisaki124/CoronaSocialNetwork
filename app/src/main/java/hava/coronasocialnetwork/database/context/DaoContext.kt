package hava.coronasocialnetwork.database.context

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object DaoContext {
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref = database.reference
}