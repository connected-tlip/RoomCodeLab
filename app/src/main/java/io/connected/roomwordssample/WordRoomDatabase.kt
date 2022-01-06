package io.connected.roomwordssample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 2, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        private val ioScope = CoroutineScope(Dispatchers.IO)
        private lateinit var INSTANCE: WordRoomDatabase

        fun getDatabase(context: Context): WordRoomDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomDatabaseCallback)
                    .build()
            }
            return INSTANCE
        }

        private val roomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                val dao = INSTANCE.wordDao()

                ioScope.launch {
                    if (dao.getAnyWord().isEmpty()) {
                        for (element in DEFAULT_WORDS) {
                            val word = Word(element)
                            dao.insert(word)
                        }
                    }
                }
            }
        }

        private val DEFAULT_WORDS = arrayOf("dolphin", "crocodile", "cobra")
    }
}