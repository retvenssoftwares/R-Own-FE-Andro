package app.retvens.rown.DataCollections.ConnectionCollection

import app.retvens.rown.DataCollections.MesiboAccount

data class GetAllRequestDataClass(
    val conns: List<Connection>
)

data class Connection(
    val _id: String,
    val Full_name: String,
    val normalUserInfo: List<NormalUserInfo>,
    val User_id: String,
    val Profile_pic: String,
    val verificationStatus: String,
    val mesiboAccount: List<Mesibo_account>
)

data class Mesibo_account(
    val uid:String,
    val token:String,
    val address:String
)

data class NormalUserInfo(
    val jobTitle: String
)