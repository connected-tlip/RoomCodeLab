package io.connected.roomwordssample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val wordRepository = WordRepository(application)

    val allWords: LiveData<List<Word>>
        get() = wordRepository.allWords

    fun insert(word: Word) {
        wordRepository.insert(word)
    }

    fun update(word: Word) {
        wordRepository.update(word)
    }

    fun deleteWord(word: Word) {
        wordRepository.delete(word)
    }

    fun deleteAll() {
        wordRepository.deleteAll()
    }
}