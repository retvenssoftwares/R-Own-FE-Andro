package app.retvens.rown.DataCollections.JobsCollection

data class GetApplicantDataClass(
    val user_id:String,
    val jid:String,
    val full_name:String,
    val profile_pic:String
) {
}