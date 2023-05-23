package app.retvens.rown.DataCollections.FeedCollection

data class PostsDataClass(
    val _id: String,
    val user_id: String,
    val location: String,
    val post_type: String?,
    val Event_location: String,
    val Event_name: String,
    val checkinLocation: String,
    val checkinVenue: String,
    var caption: String?,
    val hashtags: List<String>,
    val media: Media,
    val post_id: String,
    val display_status: String,
    val Profile_pic:String,
    var User_name:String,
    val saved_post: List<String>,
    val pollQuestion: List<PollQuestion>,
    val likes: List<Like>,

    )

data class Media(
    val post: String,
    val date_added: String,
    val _id: String
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
    val votes: List<String>,
    val date_added: String
)

data class Like(
    val _id: String,
    val post_id: String,
    val user_id: String,
    val likes: List<String>,
    val __v: Int
)


