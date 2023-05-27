package app.retvens.rown.NavigationFragments.job.savedJobs

data class Job(
    val Bookmarked: List<Bookmarked>,
    val __v: Int,
    val _id: String,
    val companyName: String,
    val designationType: String,
    val display_status: String,
    val expectedCTC: String,
    val hotelLogoUrl: String,
    val jid: String,
    val jobApplicants: List<JobApplicant>,
    val jobCategory: String,
    val jobDescription: String,
    val jobLocation: String,
    val jobTitle: String,
    val jobType: String,
    val noticePeriod: String,
    val skillsRecq: String,
    val user_id: String,
    val vendorimg: String,
    val workplaceType: String
)