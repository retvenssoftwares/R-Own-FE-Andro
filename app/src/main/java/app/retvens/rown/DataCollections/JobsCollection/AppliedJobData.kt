package app.retvens.rown.DataCollections.JobsCollection

data class AppliedJobData(
    val userID: String,
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
    val offeredCTC: String
)

