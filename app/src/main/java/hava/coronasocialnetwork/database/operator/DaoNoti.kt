package hava.coronasocialnetwork.database.operator

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.model.Noti

object DaoNoti {
    val ref = DaoContext.ref.child("Users")
    fun sendPostNoti(message: String, type: String, postId: String, ownerId: String) {
        val key = ref.child(ownerId).child("Notification").push().key!!
        ref.child(ownerId).child("Notification").child(key).apply {
            child("message").setValue(message)
            child("type").setValue(type)
            child("postId").setValue(postId)
        }
    }

    fun sendAddFriendNoti(message: String, uid: String) {
        val key = ref.child(uid).child("Notification").push().key!!
        ref.child(uid).child("Notification").child(key).apply {
            child("message").setValue(message)
            child("type").setValue(Noti.ADD_FRIEND_NOTIFICATION)
            child("friendId").setValue(DaoContext.authen.currentUser!!.uid)
        }
    }

    fun sendChatNoti(message: String, partnerUid: String, chatRoomId: String) {
        val key = ref.child(partnerUid).child("Notification").push().key!!
        ref.child(partnerUid).child("Notification").child(key).apply {
            child("message").setValue(message)
            child("type").setValue(Noti.CHAT_NOTIFICATION)
            child("chatRoomId").setValue(chatRoomId)
        }
    }

    fun getAllNotiFromUid(uid: String): Query {
        return ref.child(uid).child("Notification")
    }
}