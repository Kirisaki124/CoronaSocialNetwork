package hava.coronasocialnetwork.model

class Noti constructor(
    var message: String,
    var type: String
) {
    companion object {
        const val CHAT_NOTIFICATION = "ChatNotification"
        const val LIKE_NOTIFICATION = "LikeNotification"
        const val COMMENT_NOTIFICATION = "CommentNotification"
        const val ADD_FRIEND_NOTIFICATION = "AddFriendNotification"
    }

    var id: String = ""
    var postId: String = ""
    var friendId: String = ""

    constructor() : this("", "")
    constructor(message: String, type: String, id: String, postIdOrFriendId: String) : this(
        message,
        type
    ) {
        this.id = id
        if (type == LIKE_NOTIFICATION || type == COMMENT_NOTIFICATION) {
            this.postId = postIdOrFriendId
        } else if (type == ADD_FRIEND_NOTIFICATION) {
            this.friendId = postIdOrFriendId
        }
    }
}