package app.retvens.rown.DataCollections.ConnectionCollection

import app.retvens.rown.NavigationFragments.exploreForUsers.people.MesiboAccount

data class NormalUserDataClass(
    val data: ProfileData
)

data class ProfileData(
    val profile: Profile,
    val postCountLength: Int,
    val connCountLength: Int,
    val reqsCountLength: Int,
    val connectionStatus: String
)

data class Profile(
    val _id: String,
    val Full_name: String,
    val Profile_pic: String,
    val verificationStatus: String,
    val userBio: String,
    val User_name: String,
    val Mesibo_account: List<MesiboAccount>,
    val location: String,
    val normalUserInfo: List<NormalUserInfoo>,
    val User_id: String,
    val post_count: List<Any>,
    val Created_On: String
)

data class NormalUserInfoo(
    val jobType:String,
    val jobCompany:String,
    val jobStartYear:String,
    val jobEndYear:String,
    val jobTitle:String
)

data class hospitalityExpertInfo(
    val jobtype:String,
    val hotelCompany:String,
    val jobstartYear:String,
    val jobendYear:String,
    val jobtitle:String
)

