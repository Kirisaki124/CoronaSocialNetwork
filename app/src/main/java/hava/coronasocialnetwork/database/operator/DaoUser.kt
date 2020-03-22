package hava.coronasocialnetwork.database.operator

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User


object DaoUser {
    fun changeUsername(uid: String, username: String): Boolean {
        var isSuccess = false
        DaoContext.ref.child("Users").child(uid).child("username").setValue(username)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSuccess = true
                } else {
                    Log.i("Error", task.exception.toString())
                }
            }
        return isSuccess
    }

    fun changeAddress(uid: String, address: String): Boolean {
        var isSuccess = false
        DaoContext.ref.child("Users").child(uid).child("address").setValue(address)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSuccess = true
                } else {
                    Log.i("Error", task.exception.toString())
                }
            }
        return isSuccess
    }

    fun changePhone(uid: String, phone: String): Boolean {
        var isSuccess = false
        DaoContext.ref.child("Users").child(uid).child("phone").setValue(phone)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isSuccess = true
                } else {
                    Log.i("Error", task.exception.toString())
                }
            }
        return isSuccess
    }

    fun getUserInfo(uid: String?): User? {
        val ref = DaoContext.ref.child("Users").child(uid!!)
        var user = User()
        var ok = false

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("Error", p0.message)
                ok = true
            }

            override fun onDataChange(p0: DataSnapshot) {
                user = p0.getValue(User::class.java)!!
                ok = true
                Log.i("Username", user.username)
            }
        })
        while (!ok) {
        }
        return user
    }
}