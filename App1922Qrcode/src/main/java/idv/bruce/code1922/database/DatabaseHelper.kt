package idv.bruce.code1922.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import idv.bruce.code1922.structure.SavedData
import idv.bruce.code1922.structure.SmsData

@Database(entities = [(SmsData::class), (SavedData::class)], version = 1)
abstract class DatabaseHelper : RoomDatabase() {
    companion object {
        @Volatile
        private var instance : DatabaseHelper? = null

        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK) {
            instance ?: build(context).also { instance = it }
        }

        private fun build(context : Context) =
            Room.databaseBuilder(context, DatabaseHelper::class.java, "database.db").build()
    }

    abstract val smsDataDao : SmsDataDao

    abstract val savedDataDao : SavedDataDao
}