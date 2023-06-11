package app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore

data class ExploreJobData(
    val posts: List<Job>
)

data class Job(
    val _id: String,
    val jid: String,
    val jobTitle: String,
    val companyName: String,
    val jobType: String,
    val designationType: String,
    val noticePeriod: String,
    val expectedCTC: String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String,
    val hotelLogoUrl: String,
    val applyStatus: String
)


