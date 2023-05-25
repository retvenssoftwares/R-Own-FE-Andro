package app.retvens.rown.NavigationFragments.home

import app.retvens.rown.DataCollections.FeedCollection.PostItem
import app.retvens.rown.DataCollections.FeedCollection.PostsDataClass


data class DataItem(
    val viewType: Int,
    var recyclerItemList: List<RecyclerItem>? = null,
    var postRecyclerDataList: List<PostRecyclerData>? = null,
    val hotelSectionList: List<HotelSectionRecyclerData>? = null,
    var hotelAwardsList: List<AwardsRecyclerData>? = null,
    var blogsRecyclerDataList: List<BlogsRecyclerData>? = null,
    var vendorsRecyclerDataList: List<VendorsRecyclerData>? = null,
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
    data class CommunityRecyclerData(var image : Int, var title : String, var members : String, val join : String)
    data class CreateCommunityRecyclerData(var image : Int, var title : String, var members : String)

//    constructor(viewType: Int, recyclerItemList: List<RecyclerItem>) : this(viewType){
//        this.recyclerItemList = recyclerItemList
//    }
//    constructor(viewType: Int, banner: Banner) : this(viewType){
//        this.banner = banner
//    }
}
