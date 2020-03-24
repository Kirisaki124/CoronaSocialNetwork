package hava.coronasocialnetwork.model

import android.net.Uri

class Post constructor(
    var caption: String,
    var ownerUid: String,
    var imageURI: Uri,
    var createdDate: String
) {
    constructor() : this("", "", Uri.EMPTY, "")
}