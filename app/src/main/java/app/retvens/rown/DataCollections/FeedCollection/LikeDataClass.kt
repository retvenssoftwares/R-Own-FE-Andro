package app.retvens.rown.DataCollections.FeedCollection

data class Like(
    val user_id: String,
    val jobTitle: String,
    val date_added: String,
    val Profile_pic: String,
    val User_name: String,
    val Full_name:String,
    val Role:String,
    val verificationStatus: String,
    val display_status: String,
    val _id: String
)

data class Post(
    val _id: String,
    val post_id: String,
    val user_id: String,
    val likes: List<Like>,
    val __v: Int
)

data class LikeDataClass(
    val post: Post,
    val likeCount: Int
)
