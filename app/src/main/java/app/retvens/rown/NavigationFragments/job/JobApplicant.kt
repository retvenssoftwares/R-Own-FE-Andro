package app.retvens.rown.NavigationFragments.job

data class JobApplicant(
    val Full_name: String,
    val Profile_pic: String,
    val User_id: String,
    val location: String,
    val normalUserInfo: List<NormalUserInfo>,
    val verificationStatus: String
)