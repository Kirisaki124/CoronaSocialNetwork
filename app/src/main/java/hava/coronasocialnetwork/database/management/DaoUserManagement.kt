package hava.coronasocialnetwork.database.management

import hava.coronasocialnetwork.database.operator.DaoUser
import hava.coronasocialnetwork.model.User

object DaoUserManagement {
    fun changeName(uid: String, name: String): Boolean {
        return DaoUser.changeUsername(uid, name)
    }

    fun changeAddress(uid: String, address: String): Boolean {
        return DaoUser.changeAddress(uid, address)
    }

    fun changePhone(uid: String, phone: String): Boolean {
        return DaoUser.changePhone(uid, phone)
    }

    suspend fun getUserInfo(uid: String?): User? {
        return DaoUser.getUserInfo(uid)
    }
}