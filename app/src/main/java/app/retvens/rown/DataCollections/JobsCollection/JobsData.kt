package app.retvens.rown.DataCollections.JobsCollection

data class JobsData(
    val _id: String,
    val user_id: String,
    val jobCategory: String,
    val jobTitle: String,
    val companyName: String,
    val workplaceType: String?,
    val jobType: String,
    val designationType: String,
    val noticePeriod: String,
    val minSalary: String,
    val maxSalary: String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String,
    val display_status: String,
    val jobApplicants: List<Any>,
    val jid: String,
    val __v: Int
)
