package hava.coronasocialnetwork.database.management

import hava.coronasocialnetwork.database.operator.DaoUser

object DaoUserManagement {
    // Change user's username using user id
    // return true if success
    fun changeName(uid: String, name: String): Boolean {
        return DaoUser.changeUsername(uid, name)
    }

    // Change user's address using user id
    // return true if success
    fun changeAddress(uid: String, address: String): Boolean {
        return DaoUser.changeAddress(uid, address)
    }
}