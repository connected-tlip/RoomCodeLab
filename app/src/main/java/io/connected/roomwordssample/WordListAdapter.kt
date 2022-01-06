package io.connected.roomwordssample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


class WordListAdapter(context: Context, private val clickListener: ClickListener) :
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    interface ClickListener {
        fun onWordClicked(word: Word)
    }

    private val inflater = LayoutInflater.from(context)

    var words: List<Word>? = null
        set(value) {
            DiffUtil.calculateDiff(DiffCallback(words ?: emptyList(), value ?: emptyList()))
                .dispatchUpdatesTo(this)
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView: View = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        if (words != null) {
            val current = words!![position]
            holder.wordItemView.text = current.word
            holder.wordItemView.setOnClickListener { clickListener.onWordClicked(current) }
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.text = "No Word"
        }
    }

    operator fun get(position: Int) = words?.get(position)

    override fun getItemCount(): Int {
        return words?.size ?: 0
    }

    private class DiffCallback(private val oldList: List<Word>, private val newList: List<Word>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].word == newList[newItemPosition].word
        }
    }

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
    }
}