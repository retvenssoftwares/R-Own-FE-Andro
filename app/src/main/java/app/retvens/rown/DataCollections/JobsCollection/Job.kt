package app.retvens.rown.DataCollections.JobsCollection

data class Job(
    val _id: String,
    val companyImage: String,
    val companyName: String,
    val display_status: String,
    val expectedCTC: String,
    val jobId: String,
    val jobLocation: String,
    val jobTitle: String,
    val jobType: String,
    val saved: String,
    val user_id: String
)