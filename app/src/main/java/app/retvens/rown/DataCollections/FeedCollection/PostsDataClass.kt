package app.retvens.rown.DataCollections.FeedCollection

data class PostItem(
    val _id: String,
    val user_id: String,
    val caption: String,
    val location: String,
    val post_type: String,
    val Can_See: String,
    val Can_comment: String,
    val Event_location: String,
    val Event_name: String,
    val checkinLocation: String,
    val checkinVenue: String,
    val pollQuestion: List<PollQuestion>,
    val display_status: String,
    val media: List<Media>,
    val Profile_pic: String,
    val User_name: String,
    val post_id: String,
    val saved_post: List<Any>,
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
    val votes: List<Any>,
    val date_added: String
)

data class Media(
    val post: String,
    val date_added: String,
    val _id: String
)

data class PostsDataClass(
    val posts: List<PostItem>
)

