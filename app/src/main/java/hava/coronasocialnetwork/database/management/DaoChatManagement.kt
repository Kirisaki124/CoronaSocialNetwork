package hava.coronasocialnetwork.database.management

import com.google.firebase.database.Query
import hava.coronasocialnetwork.database.operator.DaoChat

object DaoChatManagement {
    fun createRoomChat(uid1: String, uid2: String) {
        DaoChat.createRoomChat(uid1, uid2)
    }

    fun addChatMessage(uid: String, chatRoomId: String, message: String) {
        DaoChat.addChatMessage(uid, chatRoomId, message)
    }

    fun getMessageFromChatRoom(uid: String, chatRoomId: String): Query {
        return DaoChat.getMessageFromChatRoom(uid, chatRoomId)
    }

    fun getChatRoomByUserId(uid: String): Query {
        return DaoChat.getChatRoomByUserId(uid)
    }
}