package com.mac.allomalar.ui.activities


import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout.make
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.snackbar.Snackbar.make
import com.mac.allomalar.adapters.PagerAdapterMap
import com.mac.allomalar.databinding.ActivityMapBinding
import com.mac.allomalar.db.database.AppDatabase
import com.mac.allomalar.ui.fragments.PageFirstFragment
import com.mac.allomalar.ui.fragments.PageSecondFragment
import com.mac.allomalar.ui.fragments.PageThirdFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val page1 = PageFirstFragment()
        val page2 = PageSecondFragment()
        val page3 = PageThirdFragment()
        val adapter = PagerAdapterMap(
            page1,
            page2,
            page3,
            3,
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        binding.vpMap.adapter = adapter
    }
}