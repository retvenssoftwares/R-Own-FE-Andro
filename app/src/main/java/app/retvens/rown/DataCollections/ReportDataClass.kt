package app.retvens.rown.DataCollections

data class ReportDataClass(
    val reportType:String,
    val reporterUserId:String,
    val reportedUserId:String,
    val post_id:String,
    val Reason:String
)

data class DeleteAccount(
    val User_id:String
)
data class BlockAccount(
    val user_id:String
)
