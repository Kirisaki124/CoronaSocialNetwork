package hava.coronasocialnetwork.model

class Post constructor(
    var caption: String,
    var ownerUid: String,
    var image: String,
    var createdDate: String
) {
    constructor() : this("", "", "", "")
}