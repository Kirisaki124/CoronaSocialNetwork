package hava.coronasocialnetwork.model

import java.io.File
import java.time.LocalDateTime

data class Post(
    var caption: String,
    var ownerUid: String,
    var image: File,
    var createdDate: LocalDateTime
)