package app.retvens.rown.DataCollections.ProfileCompletion

data class JobData(
    val normalUserInfo:JobDetails
)
{
    data class JobDetails(
        val jobType:String,
        val jobTitle:String,
        val jobCompany:String,
        val jobStartYear:String,
        val jobEndYear:String
    ){

    }
}
