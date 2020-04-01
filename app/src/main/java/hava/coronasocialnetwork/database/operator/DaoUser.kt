package hava.coronasocialnetwork.database.operator

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.management.DaoPostManagement
import hava.coronasocialnetwork.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object DaoUser {
    private val ref = DaoContext.ref.child("Users")
    suspend fun getUserInfo(uid: String?): User? {
        val ref = ref.child(uid!!)
        return suspendCoroutine { cont ->
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    cont.resumeWithException(databaseError.toException())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    GlobalScope.launch {
                        val user = User()
                        user.email = dataSnapshot.child("email").value.toString()
                        user.phone = dataSnapshot.child("phone").value.toString()
                        user.username = dataSnapshot.child("username").value.toString()
                        user.id = dataSnapshot.key.toString()
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
                    val listAllUser = p0.children.map { it.key!! }
                    cont.resume(listAllUser)
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
                    DaoPostManagement.getUserPostsById(uid2)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                p0.children.forEach {
                                    ref.child(uid1).child("posts").child(it.key!!)
                                        .setValue(it.value)
                                }
                            }
                        })
                    DaoPostManagement.getUserPostsById(uid1)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                p0.children.forEach {
                                    ref.child(uid2).child("posts").child(it.key!!)
                                        .setValue(it.value)
                                }
                            }

                        })
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

    suspend fun isFriend(uid: String, friendId: String): Boolean {
        return suspendCoroutine { cont ->
            ref.child(uid).child("friends").child(friendId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.value == friendId) cont.resume(true)
                        else cont.resume(false)
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