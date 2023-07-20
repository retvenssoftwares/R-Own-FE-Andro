package app.retvens.rown.DataCollections.ProfileCompletion

    data class JobDetail(
        val userDescription:String,
        val jobtype:String,
        val jobtitle:String,
        val hotelCompany:String,
        val jobstartYear:String,
        val jobendYear:String
    )

data class updateExpertDetail(
    val index:Int,
    val jobtype:String,
    val jobtitle:String,
    val hotelCompany:String,
    val jobstartYear:String,
    val jobendYear:String
)
