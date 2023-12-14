package app.retvens.rown.DataCollections.JobsCollection

import app.retvens.rown.NavigationFragments.job.savedJobs.JobApplicant

data class  JobDetailsData(
    val jobId: String,
    val jobTitle: String,
    val jobLocation: String,
    val jobType: String,
    val expectedCTC: String,
    val jobDescription: String,
    val skillsRecq: String,
    val companyImage: String,
    val companyName: String,
    val jobApplicants: List<JobApplicant>
)