package com.example.androidmaterialdesign.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.example.androidmaterialdesign.MainActivity
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.MainActivityBinding
import com.example.androidmaterialdesign.databinding.SettingsFragmentBinding
import com.example.androidmaterialdesign.util.getAppTheme
import com.example.androidmaterialdesign.util.setAppTheme

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val DEFAULT = R.style.Theme_AndroidMaterialDesign
    private val MY_THEME = R.style.Theme_AndroidMaterialDesign_MyTheme

    private var theme: Int=0
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel
    private var resThemeId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        theme = getAppTheme(requireContext())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewSetClickListener()

        when (theme) {
            DEFAULT ->
                binding.defaultTheme.isChecked=true
            MY_THEME ->
                binding.myTheme.isChecked=true
        }
    }

    private fun viewSetClickListener(){
        binding.defaultTheme.setOnClickListener{
            if (theme != DEFAULT) {
                setAppTheme(requireContext(), DEFAULT)
                val activityMain = activity as MainActivity
                activityMain.apply {
                    setAppTheme(DEFAULT)
                    recreate()
                }
            }
        }

        binding.myTheme.setOnClickListener {
            if (theme != MY_THEME) {
                setAppTheme(requireContext(), MY_THEME)

                val activityMain = activity as MainActivity
                activityMain.apply {
                    setAppTheme(MY_THEME)
                    recreate()
                }
            }
        }
    }
}