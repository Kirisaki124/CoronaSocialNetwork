package hava.coronasocialnetwork.database.operator

import android.util.Log
import hava.coronasocialnetwork.database.context.DaoContext

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
}