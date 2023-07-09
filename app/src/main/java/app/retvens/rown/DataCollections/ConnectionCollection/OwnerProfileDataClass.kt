package app.retvens.rown.DataCollections.ConnectionCollection

import app.retvens.rown.NavigationFragments.exploreForUsers.people.MesiboAccount

data class OwnerProfileDataClass(
    val profiledata: ProfileOwnerData,
    val hotellogo: HotelLogo,
    val connection_Count: Int,
    val requests_count: Int,
    val post_count: Int,
    val profile: Profiles,
    val connectionStatus: String
)

data class ProfileOwnerData(
    val _id: String,
    val Profile_pic: String,
    val userBio: String,
    val Mesibo_account: List<MesiboAccount>,
    val User_name: String,
    val post_count: List<Any>,
    val Full_name:String
)

data class HotelLogo(
    val _id: String,
    val hotelLogoUrl: String
)

data class Profiles(
    val hotelOwnerInfo: HotelOwnerInfo,
    val _id: String,
    val verificationStatus: String,
    val location: String,
    val Created_On: String
)

data class HotelOwnerInfo(
    val hotelDescription: String,
    val websiteLink: String
)

