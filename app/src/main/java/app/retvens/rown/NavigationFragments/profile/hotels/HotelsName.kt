package app.retvens.rown.NavigationFragments.profile.hotels

data class HotelsName(
    val _id: String,
    val display_status: String,
    val hotel_id: String,
    val hotelCoverpicUrl: String,
    val hotelRating: String,
    val hotelAddress: String,
    val hotelName: String,
    val Hoteldescription:String,
    val gallery: List<GalleryHotel>,
)

data class GalleryHotel(
    val Image1:String,
    val Image2:String,
    val Image3:String
)