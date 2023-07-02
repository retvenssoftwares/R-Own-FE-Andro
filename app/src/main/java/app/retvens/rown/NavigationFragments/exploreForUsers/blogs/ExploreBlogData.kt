package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData

data class ExploreBlogData(
    val blogs: List<AllBlogsData>
)
data class BlogData(
    val page: Int,
    val pageSize: Int,
    val blogs: List<Blog>
)

data class Blog(
    val category_name: String,
    val blog_title: String,
    val blog_image: String,
    val User_name: String,
    val Profile_pic: String,
    val Full_name: String,
    val blog_id: String,
    val saved: String,
    val like: String,
    val Like_count: Int,
    val comment_count: Int,
    val display_status: String
)