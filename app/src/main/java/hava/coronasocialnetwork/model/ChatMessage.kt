package hava.coronasocialnetwork.model

class ChatMessage constructor(
    var message: String,
    var uid: String, // sender
    var createdDate: String
) {
    var id: String = ""

    constructor() : this("", "", "")
    constructor(message: String, uid: String, createdDate: String, id: String) : this(
        message,
        uid,
        createdDate
    ) {
        this.id = id
    }
}