package app.retvens.rown.viewAll.viewAllBlogs

data class GetBlogByIdItem(
    val Full_name: String,
    val Profile_pic: String,
    val User_id: String,
    val User_name: String,
    val __v: Int,
    val _id: String,
    val blog_content: String,
    val blog_id: String,
    val blog_image: String,
    val blog_title: String,
    val category_id: String,
    val category_name: String,
    val comments: List<Any>,
    val date_added: String,
    val display_status: String,
    val like: String,
    val likes: List<Like>,
    val saved: String,
    val saved_blog: List<Any>
)