package app.retvens.rown.viewAll.viewAllBlogs

import app.retvens.rown.DataCollections.FeedCollection.Reply

//class CommentBlog : ArrayList<CommentBlogSubList>()
//class CommentBlogSubList : ArrayList<BlogAllComments>()
data class BlogAllComments(
    val _id: String,
    val blog_image: String,
    val Profile_pic: String,
    val User_name: String,
    val category_name: String,
    val blog_title: String,
    val blog_content: String,
    val category_id: String,
    val User_id: String,
    val likes: List<Like>,
    val comments: List<Comment>,
    val saved_blog: List<Any>,
    val blog_id: String,
    val date_added: String,
    val __v: Int
)

data class Like(
    val user_id: String,
    val _id: String,
    val date_added: String
)

data class Comment(
    val user_id: String,
    val comment: String,
    val User_name: String,
    val Full_name: String,
    val Profile_pic: String,
    val comment_id: String,
    val date_added: String,
    val Role: String,
    val _id: String,
    val replies: List<Reply>
)

data class Reply(
    val user_id: String,
    val reply: String,
    val User_name: String,
    val Full_name: String,
    val Profile_pic: String,
    val reply_id: String,
    val date_added: String,
    val _id: String
)