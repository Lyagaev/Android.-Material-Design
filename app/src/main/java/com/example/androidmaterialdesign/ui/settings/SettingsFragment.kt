package com.example.androidmaterialdesign.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.SettingsFragmentBinding

const val SETTINGS_PREFERENCE = "SETTINGS_PREFERENCE"
const val NAME_THEME = "NAME_THEME"
const val DEFAULT = "DEFAULT"
const val MY_THEME = "MY_THEME"

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }


    private lateinit var themeName: String
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel
    private var resThemeId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        setSharedPreferenceSettings()
        val inflaterNewTheme = LayoutInflater.from(ContextThemeWrapper(context, resThemeId))
        binding = SettingsFragmentBinding.inflate(inflaterNewTheme, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        viewSetClickListener()
        // TODO: Use the ViewModel
    }

    private fun viewSetClickListener(){
        binding.defaultTheme.setOnClickListener{
            if (themeName != DEFAULT) {
                saveThemeSettings(DEFAULT, R.style.Theme_AndroidMaterialDesign)
                activity?.let {
                    Toast.makeText(it, "DEFAULT", Toast.LENGTH_SHORT).show()
                    it.recreate()
                }
            }
        }

        binding.myTheme.setOnClickListener {
            if (themeName != MY_THEME) {
                saveThemeSettings(MY_THEME, R.style.Theme_AndroidMaterialDesign_MyTheme)
                activity?.let {
                    Toast.makeText(it, "MY_THEME", Toast.LENGTH_SHORT).show()
                    it.recreate()
                }
            }
        }
    }

    private fun saveThemeSettings(themeName: String, id: Int) {
        this.themeName = themeName
        activity?.let {
            with(it.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE).edit()) {
                putString(NAME_THEME, themeName).apply()
            }
        }
    }

    private fun setSharedPreferenceSettings() {
        activity?.let {
            themeName = it.getSharedPreferences(SETTINGS_PREFERENCE, Context.MODE_PRIVATE).getString(NAME_THEME, DEFAULT).toString()
            when(themeName) {
                MY_THEME -> {
                    binding.myTheme.isChecked = true
                    resThemeId = R.style.Theme_AndroidMaterialDesign_MyTheme
                }
                else -> {
                    binding.defaultTheme.isChecked = true
                    resThemeId = R.style.Theme_AndroidMaterialDesign
                }
            }
        }
    }
}