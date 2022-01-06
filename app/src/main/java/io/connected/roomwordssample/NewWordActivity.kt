package io.connected.roomwordssample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import io.connected.roomwordssample.databinding.ActivityNewWordBinding


class NewWordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewWordBinding

    private val updateWord: Word?
        get() = intent.getSerializableExtra(EXTRA_WORD_TO_UPDATE) as? Word


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(if (updateWord == null) R.string.new_word else R.string.update_word)

        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (binding.editWord.text.isEmpty()) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                val newWord = binding.editWord.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, updateWord?.copy(word = newWord) ?: Word(newWord))
                setResult(RESULT_OK, replyIntent)
            }
            finish()
        }

        updateWord?.let { binding.editWord.setText(it.word) }
    }

    object NewWordActivityResultContract : ActivityResultContract<Word?, Word?>() {
        override fun createIntent(context: Context, word: Word?): Intent {
            val intent = Intent(context, NewWordActivity::class.java)
            intent.putExtra(EXTRA_WORD_TO_UPDATE, word)
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Word? {
            return if (resultCode == RESULT_OK) {
                intent?.getSerializableExtra(EXTRA_REPLY) as Word
            } else {
                null
            }
        }
    }

    companion object {
        const val EXTRA_WORD_TO_UPDATE = "com.example.android.roomwordssample.WORD_TO_UPDATE"

        const val EXTRA_REPLY = "com.example.android.roomwordssample.REPLY"
    }
}