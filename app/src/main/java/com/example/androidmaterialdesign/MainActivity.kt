package com.example.androidmaterialdesign

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.androidmaterialdesign.databinding.MainActivityBinding
import com.example.androidmaterialdesign.ui.main.BottomNavigationDrawerFragment
import com.example.androidmaterialdesign.ui.main.MainFragment
import com.example.androidmaterialdesign.ui.notes.NotesFragment
import com.example.androidmaterialdesign.ui.settings.SettingsFragment
import com.example.androidmaterialdesign.util.getAppTheme
import com.google.android.material.bottomappbar.BottomAppBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomAppBar()

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMain)
            menuInflater.inflate(R.menu.menu_bottom_bar, menu)
        else
            menuInflater.inflate(R.menu.menu_bottom_bar_other_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.app_bar_fav -> {
                replaceBottomAppBar()
                supportFragmentManager.apply {
                    beginTransaction()
                            .add(R.id.container, NotesFragment.newInstance())
                            .addToBackStack("NotesFragment")
                            .commitAllowingStateLoss()
                }
            }
            R.id.app_bar_settings -> {
                replaceBottomAppBar()
                supportFragmentManager.apply {
                    beginTransaction()
                            .add(R.id.container, SettingsFragment.newInstance())
                            .addToBackStack("SETTINGS_FRAGMENT")
                            .commitAllowingStateLoss()
                }
            }
            R.id.app_bar_search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
            android.R.id.home -> {
                BottomNavigationDrawerFragment()
                        .show(supportFragmentManager, "BottomNavigationDrawerFragment")
            }
        }
        return true
    }

    private fun setBottomAppBar() {
        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener {
            if (isMain) {
               /* isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)*/
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                        ContextCompat.getDrawable(this, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                onBackPressed()
            }
        }
    }

    private fun replaceBottomAppBar(){
        if (isMain) {
            isMain = false
            binding.bottomAppBar.navigationIcon = null
            binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back_fab))
            binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
        } else {
            isMain = true
            binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(this, R.drawable.ic_hamburger_menu_bottom_bar)
            binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            binding.fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_fab))
            binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
        }
    }

    companion object{
        private var isMain = true
    }
}


