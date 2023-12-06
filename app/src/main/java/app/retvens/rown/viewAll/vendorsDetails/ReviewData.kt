package app.retvens.rown.viewAll.vendorsDetails

import app.retvens.rown.NavigationFragments.profile.vendorsReview.UploadReviewsIdData

data class ReviewData(
    val user_id : String,
    val reviewsId : MutableList<UploadReviewsIdData>?,
    val Rating_number : String,
    val Description : String
)
