package app.retvens.rown.NavigationFragments.job.savedJobs

import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfo


data class JobApplicant(
    val Full_name: String,
    val Profile_pic: String,
    val verificationStatus: String,
    val location: String,
    val normalUserInfo: List<NormalUserInfo>,
    val userId: String
)