package com.example.androidmaterialdesign.util

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

const val PREFERENCES_NAME = "SETTINGS"
const val APP_THEME_SELECTED = "APP_THEME_SELECTED"

/*
fun getThem(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var adultSettings = false
    if(sharedPref.contains(ADULT_SETTINGS)){
        adultSettings = sharedPref.getBoolean(ADULT_SETTINGS, false)
    }

    return adultSettings
}

fun getSettingsSharedPref(context: Context): SharedPreferences {
    return context.getSharedPreferences("settings", Context.MODE_PRIVATE)
}
*/

fun getAppTheme(context: Context): Int {
    val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getInt(APP_THEME_SELECTED, 0)
}

fun setAppTheme(context: Context, themeSaved: Int) {
    with(context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit()) {
        putInt(APP_THEME_SELECTED, themeSaved)
        apply()
    }
}