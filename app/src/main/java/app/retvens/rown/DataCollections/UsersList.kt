package app.retvens.rown.DataCollections

data class UsersList(
    val userscount:Number,
    val count:Number,
    val users:List<MesiboUsersData>,
    val op:String,
    val result:Boolean
){

}
