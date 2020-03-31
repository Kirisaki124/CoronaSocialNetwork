package hava.coronasocialnetwork.database.management

import android.net.Uri
import hava.coronasocialnetwork.database.context.DaoContext
import hava.coronasocialnetwork.database.operator.DaoUser
import hava.coronasocialnetwork.database.operator.UpdateStatus
import hava.coronasocialnetwork.model.User

object DaoUserManagement {
    // TODO: Fix status
//    fun changeName(uid: String, name: String): Boolean {
//        return DaoUser.changeUsername(uid, name)
//    }
//
//    fun changeAddress(uid: String, address: String): Boolean {
//        return DaoUser.changeAddress(uid, address)
//    }
//
//    fun changePhone(uid: String, phone: String): Boolean {
//        return DaoUser.changePhone(uid, phone)
//    }

    suspend fun getUserInfo(uid: String?): User? {
        return DaoUser.getUserInfo(uid)
    }

    suspend fun searchUserByName(username: String): List<String> {
        val uidList = DaoUser.searchUserByName()
        return uidList.filter { uid ->
            getUserInfo(uid)?.run { this.username.contains(username) } ?: false
        }
    }

    suspend fun addFriend(uid1: String, uid2: String) {
        DaoUser.addFriend(uid1, uid2)
        DaoUser.addFriend(uid2, uid1)
        DaoChatManagement.createRoomChat(uid1, uid2)
    }

    suspend fun getFriendList(uid: String): List<String> {
        return DaoUser.getFriendList(uid)
    }

    fun setAvatar(imageUri: Uri): UpdateStatus {
        return DaoUser.setAvatar(DaoContext.authen.currentUser!!.uid, imageUri)
    }

    suspend fun getAvatarById(uid: String): Uri {
        return try {
            DaoUser.getAvatarById(uid)
        } catch (e: Exception) {
            DaoUser.getAvatarById("defaultAvatar.jpg")
        }
    }

    suspend fun isFriend(uid: String, friendId: String): Boolean {
        return DaoUser.isFriend(uid, friendId)
    }

}