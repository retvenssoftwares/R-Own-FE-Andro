package app.retvens.rown.NavigationFragments.profile.vendorsReview

data class AllReviewsData(
    val _id: String,
    val userReviews: List<UserReview>,
    val reviews_types: List<UserReview>
)