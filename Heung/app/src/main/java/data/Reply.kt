package data

data class Reply(
    val reply_id: String?,
    val comment_id: String?,
    val user_id: String?,
    val reply: String?,
    val reply_date: String?
) {
    constructor() : this(null, null, null, null, null)
}
