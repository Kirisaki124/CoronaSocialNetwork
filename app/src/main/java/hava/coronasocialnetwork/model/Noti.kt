package hava.coronasocialnetwork.model

class Noti constructor(
    var type: String
) {
    companion object {
        const val CHAT_NOTIFICATION = "ChatNotification"
        const val LIKE_NOTIFICATION = "LikeNotification"
        const val COMMENT_NOTIFICATION = "CommentNotification"
        const val ADD_FRIEND_NOTIFICATION = "AddFriendNotification"
    }

    var id: String = ""
    var senderId: String = ""
    var postId: String = ""
    var chatRoomId: String = ""
    var createdDate: Long = 0

    constructor() : this("")
    constructor(
        type: String,
        id: String,
        postIdOrFriendIdOrChatRoomId: String,
        senderId: String,
        createdDate: Long
    ) : this(
        type
    ) {
        this.id = id
        this.senderId = senderId
        this.createdDate = createdDate
        if (type == LIKE_NOTIFICATION || type == COMMENT_NOTIFICATION) {
            this.postId = postIdOrFriendIdOrChatRoomId
        } else if (type == CHAT_NOTIFICATION) {
            this.chatRoomId = postIdOrFriendIdOrChatRoomId
        }
    }
}