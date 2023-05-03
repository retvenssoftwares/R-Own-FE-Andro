package app.retvens.rown.NavigationFragments.home

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
        val _id: String?,
        val user_id: String?,
        val caption: String?,
        val likes: List<String>?,
        val comments: List<Comment>?,
        val location: String?,
        val hashtags: List<String>?,
        val media: List<Media>?,
        val post_id: String?,
        val __v: Int?,
        val display_status: String?,
        val saved_post: List<String>?,
        val profile_pic:String?,
        val profileName:String?,
        val profileType:String?,
        val username:String?
    )

    data class Comment(
        val user_id: String?,
        val comment_id: String?,
        val date_added: String?,
        val comment: String?
    )

    data class Media(
        val post: String?,
        val date_added: String?,
        val _id: String?
    )
    data class PostRecyclerData(val UserProfile : Int, val PostPic : Int, val Name : String)
    data class VendorsRecyclerData(val VendorCover : Int, val Profile : Int, val Name : String, val uers : String)
    data class BlogsRecyclerData(val BlogsCover : Int, val BlogTitle : String, val Profile : Int)
    data class AwardsRecyclerData(val Cover : Int)
    data class HotelSectionRecyclerData(val Cover : Int, val Title : String)
    data class CommunityRecyclerData(var image : Int, var title : String, var members : String)
    data class CreateCommunityRecyclerData(var image : Int, var title : String, var members : String)

//    constructor(viewType: Int, recyclerItemList: List<RecyclerItem>) : this(viewType){
//        this.recyclerItemList = recyclerItemList
//    }
//    constructor(viewType: Int, banner: Banner) : this(viewType){
//        this.banner = banner
//    }
}
