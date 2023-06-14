package app.retvens.rown.NavigationFragments.profile.hotels

data class HotelData(
    val Hoteldescription: String,
    val __v: Int,
    val _id: String,
    val display_status: String,
    val date_added: String,
    val gallery: List<GalleryImages>,
    val hotelCoverpicUrl: String,
    val hotelLogoUrl: String,
    val hotelName: String,
    val hotelProfilepicUrl: String,
    val hotel_id: String,
    val hotelAddress: String,
    val saved: String,
    val user_id: String
)