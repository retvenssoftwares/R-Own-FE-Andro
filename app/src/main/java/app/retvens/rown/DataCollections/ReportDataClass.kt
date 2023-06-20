package app.retvens.rown.DataCollections

data class ReportDataClass(
    val reportType:String,
    val reporterUserId:String,
    val reportedUserId:String,
    val post_id:String,
    val reasons:List<Reasons>
)
data class Reasons(
    val reason:String
)