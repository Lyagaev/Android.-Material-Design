package com.example.androidmaterialdesign.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.androidmaterialdesign.MainActivity
import com.example.androidmaterialdesign.R
import com.example.androidmaterialdesign.databinding.MainFragmentBinding
import com.example.androidmaterialdesign.model.PODServerResponseData
import com.example.androidmaterialdesign.model.PictureOfTheDayData
import com.example.androidmaterialdesign.ui.main.pictureFragments.PictureFragment
import com.example.androidmaterialdesign.ui.main.pictureFragments.ViewPagerAdapter
import com.example.androidmaterialdesign.ui.settings.SettingsFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import java.util.*


class MainFragment : Fragment() {

    //Ленивая инициализация модели
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private lateinit var binding: MainFragmentBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheetBehaviorText: TextView
    private lateinit var bottomSheetBehaviorTitle: TextView

    private lateinit var serverResponseData: List<PODServerResponseData>
    private var idxList: Int=0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view)
        setBottomAppBar(view)
        //setTextChip()
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData(viewModel.getDaysAgo(-2), viewModel.getDaysAgo(0))
            .observe(this@MainFragment, Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                serverResponseData = data.serverResponseData

                if (serverResponseData.size > idxList) {
                    setViewPager()
                }
            }
            is PictureOfTheDayData.Loading -> {
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
                toast(data.error.message)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> toast("Favourite")
            R.id.app_bar_search -> toast("Search")
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
            R.id.app_bar_settings -> {
                activity?.supportFragmentManager?.apply {
                    beginTransaction()
                            .add(R.id.container, SettingsFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                        ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setBottomSheetBehavior(view: View) {
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet_container)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        bottomSheetBehaviorText = view.findViewById(R.id.bottom_sheet_description)
        bottomSheetBehaviorTitle = view.findViewById(R.id.bottom_sheet_description_header)
    }

    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }

    private fun setViewPager(){
        val fragmentList: MutableList<Fragment> = mutableListOf()
        //хаполняем список фрагментов передав в него обьект
        serverResponseData.forEach{
            val fragment = PictureFragment.newInstance(it.url)
            fragmentList.add(fragment)
        }
        //переворачиваем список что бы 1 элемент был сегодняшний день
        fragmentList.reverse()
        //Добавляем адаптер
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, fragmentList)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        //Определяем первональную позицию
        binding.viewPager.currentItem = idxList
        //Добавлчям листенер на свайп ViewPager
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                idxList = position
                bottomSheetBehaviorText.text = serverResponseData[idxList].explanation
                bottomSheetBehaviorTitle.text = serverResponseData[idxList].title
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })

        bottomSheetBehaviorText.text = serverResponseData[idxList].explanation
        bottomSheetBehaviorTitle.text = serverResponseData[idxList].title
    }

    companion object {
        fun newInstance() = MainFragment()
        private var isMain = true
    }
}




