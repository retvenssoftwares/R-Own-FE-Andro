package app.retvens.rown.NavigationFragments.home

import app.retvens.rown.DataCollections.FeedCollection.*
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData


data class DataItem(
    val viewType: Int,
    var recyclerItemList: List<RecyclerItem>? = null,
    var postRecyclerDataList: List<PostRecyclerData>? = null,
    val hotelSectionList: List<HotelData>? = null,
    var hotelAwardsList: List<AwardsRecyclerData>? = null,
    var blogsRecyclerDataList: List<AllBlogsData>? = null,
    var vendorsRecyclerDataList: List<ProfileServicesDataItem>? = null,
    var communityRecyclerDataList: List<CommunityRecyclerData>? = null,
    var createCommunityRecyclerDataList: List<CreateCommunityRecyclerData>? = null,
    var banner: PostItem? = null
){

    data class RecyclerItem(val image : Int, val title : String)

    data class Banner(
        val posts:PostsDataClass
    )





    data class PostRecyclerData(val UserProfile : Int, val PostPic : Int, val Name : String)
    data class VendorsRecyclerData(val VendorCover : Int, val Profile : Int, val Name : String, val uers : String)
    data class BlogsRecyclerData(val BlogsCover : Int, val BlogTitle : String, val Profile : Int)
    data class AwardsRecyclerData(val Cover : Int)
    data class HotelSectionRecyclerData(val Cover : Int, val Title : String)
    data class CommunityRecyclerData(val _id: String,
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
                                     val __v: Int)
    data class CreateCommunityRecyclerData(var image : Int, var title : String, var members : String)

//    constructor(viewType: Int, recyclerItemList: List<RecyclerItem>) : this(viewType){
//        this.recyclerItemList = recyclerItemList
//    }
//    constructor(viewType: Int, banner: Banner) : this(viewType){
//        this.banner = banner
//    }
}
