package com.example.androidmaterialdesign.ui.notes.recycler

import com.example.androidmaterialdesign.model.Notes

interface OnListItemClickListener {
    fun onItemClick(data: Notes)
}