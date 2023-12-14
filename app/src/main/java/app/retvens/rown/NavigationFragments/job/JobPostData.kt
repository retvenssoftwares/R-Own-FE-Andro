package app.retvens.rown.NavigationFragments.job

data class JobPostData(
    val designationType:String,
    val jobTitle: String,
    val companyId:String,
    val workplaceType: String,
    val jobType: String,
    val expectedCTC: String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String)