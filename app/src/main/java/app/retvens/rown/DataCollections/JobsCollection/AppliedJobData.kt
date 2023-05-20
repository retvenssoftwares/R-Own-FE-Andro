package app.retvens.rown.DataCollections.JobsCollection

data class AppliedJobData(
    val _id: String,
    val user_id: String,
    val Full_name: String,
    val Experience: String,
    val resume: String,
    val jid: String,
    val applicationId: String,
    val status: String,
    val self_introduction: String,
    val __v: Int,
    val jobData: JobData
)

data class JobData(
    val _id: String,
    val user_id: String,
    val jobCategory: String,
    val jobTitle: String,
    val companyName: String,
    val workplaceType: String,
    val jobType: String,
    val designationType: String,
    val noticePeriod: String,
    val expectedCTC: String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String,
    val display_status: String,
    val jobApplicants: List<Any>,
    val jid: String,
    val Bookmarked: List<Any>,
    val __v: Int
)
