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
    val appid:String
){

}
//val api: Mesibo = Mesibo.getInstance()
//        api.init(applicationContext)
//
//        Mesibo.addListener(this)
//        Mesibo.setAccessToken("a9afa113e47f7f13ca8a5f61ad30c15218971cb9609f51385787e554786c2sa0585e450b2")
//        Mesibo.setDatabase("retvensDB", 0)
//
//        Mesibo.start()






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