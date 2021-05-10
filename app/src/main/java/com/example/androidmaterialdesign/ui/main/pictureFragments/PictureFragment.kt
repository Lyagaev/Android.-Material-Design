package com.example.androidmaterialdesign.ui.main.pictureFragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.api.load
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.FragmentPictureBinding
import com.example.androidmaterialdesign.databinding.MainFragmentBinding
import com.example.androidmaterialdesign.model.PODServerResponseData


class PictureFragment : Fragment() {

    private var url = ""
    private lateinit var binding: FragmentPictureBinding
    private lateinit var bindingMainActivity: MainFragmentBinding
    private var isExpanded = false
    private var showDescription = false
    private val responseData: PODServerResponseData? by lazy { arguments?.getParcelable("dataKey") as PODServerResponseData? }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureBinding.inflate(inflater, container, false)
        bindingMainActivity = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (responseData?.url.isNullOrEmpty()) {
        } else {
            binding.imageView.load(responseData?.url) {
                lifecycle(this@PictureFragment)
                error(R.drawable.ic_load_error_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }
        }
        binding.description.text = responseData?.explanation ?: ""
        binding.title.text = responseData?.title ?: ""
        binding.date.text = responseData?.date ?: ""

        binding.imageView.setOnClickListener {
            isExpanded = !isExpanded
            //дописать увеличение
        }

        binding.tap.setOnClickListener {
            if (showDescription) hideComponents() else showComponents()
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("URL")?.let {
            url = it
        }
    }

    private fun showComponents() {
        showDescription = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_picture_end)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
        binding.tap.text = "Скрыть описание"
    }

    private fun hideComponents() {
        showDescription = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_picture)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)

        binding.tap.text = "Показать описание"
    }

    companion object {
        fun newInstance(responseData : PODServerResponseData?) = PictureFragment().apply {
            arguments = Bundle().apply {
                putParcelable("dataKey",responseData)
            }
        }
    }
}


