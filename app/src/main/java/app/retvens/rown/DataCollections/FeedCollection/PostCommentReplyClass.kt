package app.retvens.rown.DataCollections.FeedCollection


data class PostCommentReplyClass(
    val user_id:String,
    val comment:String,
    val parent_comment_id:String
)
