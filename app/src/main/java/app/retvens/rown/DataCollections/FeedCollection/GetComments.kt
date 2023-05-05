package app.retvens.rown.DataCollections.FeedCollection

data class GetComments(
    val user_id:String,
    val comment:String?,
    val name:String,
    val profile:String
) {
}