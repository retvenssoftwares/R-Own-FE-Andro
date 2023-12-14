package app.retvens.rown.DataCollections.JobsCollection

data class JobDetailsDataClass(
    val jobId: String,
    val jobTitle: String,
    val jobLocation: String,
    val jobType: String,
    val expectedCTC: String,
    val jobDescription: String,
    val skillsRecq: String,
    val companyImage: String,
    val companyName: String,
    val websiteLink: String,
    val status: String,
    val people: People
)
data class People(
    val full_name: String,
    val role: String,
    val profile_pic: String,
    val verificationStatus: String
)