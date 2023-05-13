package app.retvens.rown.DataCollections.JobsCollection

data class RequestJobDataClass(
    val userID: String,
    val department: String,
    val employmentType: String,
    val designationType: String,
    val noticePeriod: String,
    val preferredLocation: String,
    val expectedCTC: String,
)

