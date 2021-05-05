package com.example.androidmaterialdesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidmaterialdesign.ui.main.MainFragment
import com.example.androidmaterialdesign.util.getAppTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        var theme = getAppTheme(applicationContext)
        if (theme!=0){
          setTheme(theme)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    fun setAppTheme(theme: Int) {
        setTheme(theme)
    }
}


