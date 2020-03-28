package hava.coronasocialnetwork.database.management

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.operator.DaoNoti

object DaoNotiManagement {
    fun sendPostNoti(message: String, type: String, postId: String, ownerId: String) {
        return DaoNoti.sendPostNoti(message, type, postId, ownerId)
    }

    fun sendAddFriendNoti(message: String, uid: String) {
        return DaoNoti.sendAddFriendNoti(message, uid)
    }

    fun sendChatNoti(message: String, partnerUid: String, chatRoomId: String) {
        return DaoNoti.sendChatNoti(message, partnerUid, chatRoomId)
    }

    fun getAllNotiFromUid(uid: String): Query {
        return DaoNoti.getAllNotiFromUid(uid)
    }

    fun getChatNotiFromUid(uid: String): Query {
        return DaoNoti.getChatNotiFromUid(uid)
    }
}