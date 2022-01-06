package io.connected.roomwordssample

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordRepository(application: Application) {

    private val wordDao: WordDao = WordRoomDatabase.getDatabase(application).wordDao()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    val allWords: LiveData<List<Word>>
        get() = wordDao.getAllWords()

    fun insert(word: Word) = ioScope.launch {
        wordDao.insert(word)
    }

    fun update(word: Word) = ioScope.launch {
        wordDao.update(word)
    }

    fun delete(word: Word) = ioScope.launch {
        wordDao.deleteWord(word)
    }

    fun deleteAll() = ioScope.launch {
        wordDao.deleteAll()
    }
}