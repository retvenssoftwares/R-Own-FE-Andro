package app.retvens.rown.DataCollections.ProfileCompletion

data class PostVendorSerivces(
    val user_id:String,
    val serviceId:String
) {

}

data class AddCompanyDataClass(
    val company_name:String,
    val addedbyUser:String
)

data class AddDesignationDataClass(
    val designation_name:String,
    val addedbyUser:String
)