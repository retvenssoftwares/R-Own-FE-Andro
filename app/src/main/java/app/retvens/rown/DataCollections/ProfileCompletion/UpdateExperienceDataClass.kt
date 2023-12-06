package app.retvens.rown.DataCollections.ProfileCompletion

data class UpdateExperienceDataClass(
    val index:Int,
    val jobType:String,
    val jobCompany:String,
    val jobStartYear:String,
    val jobEndYear:String,
    val jobTitle:String
)
