package app.retvens.rown.DataCollections.FeedCollection

data class FetchPostDataClass(
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