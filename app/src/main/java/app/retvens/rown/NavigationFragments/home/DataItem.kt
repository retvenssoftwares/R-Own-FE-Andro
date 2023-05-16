package app.retvens.rown.NavigationFragments.home

import app.retvens.rown.DataCollections.FeedCollection.Profile

data class DataItem(
    val viewType : Int,
    var recyclerItemList : List<RecyclerItem>? = null,
    var postRecyclerDataList : List<PostRecyclerData>? = null,
    val hotelSectionList : List<HotelSectionRecyclerData>? = null,
    var hotelAwardsList : List<AwardsRecyclerData>? = null,
    var blogsRecyclerDataList : List<BlogsRecyclerData>? = null,
    var vendorsRecyclerDataList : List<VendorsRecyclerData>? = null,
    var communityRecyclerDataList : List<CommunityRecyclerData>? = null,
    var createCommunityRecyclerDataList : List<CreateCommunityRecyclerData>? = null,
    var banner : Banner? = null
){

    data class RecyclerItem(val image : Int, val title : String)

    data class Banner(
//        val banner:Post,
//        val posts:Profile
    val image: Int,
    )

    data class Post(
        val _id: String,
        val user_id: String,
        val caption: String,
        val likes: List<String>,
        val comments: List<Comment>,
        val location: String,
        val hashtags: List<String>,
        val media: List<Media>,
        val post_id: String,
        val __v: Int,
        val display_status: String,
        val saved_post: List<String>
    )

    data class Comment(
        val user_id: String,
        val comment_id: String,
        val date_added: String,
        val comment: String?
    )

    data class Media(
        val post: String,
        val date_added: String,
        val _id: String
    )

    data class Profile(
        val hotelOwnerInfo: HotelOwnerInfo?,
        val vendorInfo: VendorInfo?,
        val _id: String,
        val Full_name: String,
        val Email: String,
        val Phone: String,
        val Profile_pic: String,
        val Mesibo_account: List<MesiboAccount>,
        val Interest: List<Interest>,
        val Post_count: Int,
        val connection_count: Int,
        val saved_post: List<String>,
        val Liked_post: List<String>,
        val User_name: String,
        val DOB: String,
        val location: String,
        val Role: String,
        val device_token: String?,
        val studentEducation: List<StudentEducation>,
        val normalUserInfo: List<NormalUserInfo>,
        val hospitalityExpertInfo: List<HospitalityExpertInfo>,
        val User_id: String,
        val __v: Int
    )

    data class HotelOwnerInfo(
        val hotelownerName: String,
        val hotelDescription: String,
        val hotelType: String?,
        val hotelCount: String?,
        val websiteLink: String?,
        val bookingEngineLink: String?,
        val hotelInfo: List<HotelInfo>,
        val hotelownerid: String
    )

    data class HotelInfo(
        val hotel_id: String,
        val _id: String
    )

    data class VendorInfo(
        val vendorImage: String?,
        val vendorName: String?,
        val vendorDescription: String?,
        val portfolioLink: String?,
        val websiteLink: String?,
        val vendor_id: String,
        val vendorServices: List<String>,
        val Id: String
    )

    data class MesiboAccount(
        val uid: Int,
        val address: String,
        val token: String,
        val _id: String
    )

    data class Interest(
        val _id: String
    )

    data class StudentEducation(
        val _id: String,
        val educationPlace: String,
        val education_session_end: String,
        val education_session_start: String
    )

    data class NormalUserInfo(
        val _id: String,
        val hotelDescription: String,
        val jobType: String,
        val jobTitle: String,
        val jobCompany: String,
        val jobStartYear: String,
        val jobEndYear: String
    )

    data class HospitalityExpertInfo(
        val jobtype: String?,
        val jobtitle: String?,
        val jobstartYear: String?,
        val jobendYear: String?,
        val _id: String,
        val userDescription: String?,
        val jobType: String?,
        val jobTitle: String?,
        val hotelCompany: String?,
        val jobStartYear: String?,
        val jobEndYear: String?
    )



    data class PostRecyclerData(val UserProfile : Int, val PostPic : Int, val Name : String)
    data class VendorsRecyclerData(val VendorCover : Int, val Profile : Int, val Name : String, val uers : String)
    data class BlogsRecyclerData(val BlogsCover : Int, val BlogTitle : String, val Profile : Int)
    data class AwardsRecyclerData(val Cover : Int)
    data class HotelSectionRecyclerData(val Cover : Int, val Title : String)
    data class CommunityRecyclerData(var image : Int, var title : String, var members : String, val join : String)
    data class CreateCommunityRecyclerData(var image : Int, var title : String, var members : String)

//    constructor(viewType: Int, recyclerItemList: List<RecyclerItem>) : this(viewType){
//        this.recyclerItemList = recyclerItemList
//    }
//    constructor(viewType: Int, banner: Banner) : this(viewType){
//        this.banner = banner
//    }
}
