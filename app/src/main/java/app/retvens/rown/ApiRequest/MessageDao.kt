package app.retvens.rown.ApiRequest



import androidx.room.*
import app.retvens.rown.DataCollections.MessageEntity
import com.mesibo.api.MesiboMessage

@Dao
interface MessageDao {
    @Query("SELECT * FROM chats WHERE ((sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender)) ORDER BY timestamp ASC")
    fun getMessages(sender: String, receiver: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(chatMessage: MessageEntity)

    @Delete
    fun deleteMessage(chatMessage: MessageEntity)


    @Query("SELECT * FROM chats WHERE receiver = :receiverId ORDER BY timestamp ASC")
    fun getReceiverMessages(receiverId: String): List<MessageEntity>

    @Query("SELECT DISTINCT receiver FROM chats")
    fun getDistinctReceivers(): List<String>

//    @Query("SELECT id, sender, receiver, message, timestamp, state FROM chats WHERE (sender, receiver, timestamp) IN (SELECT sender, receiver, MAX(timestamp) FROM chats GROUP BY sender, receiver) ORDER BY timestamp DESC")
//    fun getDistinctReceiversWithLatestMessage(): List<MessageEntity>

    @Query("SELECT * FROM chats WHERE sender=:userId OR receiver=:userId GROUP BY CASE WHEN sender=:userId THEN receiver ELSE sender END ORDER BY timestamp DESC")
    fun getRecentChats(userId: String): List<MessageEntity>
}