package app.retvens.rown.ApiRequest



import androidx.room.*
import app.retvens.rown.DataCollections.MessageEntity
import com.mesibo.api.MesiboMessage

@Dao
interface MessageDao {
    @Query("SELECT * FROM chats WHERE (sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender) AND sender = :specifiedSender ORDER BY timestamp ASC")
    fun getMessages(sender: String, receiver: String, specifiedSender: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(chatMessage: MessageEntity)

    @Delete
    fun deleteMessage(chatMessage: MessageEntity)


    @Query("SELECT * FROM chats WHERE receiver = :receiverId ORDER BY timestamp ASC")
    fun getReceiverMessages(receiverId: String): List<MessageEntity>

    @Query("SELECT DISTINCT receiver FROM chats")
    fun getDistinctReceivers(): List<String>

    @Query("SELECT id, sender, receiver, message, timestamp, state FROM chats WHERE (sender, receiver, timestamp) IN (SELECT sender, receiver, MAX(timestamp) FROM chats GROUP BY sender, receiver) ORDER BY timestamp DESC")
    fun getDistinctReceiversWithLatestMessage(): List<MessageEntity>

    @Query("UPDATE chats SET state = :state WHERE id = :messageId")
    fun updateMessageState(messageId: Long, state: MessageEntity.MessageState)
}