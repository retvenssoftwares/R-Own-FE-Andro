package app.retvens.rown.DataCollections.JobsCollection

import com.google.gson.annotations.SerializedName

data class GetAllJobsData(
    @SerializedName("_id")
    val _id: String,
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("jobTitle")
    val jobTitle: String,
    @SerializedName("jobType")
    val jobType: String,
    @SerializedName("expectedCTC")
    val expectedCTC: String,
    @SerializedName("jobLocation")
    val jobLocation: String,
    @SerializedName("display_status")
    val display_status: String,
    @SerializedName("jobId")
    val jobId: String,
    @SerializedName("companyImage")
    val companyImage: String?,
    @SerializedName("companyName")
    val companyName: String?
)
