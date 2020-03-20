package hava.coronasocialnetwork.database

object DaoManagement {
    fun register(
        email: String,
        password: String,
        phone: String,
        username: String,
        address: String
    ): Boolean {
        return Dao.register(email, password, phone, username, address)
    }

    fun login(email: String, password: String): String? {
        return Dao.login(email, password)
    }
}