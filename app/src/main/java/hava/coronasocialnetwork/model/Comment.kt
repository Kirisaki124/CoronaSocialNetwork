package hava.coronasocialnetwork.model

class Comment constructor(
    var comment: String,
    var uid: String, // ownerId
    var createdDate: Long
) {
    var id: String = ""

    constructor() : this("", "", 0)
    constructor(comment: String, ownerUid: String, createdDate: Long, id: String) : this(
        comment,
        ownerUid,
        createdDate
    ) {
        this.id = id
    }
}