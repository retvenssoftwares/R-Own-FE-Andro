package app.retvens.rown.NavigationFragments.job.savedJobs

data class SavedJob(
    val jobTitle: String,
    val jobType: String,
    val expectedCTC: String,
    val jobLocation: String,
    val companyImage: String? = null,  // Optional, as it's not present in all jobs
    val companyName: String? = null     // Optional, as it's not present in all jobs
)

data class SavedJobsData(
    val page: Int,
    val pageSize: Int,
    val jobs: List<SavedJob>
)

