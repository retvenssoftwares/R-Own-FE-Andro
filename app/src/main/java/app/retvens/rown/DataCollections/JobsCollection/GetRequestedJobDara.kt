package app.retvens.rown.DataCollections.JobsCollection

data class GetRequestedJobDara(
    val id: String,
    val userID: String,
    val designationType: String,
    val noticePeriod: String,
    val department: String,
    val preferredLocation: String,
    val expectedCTC: String,
    val employmentType: String,
    val requestJobId: String,
    val v: Int,
    val Full_name: String,
    val profile_pic: String,
    val Location: String,
    val jobType: List<String>,
    val jobTitle: List<String>
)
