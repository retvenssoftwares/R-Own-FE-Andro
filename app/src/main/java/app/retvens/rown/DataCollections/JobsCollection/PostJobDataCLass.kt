package app.retvens.rown.DataCollections.JobsCollection

data class PostJobDataCLass(
    val user_id:String,
    val hotel_id:String,
    val display_status: String,
    val jobCategory: String,
    val jobTitle: String,
    val companyName: String,
    val workplaceType: String,
    val jobType: String,
    val designationType: String,
    val noticePeriod: String,
    val expectedCTC:String,
    val jobLocation: String,
    val jobDescription: String,
    val skillsRecq: String,
)
