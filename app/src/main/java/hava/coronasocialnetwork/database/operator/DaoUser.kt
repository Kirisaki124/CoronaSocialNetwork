package hava.coronasocialnetwork.database.operator

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object DaoUser {
    private val ref = DaoContext.ref.child("Users")
    // TODO: Fix async
//    fun changeUsername(uid: String, username: String): UpdateStatus {
//        var isSuccess = false
//        ref.child(uid).child("username").setValue(username)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    isSuccess = true
//                } else {
//                    Log.i("Error", task.exception.toString())
//                }
//            }
//        return isSuccess
//    }
//
//    fun changeAddress(uid: String, address: String): Boolean {
//        var isSuccess = false
//        ref.child(uid).child("address").setValue(address)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    isSuccess = true
//                } else {
//                    Log.i("Error", task.exception.toString())
//                }
//            }
//        return isSuccess
//    }
//
//    fun changePhone(uid: String, phone: String): Boolean {
//        var isSuccess = false
//        ref.child(uid).child("phone").setValue(phone)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    isSuccess = true
//                } else {
//                    Log.i("Error", task.exception.toString())
//                }
//            }
//        return isSuccess
//    }

    suspend fun getUserInfo(uid: String?): User? {
        val ref = ref.child(uid!!)
        return suspendCoroutine { cont ->
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    cont.resumeWithException(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    GlobalScope.launch {
                        val user = dataSnapshot.getValue(User::class.java)
                        cont.resume(user)
                    }
                }
            })
        }
    }

    suspend fun searchUserByName(): List<String> {
        return suspendCoroutine { cont ->
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("Error", p0.toException().toString())
                    cont.resumeWithException(p0.toException())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val list = p0.children.map { it.key!! }
                    cont.resume(list)
                }
            })
        }
    }

    suspend fun addFriend(uid1: String, uid2: String) {
        val currentRef = ref.child(uid1).child("friends").child(uid2)
        if (getUserInfo(uid2) != null) {
            currentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("Error", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (!p0.children.any { it.value?.equals(uid2)!! }) currentRef.setValue(uid2)
                }
            })
        }
    }

    suspend fun getFriendList(uid: String): List<String> {
        return suspendCoroutine { cont ->
            ref.child(uid).child("friends")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val friendList =
                            p0.children.map { dataSnapshot -> dataSnapshot.getValue(String::class.java)!! }
                        cont.resume(friendList)
                    }
                })
        }
    }

    fun setAvatar(uid: String, imageUri: Uri): UpdateStatus {
        DaoContext.storageRef.child("avatars/$uid").putFile(imageUri)
        DaoContext.ref.child("Users").child(uid).child("id").setValue(uid)

        return UpdateStatus.OK
    }

    suspend fun getAvatarById(uid: String): Uri {
        return suspendCoroutine { cont ->
            DaoContext.storageRef.child("avatars/${uid}").downloadUrl.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("URI", it.result.toString())
                    cont.resume(it.result!!)
                } else {
                    Log.i("Error", it.exception.toString())
                    cont.resumeWithException(it.exception!!)
                }
            }
        }
    }
}