package hava.coronasocialnetwork.database.operator

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import hava.coronasocialnetwork.database.context.DaoContext
import java.util.*

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
                }

            })
    }

    fun getMessageFromChatRoom(uid: String, chatRoomId: String): Query {
        return ref.child(uid).child("ChatRoom").child(chatRoomId).child("chatHistory")
    }

    fun getChatRoomByUserId(uid: String): Query {
        return ref.child(uid).child("ChatRoom")
    }
}