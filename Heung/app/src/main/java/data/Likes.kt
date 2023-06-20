package data

data class Likes(
    val post_id: String = "",
    val userIds: List<String> = emptyList(),
    val like: String = ""
)