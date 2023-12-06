package app.retvens.rown.Dashboard.profileCompletion.frags.adapter

import android.net.Uri

data class HotelChainData(
    val coverImage : Int,
    val hotelNameHint : String,
    val hotelStarHint : String,
    val locationHint : String,
    val coverUri: Uri?
)
