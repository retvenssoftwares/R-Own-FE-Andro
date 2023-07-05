package app.retvens.rown.DataCollections.FeedCollection

data class GetCommunitiesData(
    val _id: String,
    val creatorID: String,
    val creator_name: String,
    val group_name: String,
    val Profile_pic: String,
    val description: String,
    val group_id: String,
    val location: String,
    val latitude: String,
    val longitude: String,
    val community_type: String,
    val Admin: List<Admin>,
    val date_added: String,
    val Members: List<Member>,
    val Totalmember:Int,
    val __v: Int
)

interface User {
    val Role: String
    val location: String
    val Profile_pic: String
    val Full_name: String
    val admin: String
    // Add any other common properties or methods here
}

data class Member(
    val user_id: String,
    override val Full_name: String,
    val address: String,
    val uid: Int,
    override val Profile_pic: String,
    val verificationStatus: String,
    override val location: String,
    override val Role: String,
    override val admin: String,
    val _id: String
) : User

data class Admin(
    val user_id: String,
    override val Full_name: String,
    val address: String,
    val uid: Int,
    override val Profile_pic: String,
    val verificationStatus: String,
    override val location: String,
    override val Role: String,
    override val admin: String,
    val _id: String
) : User

