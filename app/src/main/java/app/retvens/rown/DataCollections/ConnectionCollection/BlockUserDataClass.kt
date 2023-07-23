package app.retvens.rown.DataCollections.ConnectionCollection

data class BlockUserDataClass(
    val _id: String,
    val blockedUser: List<UserData>
)

data class UserData(
    val user_id: String,
    val Full_name: String,
    val Profile_pic: String,
    val _id: String
)