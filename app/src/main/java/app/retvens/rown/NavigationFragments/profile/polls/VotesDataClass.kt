package app.retvens.rown.NavigationFragments.profile.polls

data class VotesDataClass(
    val Question: String,
    val date_added: String,
    val Options: List<Option>,
    val _id: String,
    val question_id: String
)

data class Option(
    val Option: String,
    val date_added: String,
    val _id: String,
    val option_id: String,
    val votes: List<Vote>
)

data class Vote(
    val user_id: String,
    val profile_pic: String,
    val job_title: String,
    val Full_name: String,
    val Verification_status: String
)

