package hava.coronasocialnetwork.model

class ChatRoom constructor(
    var uid1: String,
    var uid2: String
) {
    var chatRoomId: String = ""

    constructor() : this("", "")
    constructor(uid1: String, uid2: String, chatRoomId: String) : this(
        uid1,
        uid2
    ) {
        this.chatRoomId = chatRoomId
    }
}