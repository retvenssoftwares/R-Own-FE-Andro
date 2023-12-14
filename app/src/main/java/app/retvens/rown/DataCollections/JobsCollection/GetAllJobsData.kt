package app.retvens.rown.DataCollections.JobsCollection

import com.google.gson.annotations.SerializedName

data class GetAllJobsData(
    val _id: String,
    val user_id: String,
    val jobTitle: String,
    val jobType: String,
    val expectedCTC: String,
    val jobLocation: String,
    val display_status: String,
    val jobId: String,
    @SerializedName("companyImage")
    val companyImage: String,
    @SerializedName("companyName")
    val companyName: String
)
