package app.retvens.rown.DataCollections.JobsCollection

data class HotelsDataClass(
    val _id: String,
    val user_id: String,
    val hotelName: String,
    val hotelAddress: String,
    val hotelRating: String,
    val hotelLogoUrl: String,
    val hotelCoverpicUrl: String,
    val date_added: String,
    val Hoteldescription: String,
    val hotel_id: String,
    val gallery: List<String>,
    val __v: Int
)

