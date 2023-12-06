package app.retvens.rown.NavigationFragments.profile.vendorsReview

data class VendorReviewsData(
    val review_count : ReviewCount,
    val quickReviewImage : String,
    val reviews_name : String
)

data class ReviewCount(
    val User_id : String,
    val count : String
)