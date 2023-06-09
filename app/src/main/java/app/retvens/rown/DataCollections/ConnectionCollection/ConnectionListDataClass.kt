package app.retvens.rown.DataCollections.ConnectionCollection

data class ConnectionListDataClass(
    val conns: List<Connections>
)

data class Connections(
    val _id: String,
    val Full_name: String,
    val Phone: String,
    val Profile_pic: String,
    val Mesibo_account: List<MesiboAccount>,
    val verificationStatus: String,
    val normalUserInfo: List<NormalUserInfos>,
    val User_id: String
)

data class MesiboAccount(
    val uid: Int,
    val address: String,
    val token: String,
    val _id: String
)

data class NormalUserInfos(
    val jobTitle: String
)
