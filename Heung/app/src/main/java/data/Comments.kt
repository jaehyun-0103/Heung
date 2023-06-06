package data

data class Comments(
    val comment_id: String = "",
    val post_id: String? = "",
    val user_nickname: String? = "",
    val comment: String = "",
    val comment_date: String = "",
    val replies: MutableList<Reply> = mutableListOf(),
    var replyCount: Int = 0
)
