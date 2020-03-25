package hava.coronasocialnetwork.model

import android.net.Uri

class User constructor(
    val username: String,
    val email: String,
    val phone: String,
    val avatar: Uri
) {

    constructor() : this("", "", "", Uri.EMPTY)

}

