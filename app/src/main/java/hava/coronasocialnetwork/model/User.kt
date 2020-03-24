package hava.coronasocialnetwork.model

class User constructor(
    val username: String,
    val email: String,
    val phone: String
) {

    constructor() : this("", "", "")

}

