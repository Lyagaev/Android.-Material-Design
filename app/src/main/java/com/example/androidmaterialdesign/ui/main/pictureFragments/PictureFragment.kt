package com.example.androidmaterialdesign.ui.main.pictureFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.FragmentPictureBinding

class PictureFragment : Fragment() {

    private var url = ""
    private lateinit var binding: FragmentPictureBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (url.isNullOrEmpty()) {
        } else {
            binding.imageView.load(url) {
                lifecycle(this@PictureFragment)
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("URL")?.let {
            url = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String?) = PictureFragment().apply {
            arguments = Bundle().apply {
                putString("URL", url)
            }
        }
    }
}
