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
            child("type").setValue(type)
            child("createdDate").setValue(Date().time)
            child("postId").setValue(postId)
        }
    }

    fun sendAddFriendNoti(uid: String) {
        ref.child(uid).child("Notification").push().apply {
            DaoNoti.ref.child(uid).child("Notification").child("NotiScreen").push().apply {
                child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
                child("type").setValue(Noti.ADD_FRIEND_NOTIFICATION)
                child("createdDate").setValue(Date().time)
            }
        }

    }

    fun sendChatNoti(partnerUid: String, chatRoomId: String) {
        ref.child(partnerUid).child("Notification").child("ChatNoti").push().apply {
            child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
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
}