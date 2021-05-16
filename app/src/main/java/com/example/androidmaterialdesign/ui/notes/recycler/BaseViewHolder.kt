package com.example.androidmaterialdesign.ui.notes.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmaterialdesign.model.Notes

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem: Pair<Notes, Boolean>)
}
