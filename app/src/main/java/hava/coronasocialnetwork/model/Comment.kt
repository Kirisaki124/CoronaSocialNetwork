package hava.coronasocialnetwork.model

class Comment constructor(
    var comment: String,
    var uid: String, // ownerId
    var createdDate: String
) {
    var id: String = ""

    constructor() : this("", "", "")
    constructor(comment: String, ownerUid: String, createdDate: String, id: String) : this(
        comment,
        ownerUid,
        createdDate
    ) {
        this.id = id
    }
}