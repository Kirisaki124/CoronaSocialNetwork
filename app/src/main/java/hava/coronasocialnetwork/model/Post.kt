package hava.coronasocialnetwork.model

import java.io.File

data class Post(
    var caption: String,
    var ownerUid: String,
    var image: File,
    var createdDate: String
)