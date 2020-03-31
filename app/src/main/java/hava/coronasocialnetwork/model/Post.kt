package hava.coronasocialnetwork.model

class Post constructor(
    var caption: String,
    var ownerUid: String,
    var createdDate: Long
) {
    var id: String = ""

    constructor() : this("", "", 0)
    constructor(caption: String, ownerUid: String, createdDate: Long, id: String) : this(
        caption,
        ownerUid,
        createdDate
    ) {
        this.id = id
    }
}