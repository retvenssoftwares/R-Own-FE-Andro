package app.retvens.rown.ApiRequest



import androidx.room.*
import app.retvens.rown.DataCollections.MessageEntity
import com.mesibo.api.MesiboMessage

@Dao
interface MessageDao {
    @Query("SELECT * FROM chats WHERE (sender = :sender AND receiver = :receiver) OR (sender = :receiver AND receiver = :sender) ORDER BY timestamp ASC")
    fun getMessages(sender: String, receiver: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(chatMessage: MessageEntity)

    @Delete
    fun deleteMessage(chatMessage: MessageEntity)


    @Query("SELECT * FROM chats WHERE receiver = :receiverId ORDER BY timestamp ASC")
    fun getReceiverMessages(receiverId: String): List<MessageEntity>
}