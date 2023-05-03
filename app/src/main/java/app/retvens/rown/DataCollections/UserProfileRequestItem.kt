package app.retvens.rown.DataCollections

import android.telecom.Call.Details

data class UserProfileRequestItem(

    val Email: String,
    val Full_name: String,
    val Interest: List<Interest>,
    val Mesibo_account: List<MesiboAccount>,
    val Phone: String,
    val Post_count: Int,
    val Profile_pic: String,
    val User_id: String,
    val __v: Int,
    val _id: String,
    val connection_count: Int,
    val vendorInfo:Details,
    val User_name:String
){
    data class Details(
        val vendor_id:String
    )
}