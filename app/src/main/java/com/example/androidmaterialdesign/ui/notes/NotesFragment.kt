package com.example.androidmaterialdesign.ui.notes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.NotesFragmentBinding
import com.example.androidmaterialdesign.databinding.SettingsFragmentBinding
import com.example.androidmaterialdesign.model.Notes
import com.example.androidmaterialdesign.ui.notes.recycler.ItemTouchHelperCallback
import com.example.androidmaterialdesign.ui.notes.recycler.OnListItemClickListener
import com.example.androidmaterialdesign.ui.notes.recycler.RecyclerAdapter
import com.example.androidmaterialdesign.util.getAppTheme

class NotesFragment : Fragment() {

    private lateinit var binding: NotesFragmentBinding

    companion object {
        fun newInstance() = NotesFragment()
    }

    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = NotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)


        setRecyclerAdapter()
    }

    private fun setRecyclerAdapter() {
        val adapter = RecyclerAdapter(object : OnListItemClickListener {
            override fun onItemClick(data: Notes) {
            }
        })

        var myList: MutableList<Notes> = mutableListOf<Notes>()
        adapter.setNotes(myList)
        //доделать сохранение заметок в БД
        if (myList.size==0){
            myList.add(Notes(1,""))
        }

        binding.recyclerNotes.layoutManager = LinearLayoutManager(context)
        binding.recyclerNotes.adapter = adapter

        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.recyclerNotes)

        binding.nodeAdd.setOnClickListener {
            adapter.apply {
                addNote()
                notifyDataSetChanged()
            }
        }
    }

}

