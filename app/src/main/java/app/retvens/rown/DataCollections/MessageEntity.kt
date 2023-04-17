package app.retvens.rown.DataCollections

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class MessageEntity(
    @PrimaryKey val id: Long,
    val sender: String,
    val receiver: String,
    val message: String,
    val timestamp: Long ,
    var state: MessageState
){
    enum class MessageState {
        SENT,
        DELIVERED,
        SEEN
    }
}