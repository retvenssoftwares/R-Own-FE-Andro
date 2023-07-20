package app.retvens.rown.NavigationFragments.exploreForUsers.people

data class ExplorePeopleDataClass(
    val posts: List<Post>,
    val message:String
)

data class Post(
    val _id: String,
    val Full_name: String,
    val Mesibo_account: List<MesiboAccount>,
    val User_name: String,
    val Role: String,
    val User_id: String,
    val verificationStatus: String,
    val userBio: String,
    val Profile_pic: String,
    val display_status: String,
    var connectionStatus: String,
    val userID:String
)

data class MesiboAccount(
    val uid: Int,
    val address: String,
    val token: String,
    val _id: String
)
