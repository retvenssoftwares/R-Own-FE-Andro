package app.retvens.rown.DataCollections

data class NotificationResponse (
    val multicast_id:Long,
    val success:Int,
    val failure:Int
        ){
}