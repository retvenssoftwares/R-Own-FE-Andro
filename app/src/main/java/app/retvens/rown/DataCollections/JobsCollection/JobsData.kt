package app.retvens.rown.DataCollections.JobsCollection

data class JobsData(
    val _id: String,
    val user_id: String,
    val joblist: List<JobDetails>,
    val __v: Int,
    val jobApplicants: List<JobApplicant>
)

data class JobDetails(
    val display_status: String,
    val jID: String,
    val jobCategory: String,
    val jobTitle: String,
    val companyName: String,
    val workplaceType: String,
    val jobType: String,
    val designationType: String,
    val noticePeriod: String,
    val minSalary: String,
    val maxSalary: String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String,
    val _id: String
)

data class JobApplicant(
    val user_id: String,
    val jid: String,
    val _id: String
)

