package app.retvens.rown.ApiRequest

import android.content.Context
import android.os.Message
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.retvens.rown.DataCollections.MessageEntity

@Database(entities = [MessageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): MessageDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Retvens.db"
                ).addMigrations(MIGRATION_1_2)
                    .build().also {
                    instance = it
                }
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sender TEXT NOT NULL, recipient TEXT NOT NULL, message TEXT NOT NULL, timestamp INTEGER NOT NULL)")
                database.execSQL("ALTER TABLE messages ADD COLUMN is_sent INTEGER NOT NULL DEFAULT 1")
            }
        }
    }
}