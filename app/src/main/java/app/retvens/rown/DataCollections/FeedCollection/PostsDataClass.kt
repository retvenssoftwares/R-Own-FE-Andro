package app.retvens.rown.DataCollections.FeedCollection

import app.retvens.rown.NavigationFragments.home.DataItem
import app.retvens.rown.NavigationFragments.profile.hotels.HotelData
import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem
import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData

data class PostItem(
    val _id: String,
    val user_id: String,
    val caption: String,
    val location: String,
    val post_type: String,
    val Can_See: String,
    val Can_comment: String,
    val date_added:String,
    val Event_location: String,
    val Event_name: String,
    val checkinLocation: String,
    val checkinVenue: String,
    val pollQuestion: List<PollQuestion>,
    val display_status: String,
    val media: List<Media>,
    val Profile_pic: String,
    val User_name: String,
    val event_thumbnail:String,
    val event_start_date:String,
    val verificationStatus: String,
    val price:String,
    var like:String,
    var liked:String,
    var islike:Boolean,
    var Like_count:String,
    val Comment_count:String,
    val hotel_id:String,
    val hotelAddress:String,
    val hotelName:String,
    val hotelCoverpicUrl:String,
    val bookingengineLink:String,
    val likeCount:String,
    val commentCount:String,
    val Role:String,
    val postid: String,
    val post_id: String,
    val voted:String,
    val saved_post: List<Any>,
    val Full_name:String,
    var isSaved:String,
    val saved:String,
    val __v: Int
)

data class PollQuestion(
    val Question: String,
    val Options: List<Option>,
    val _id: String,
    val question_id: String,
    val date_added: String
)

data class Option(
    val Option: String,
    val _id: String,
    val option_id: String,
    val votes: List<Vote>,
    val date_added: String
)

data class Vote(
    val user_id: String,
    val _id: String
)

data class Media(
    val post: String,
    val date_added: String,
    val _id: String
)

data class PostsDataClass(
    val posts: List<PostItem>,
    val blogs: List<AllBlogsData>,
    val hotels:List<HotelData>,
    val communities:List<DataItem.CommunityRecyclerData>,
    val services:List<ProfileServicesDataItem>,
    val page:Int,
    val message:String,
    val pageSize:Int
)

