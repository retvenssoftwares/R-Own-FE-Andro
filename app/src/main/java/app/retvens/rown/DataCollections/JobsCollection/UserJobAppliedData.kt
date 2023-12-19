data class AppliedJobs(
    val _id: String,
    val user_id: String,
    val jobId: String,
    val status: String,
    val jobType: String,
    val jobTitle: String,
    val jobLocation: String,
    val companyImage: String,
    val companyName: String
)

data class UserJobAppliedData(
    val page: Int,
    val pageSize: Int,
    val jobs: List<AppliedJobs>,
    val message:String
)

