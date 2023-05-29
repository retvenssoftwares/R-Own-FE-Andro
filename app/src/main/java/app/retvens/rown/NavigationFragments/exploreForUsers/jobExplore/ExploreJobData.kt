package app.retvens.rown.NavigationFragments.exploreForUsers.jobExplore

data class JobPost(
    val _id: String,
    val jobTitle: String,
    val companyName: String,
    val jobType: String,
    val jobLocation: String,
    val hotelLogoUrl: String?,
    val vendorimg: String?
)

data class ExploreJobData(
    val posts: List<JobPost>
)

