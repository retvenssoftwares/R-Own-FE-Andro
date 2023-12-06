package app.retvens.rown.DataCollections

data class MesiboUsersData(
    val uid:Long,
    val address:String,
    val ipaddr:Number,
    val lastonline:Long,
    val active:Number,
    val pinned:Number,
    val online:Number,
    val flag:Number,
    val flags:Number,
    val token:String,
    val appid:String,
    var isSelected: Boolean = false
){

}







//        var mProfile: ConcurrentHashMap<String, MesiboProfile>? = null
//
//        mProfile = Mesibo.getUserProfiles()
//
//        val userList = mutableListOf<MesiboUsersData>()



//        Toast.makeText(applicationContext,mProfile.size.toString(),Toast.LENGTH_SHORT).show()
//




//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//
//
//        })