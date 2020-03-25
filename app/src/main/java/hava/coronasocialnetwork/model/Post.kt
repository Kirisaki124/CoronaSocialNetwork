package hava.coronasocialnetwork.model

class Post constructor(
    var caption: String,
    var ownerUid: String,
    var createdDate: String
) {
    var id: String = ""

    constructor() : this("", "", "")
    constructor(caption: String, ownerUid: String, createdDate: String, id: String) : this(
        caption,
        ownerUid,
        createdDate
    ) {
        this.id = id
    }
}