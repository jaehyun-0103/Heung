package data

data class Likes(
    val like_id: String = "",
    val post_id: String = "",
    val userIds: List<String> = emptyList(),
    val like: String = ""
)