package app.retvens.rown.DataCollections.JobsCollection

data class GetAllJobsData(
    val _id: String,
    val user_id: String,
    val jobTitle: String,
    val jobType: String,
    val display_status:String,
    val expectedCTC: String,
    val jobLocation: String,
    val jobId: String,
    val companyImage: String, // Optional field, as it may not be present in all jobs
    val companyName: String // Optional field, as it may not be present in all jobs
)
