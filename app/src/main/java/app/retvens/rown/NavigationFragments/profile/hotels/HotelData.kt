package app.retvens.rown.NavigationFragments.profile.hotels

data class HotelData(
    val _id: String,
    val user_id: String,
    val hotelName: String,
    val hotelAddress: String,
    val hotelRating: String,
    val hotelLogoUrl: String,
    val hotelCoverpicUrl: String,
    val date_added: String,
    val Hoteldescription: String,
    val display_status: String,
    val hotel_id: String,
    val saved:String,
    val gallery: List<GalleryItem>,
    val __v: Int
)

data class GalleryItem(
    val Image1: String,
    val Image2: String,
    val Image3: String,
    val _id: String
)
