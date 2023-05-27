package data

data class Calendar (
    val cal_id: String = "",
    val user_id: String = "",
    val cal_date: String = "",
    var cal_startDate: Long = 0L,
    var cal_endDate: Long = 0L,
    val cal_location: String = "",
    val cal_memo: String = "",
    val cal_title: String = ""
)