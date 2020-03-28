package hava.coronasocialnetwork.database.operator

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Noti

object DaoNoti {
    private val ref = DaoContext.ref.child("Users")
    fun sendPostNoti(message: String, type: String, postId: String, ownerId: String) {
        ref.child(ownerId).child("Notification").child("NotiScreen").push().apply {
            child("message").setValue(message)
            child("type").setValue(type)
            child("postId").setValue(postId)
            child("senderId").setValue(DaoContext.authen.currentUser!!.uid)
        }
    }

    fun sendAddFriendNoti(message: String, uid: String) {
        ref.child(uid).child("Notification").push().apply {
            DaoNoti.ref.child(uid).child("Notification").child("NotiScreen").push().apply {
                child("message").setValue(message)
                child("type").setValue(Noti.ADD_FRIEND_NOTIFICATION)
                child("friendId").setValue(DaoContext.authen.currentUser!!.uid)
            }
        }

    }

    fun sendChatNoti(message: String, partnerUid: String, chatRoomId: String) {
        ref.child(partnerUid).child("Notification").child("ChatNoti").push().apply {
            child("message").setValue(message)
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