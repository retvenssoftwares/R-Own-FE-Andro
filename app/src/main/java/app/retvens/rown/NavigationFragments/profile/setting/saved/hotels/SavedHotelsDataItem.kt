package app.retvens.rown.NavigationFragments.profile.setting.saved.hotels

data class SavedHotelsDataItem(
    val hotels: List<Hotel>,
    val page: Int,
    val pageSize: Int
)