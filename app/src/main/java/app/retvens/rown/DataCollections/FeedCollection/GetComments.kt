package app.retvens.rown.DataCollections.FeedCollection

data class Comments(
    val user_id: String,
    val comment: String,
    val comment_id: String,
    val date_added: String,
    val Profile_pic: String,
    val User_name: String,
    val _id: String,
//    val replies: List<Comment>
)

data class Posts(
    val _id: String,
    val user_id: String,
    val post_id: String,
    val comments: List<Comments>,
    val __v: Int
)

data class GetComments(
    val post: Posts,
    val commentCount: Int
)

