package app.retvens.rown.NavigationFragments.exploreForUsers.services

import app.retvens.rown.NavigationFragments.profile.services.ProfileServicesDataItem

data class ExploreServiceData(
    val events : List<ProfileServicesDataItem>,
    val vendors : List<ProfileServicesDataItem>
    )