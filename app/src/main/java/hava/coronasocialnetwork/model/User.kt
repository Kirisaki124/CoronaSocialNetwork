package hava.coronasocialnetwork.model

class User constructor(
    val username: String,
    val email: String,
    val phone: String
) {
    var id = ""

    constructor() : this("", "", "")
    constructor(username: String, email: String, phone: String, id: String) : this(
        username,
        email,
        phone
    ) {
        this.id = id
    }
}

