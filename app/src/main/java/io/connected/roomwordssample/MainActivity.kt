package io.connected.roomwordssample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.connected.roomwordssample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var wordViewModel: WordViewModel

    private val newWordActivityLauncher = registerForActivityResult(
        NewWordActivity.NewWordActivityResultContract,
        newWordActivityResultCallback()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setTitle(R.string.word_list)

        val adapter = WordListAdapter(this, wordClickedListener())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        deleteOnSwipeItemTouchHelper(adapter).attachToRecyclerView(binding.recyclerView)

        binding.fab.setOnClickListener { newWordActivityLauncher.launch(null) }

        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]
        wordViewModel.allWords.observe(this) { adapter.words = it }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.clear_data -> {
            Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show();
            wordViewModel.deleteAll();
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun wordClickedListener() = object : WordListAdapter.ClickListener {
        override fun onWordClicked(word: Word) {
            newWordActivityLauncher.launch(word)
        }
    }

    private fun newWordActivityResultCallback() =
        ActivityResultCallback<Word?> { word ->
            when {
                word == null ->
                    Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG)
                        .show()
                word.id == 0 -> wordViewModel.insert(word)
                else -> wordViewModel.update(word)
            }
        }

    private fun deleteOnSwipeItemTouchHelper(adapter: WordListAdapter) =
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.words?.get(viewHolder.adapterPosition)?.let { word ->
                    Toast.makeText(this@MainActivity, "Deleting $word", Toast.LENGTH_LONG).show();
                    wordViewModel.deleteWord(word)
                }
            }
        })
}