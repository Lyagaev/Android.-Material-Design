package com.example.androidmaterialdesign.ui.notes.recycler

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.`interface`.ItemTouchHelperViewHolder
import com.example.androidmaterialdesign.databinding.NotesRecyclerItemBinding
import com.example.androidmaterialdesign.model.Notes
import java.util.*
import kotlin.collections.ArrayList


class RecyclerAdapter(private var onListItemClickListener: OnListItemClickListener?) :
RecyclerView.Adapter<RecyclerAdapter.MainViewHolder>(), ItemTouchHelperAdapter, Filterable {

    private lateinit var notesData: MutableList<Notes>
    private lateinit var notesDataFilter: MutableList<Notes>

    private var searchText: String=""

    fun setNotes(data: MutableList<Notes>) {
        notesData = data
        notesDataFilter = data

        notifyDataSetChanged()
    }

    fun removeListener() {
        onListItemClickListener = null
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.notes_recycler_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(notesDataFilter[position])
    }


    override fun getItemCount(): Int {
        return notesDataFilter.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        private var binding = NotesRecyclerItemBinding.bind(view)

        fun bind(notes: Notes) {
            itemView.apply {
                binding.noteDescription.setText(notes.someDescription)

                val spannableString = SpannableString(notes.someDescription)
                // передаём span
                binding.noteDescription.setText(spannableString, TextView.BufferType.SPANNABLE)
                // получаем текст и кастим его в span, потому что ранее передали туда span
                val spannableText = binding.noteDescription.text as Spannable

                //проверяем содержит ли строка текст
                if (spannableText.contains(searchText, true) && searchText.isNotEmpty()){
                    //поиск номера первого символа в строке
                    val startIdx: Int = spannableText.toString().toLowerCase().indexOf(searchText.toLowerCase())
                    val endIdx: Int = startIdx + searchText.length

                    spannableText.setSpan(
                            BackgroundColorSpan(Color.RED),
                            startIdx, endIdx,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }

                setOnClickListener {
                    onListItemClickListener?.onItemClick(notes)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        notesDataFilter.removeAt(fromPosition.apply {
            notesDataFilter.add(
                    if (toPosition > fromPosition) toPosition - 1 else toPosition,
                    notesDataFilter[fromPosition]
            )
        })
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notesDataFilter.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addNote() = notesDataFilter.add(Notes())

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                notesDataFilter = if (charSearch.isEmpty()) {
                    searchText=""
                    notesData
                } else {
                    val resultList = ArrayList<Notes>()
                    for (row in notesData) {
                        if (row.someDescription?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT)) == true) {
                            searchText=charSearch
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = notesDataFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notesDataFilter = results?.values as ArrayList<Notes>
                notifyDataSetChanged()
            }

        }
    }
}
