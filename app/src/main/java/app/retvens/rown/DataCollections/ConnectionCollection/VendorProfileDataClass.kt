package app.retvens.rown.DataCollections.ConnectionCollection

data class VendorProfileDataClass(
    val vendorInfo: VendorInfo,
    val _id: String,
    val verificationStatus: String,
    val location: String,
    val post_count: List<Any>,
    val Created_On: String,
    val postcount: Int,
    val connectioncount: Int,
    val requestcount: Int,
    val connectionStatus: String
)

data class VendorInfo(
    val vendorImage: String,
    val vendorName: String,
    val vendorDescription: String,
    val websiteLink: String
)

