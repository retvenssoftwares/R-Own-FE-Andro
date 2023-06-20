package app.retvens.rown.DataCollections.ConnectionCollection

data class RoleDetails(
    val vendorInfo: VendorInfo,
    val _id: String,
    val Full_name: String,
    val Profile_pic: String,
    val verificationStatus: String,
    val userBio: String,
    val User_name: String,
    val location: String,
    val post_count: List<Post>,
    val Created_On: String
)

data class VendorInfo(
    val vendorDescription: String,
    val websiteLink: String
)

data class Post(
    val post_id: String,
    val date_added: String,
    val _id: String
)

data class VendorProfileDataClass(
    val roleDetails: RoleDetails,
    val postcount: Int,
    val connectioncount: Int,
    val requestcount: Int,
    val connectionStatus: String
)
