package hava.coronasocialnetwork.database.operator

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Noti
import java.util.*

object DaoNoti {
    private val ref = DaoContext.ref.child("Users")
    fun sendPostNoti(type: String, postId: String, ownerId: String) {
        ref.child(ownerId).child("Notification").child("NotiScreen").push().apply {
            child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
            child("id").setValue(key)
            child("type").setValue(type)
            child("seen").setValue(false)
            child("createdDate").setValue(Date().time)
            child("postId").setValue(postId)
        }
    }

    fun sendAddFriendNoti(uid: String) {
        ref.child(uid).child("Notification").push().apply {
            DaoNoti.ref.child(uid).child("Notification").child("NotiScreen").push().apply {
                child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
                child("id").setValue(key)
                child("type").setValue(Noti.ADD_FRIEND_NOTIFICATION)
                child("seen").setValue(false)
                child("createdDate").setValue(Date().time)
            }
        }
    }

    fun sendChatNoti(partnerUid: String, chatRoomId: String) {
        ref.child(partnerUid).child("Notification").child("ChatNoti").child(chatRoomId).apply {
            child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
            child("seen").setValue(false)
            child("type").setValue(Noti.CHAT_NOTIFICATION)
            child("chatRoomId").setValue(chatRoomId)
        }
    }

    fun getAllNotiFromUid(uid: String): Query {
        return ref.child(uid).child("Notification").child("NotiScreen")
    }

    fun getChatNotiFromUid(uid: String): Query {
        return ref.child(uid).child("Notification").child("ChatNoti")
    }

    fun markChatAsRead(uid: String, roomId: String) {
        ref.child(uid).child("ChatRoom").child(roomId).child("seen").setValue(true)
        ref.child(uid).child("Notification").child("ChatNoti").child(roomId).child("seen")
            .setValue(true)
    }

    fun markAsSeen(uid: String, notiId: String) {
        ref.child(uid).child("Notification").child("NotiScreen").child(notiId).child("seen")
            .setValue(true)
    }
}