package hava.coronasocialnetwork.database.management

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.operator.DaoNoti

object DaoNotiManagement {
    fun sendPostNoti(type: String, postId: String, ownerId: String) {
        return DaoNoti.sendPostNoti(type, postId, ownerId)
    }

    fun sendAddFriendNoti(uid: String) {
        return DaoNoti.sendAddFriendNoti(uid)
    }

    fun sendChatNoti(partnerUid: String, chatRoomId: String) {
        return DaoNoti.sendChatNoti(partnerUid, chatRoomId)
    }

    fun getAllNotiFromUid(uid: String): Query {
        return DaoNoti.getAllNotiFromUid(uid)
    }

    fun getChatNotiFromUid(uid: String): Query {
        return DaoNoti.getChatNotiFromUid(uid)
    }

    fun deleteAllChatNoti(uid: String) = DaoNoti.deleteAllChatNoti(uid)

    fun markAsSeen(uid: String, notiId: String) {
        DaoNoti.markAsSeen(uid, notiId)
    }
}