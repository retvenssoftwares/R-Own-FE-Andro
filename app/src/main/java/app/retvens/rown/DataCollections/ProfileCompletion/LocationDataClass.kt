package app.retvens.rown.DataCollections.ProfileCompletion

import javax.sql.StatementEvent

data class LocationDataClass(
    val _id: String,
    val id: Int,
    val name: String,
    val iso3: String,
    val iso2: String,
    val numeric_code: String,
    val phone_code: String,
    val capital: String,
    val currency: String,
    val currency_name: String,
    val currency_symbol: String,
    val tld: String,
    val native: String,
    val region: String,
    val subregion: String,
    val timezones: List<Timezone>,
    val translations: Map<String, String>,
    val latitude: String,
    val longitude: String,
    val emoji: String,
    val emojiU: String,
    val states: List<State>
)

data class Timezone(
    val zoneName: String,
    val gmtOffset: Int,
    val gmtOffsetName: String,
    val abbreviation: String,
    val tzName: String
)

data class State(
    val id: Int,
    val name: String,
    val state_code: String,
    val latitude: String,
    val longitude: String,
    val type: String?,
    val cities: List<City>
)

data class City(
    val id: Int,
    val name: String,
    val latitude: String,
    val longitude: String
)
