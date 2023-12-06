package app.retvens.rown.NavigationFragments.exploreForUsers.blogs

import app.retvens.rown.viewAll.viewAllBlogs.AllBlogsData

data class ExploreBlogData(
    val blogs: List<AllBlogsData>,
    val message:String
)