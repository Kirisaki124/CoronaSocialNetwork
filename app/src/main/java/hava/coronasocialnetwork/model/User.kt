package hava.coronasocialnetwork.model

class User constructor(
    var username: String,
    var email: String,
    var phone: String
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

