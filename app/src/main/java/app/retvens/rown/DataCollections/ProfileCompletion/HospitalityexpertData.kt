package app.retvens.rown.DataCollections.ProfileCompletion

data class HospitalityexpertData(
    val hospitalityExpertInfo:JobDetail
)
{
    data class JobDetail(
        val jobType:String,
        val jobTitle:String,
        val jobCompany:String,
        val jobStartYear:String,
        val jobEndYear:String
    )
}
