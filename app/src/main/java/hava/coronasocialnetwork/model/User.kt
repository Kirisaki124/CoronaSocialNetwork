package hava.coronasocialnetwork.model

import java.io.Serializable

class User(
    var username: String?,
    var email: String?,
    var phone: String?,
    var address: String?
) : Serializable