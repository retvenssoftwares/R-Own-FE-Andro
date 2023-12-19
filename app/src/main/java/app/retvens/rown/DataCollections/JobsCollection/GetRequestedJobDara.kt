package app.retvens.rown.DataCollections.JobsCollection

data class GetRequestedJobDara(
    val userId: String,
    val designationType: String,
    val noticePeriod: String,
    val department: String,
    val preferredLocation: String,
    val expectedCTC: String,
    val employmentType: String,
    val status: String,
    val display_status: String,
    val requestJob_id: String,
    val Full_name: String,
    val profile_pic: String,
    val verificationStatus: String,
    val Location: String,
    val jobType: String,
    val jobTitle: String
)
