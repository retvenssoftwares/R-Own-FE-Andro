package app.retvens.rown.DataCollections.JobsCollection

data class CandidateDataClass(
    val _id: String,
    val user_id: String,
    val Full_name: String,
    val Experience: String,
    val resume: String,
    val jid: String,
    val status: String,
    val self_introduction: String,
    val applicationId: String,
    val __v: Int,
    val User_name: String,
    val Profile_pic: String,
    val jobType: List<String>
)
