package app.retvens.rown.DataCollections

import android.telecom.Call.Details
import app.retvens.rown.DataCollections.ConnectionCollection.NormalUserInfoo
import app.retvens.rown.NavigationFragments.PortfolioImages
import app.retvens.rown.NavigationFragments.profile.hotels.GalleryImages
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem

data class UserProfileRequestItem(

    val Email: String,
    val Full_name: String?,
    val normalUserInfo: List<NormalUserInfoo>,
    val hotelOwnerInfo: HotelOwnerInfo,
    val Interest: List<Interest>,
    val Mesibo_account: List<MesiboAccount>,
    val Phone: String,
    val studentEducation:List<StudentEducation>,
    val Post_count: Int,
    val Profile_pic: String?,
    val User_id: String,
    val Role: String,
    val userBio: String,
    val Gender: String,
    val display_status: String,
    val verificationStatus: String,
    val profileCompletionStatus: String,
    val __v: Int,
    val _id: String,
    val connection_count: Int,
    val vendorInfo:Details,
    var isClick:Boolean,
    val User_name:String?,
    var isSelected:Boolean
){
    data class Details(
        val vendorImage:String,
        val vendorDescription:String,
        val websiteLink:String,
        val vendorServices:List<ProfileServicesDataItem>,
        val portfolioLink:List<PortfolioImages>,
    )
    data class HotelOwnerInfo(
        val hotelownerName:String,
        val hotelDescription:String,
        val hotelType:String,
        val websiteLink:String,
        val bookingEngineLink:String
    )

    data class StudentEducation(
        val educationPlace:String,
        val education_session_start:String,
        val education_session_end:String
    )
}