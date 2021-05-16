package com.example.androidmaterialdesign.ui.notes.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.model.Notes
import androidx.databinding.DataBindingUtil
import com.example.androidmaterialdesign.`interface`.ItemTouchHelperViewHolder
import com.example.androidmaterialdesign.databinding.NotesEmptyRecyclerItemBinding
import com.example.androidmaterialdesign.databinding.NotesRecyclerItemBinding


class RecyclerAdapter(private var onListItemClickListener: OnListItemClickListener?) :
RecyclerView.Adapter<RecyclerAdapter.MainViewHolder>(), ItemTouchHelperAdapter {

    private lateinit var notesData: MutableList<Notes>

    fun setNotes(data: MutableList<Notes>) {
        notesData = data
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
        holder.bind(notesData[position])
    }


    override fun getItemCount(): Int {
        return notesData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        private var binding = NotesRecyclerItemBinding.bind(view)

        fun bind(notes: Notes) {
            itemView.apply {
                binding.noteDescription.setText(notes.someDescription)

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
        notesData.removeAt(fromPosition.apply {
            notesData.add(
                    if (toPosition > fromPosition) toPosition - 1 else toPosition,
                    notesData[fromPosition]
            )
        })
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notesData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addNote() = notesData.add(Notes())
}
