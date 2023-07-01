package app.retvens.rown.bottomsheet.bottomSheetPeople

data class BottomSheetPeopleData(
    val matchedContacts: List<MatchedContact>,
    val message: String
)

data class MatchedContact(
    var connectionStatus: String,
    val matchedNumber: MatchedNumber
)

data class MatchedNumber(
    val Full_name: String,
    val Profile_pic: String,
    val Role: String,
    val _id: String,
    val connections: List<Connection>,
    val hospitalityExpertInfo: List<Any>,
    val normalUserInfo: List<NormalUserInfo>,
    val requests: List<Request>,
    val userBio: String,
    val display_status: String,
    val User_id: String,
    val verificationStatus: String
)