package app.retvens.rown.DataCollections.ProfileCompletion

data class BasicInfoClass(
    val Role: String,
    val studentEducation: EducationData
) {
    data class EducationData(
        val educationPlace: String,
        val education_session_start: String,
        val education_session_end: String
        )
}