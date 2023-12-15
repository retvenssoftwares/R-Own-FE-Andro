package app.retvens.rown.NavigationFragments.job.savedJobs

data class JobApplicant(
    val Full_name: String,
    val Profile_pic: String,
    val verificationStatus: String,
    val location: String,
    val normalUserInfo: List<NormalUserInfo>,
    val User_id: String
)