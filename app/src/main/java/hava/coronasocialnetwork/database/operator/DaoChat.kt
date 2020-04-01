package hava.coronasocialnetwork.database.operator

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object DaoChat {
    private val ref = DaoContext.ref.child(("Users"))

    fun createRoomChat(uid1: String, uid2: String) {
        val key = ref.push().key!!
        ref.child(uid1).child("ChatRoom").child(key).apply {
            child("uid1").setValue(uid1)
            child("uid2").setValue(uid2)
            child("id").setValue(key)
        }
        ref.child(uid2).child("ChatRoom").child(key).apply {
            child("uid1").setValue(uid1)
            child("uid2").setValue(uid2)
            child("id").setValue(key)
        }
    }

    fun addChatMessage(uid: String, chatRoomId: String, message: String) {
        val key = DaoContext.ref.child("Users").push().key!!
        val time = Date().time
        ref.child(uid).child("ChatRoom").child(chatRoomId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    ref.child(p0.child("uid1").value.toString()).child("ChatRoom").child(chatRoomId)
                        .child("chatHistory").child(key).apply {
                            child("uid").setValue(uid)
                            child("createDate").setValue(time)
                            child("message").setValue(message)
                        }
                    ref.child(p0.child("uid1").value.toString()).child("ChatRoom").child(chatRoomId)
                        .apply {
                            child("lastMessage").setValue(message)
                            child("lastUpdate").setValue(time)
                        }

                    ref.child(p0.child("uid2").value.toString()).child("ChatRoom").child(chatRoomId)
                        .child("chatHistory").child(key).apply {
                            child("uid").setValue(uid)
                            child("createDate").setValue(time)
                            child("message").setValue(message)
                        }
                    ref.child(p0.child("uid2").value.toString()).child("ChatRoom").child(chatRoomId)
                        .apply {
                            child("lastMessage").setValue(message)
                            child("lastUpdate").setValue(time)
                        }

                    if (DaoContext.authen.currentUser!!.uid == p0.child("uid1").value.toString()) {
                        ref.child(p0.child("uid2").value.toString()).child("ChatRoom")
                            .child(chatRoomId).child("seen").setValue(false)
                        ref.child(p0.child("uid2").value.toString()).child("ChatRoom")
                            .child(chatRoomId).child("seen").setValue(false)
                    } else if (DaoContext.authen.currentUser!!.uid == p0.child("uid2").value.toString()) {
                        ref.child(p0.child("uid1").value.toString()).child("ChatRoom")
                            .child(chatRoomId).child("seen").setValue(false)
                    }
                }

            })
    }

    fun getMessageFromChatRoom(uid: String, chatRoomId: String): Query {
        return ref.child(uid).child("ChatRoom").child(chatRoomId).child("chatHistory")
    }

    fun getChatRoomByUserId(uid: String): Query {
        return ref.child(uid).child("ChatRoom")
    }

    suspend fun getChatRoomWithId(currentUid: String, friendUid: String): String {
        return suspendCoroutine { cont ->
            ref.child(currentUid).child("ChatRoom")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        cont.resumeWithException(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var key = ""
                        for (item in p0.children) {
                            if (item.child("uid1").value.toString() == friendUid || item.child("uid2").value.toString() == friendUid) {
                                key = item.key!!
                            }
                        }
                        cont.resume(key)
                    }
                })
        }
    }
}