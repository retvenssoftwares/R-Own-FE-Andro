package app.retvens.rown.NavigationFragments.job

data class JobCantidateDetailsData(
    val Full_name: String,
    val Profile_pic: String,
    val User_name: String,
    val expectedCTC: String,
    val experience: String,
    val jobTitle: String,
    val jobType: String,
    val location: String,
    val noticePeriod: String,
    val preferredLocation: String,
    val previousExperience: List<PreviousExperience>,
    val resume: String,
    val applicationId:String,
    val self_introduction: String
)