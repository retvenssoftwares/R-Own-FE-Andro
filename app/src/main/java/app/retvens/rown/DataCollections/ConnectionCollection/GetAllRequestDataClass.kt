package app.retvens.rown.DataCollections.ConnectionCollection

data class GetAllRequestDataClass(
    val conns: List<Connection>
)

data class Connection(
    val _id: String,
    val Full_name: String,
    val normalUserInfo: List<NormalUserInfo>,
    val User_id: String,
    val Profile_pic: String,
    val verificationStatus: String
)

data class NormalUserInfo(
    val jobTitle: String
)