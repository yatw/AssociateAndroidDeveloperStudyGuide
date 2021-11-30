package com.example.androidteststudyguide.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidteststudyguide.databinding.FragmentHomeBinding
import com.example.androidteststudyguide.features.core.CoreFragment
import com.example.androidteststudyguide.features.ui.UIFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager2

        val adapter = ExploreViewPagerAdapter(this)
        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = adapter.tabTitles[position]
        }.attach()
        viewPager2.isUserInputEnabled = false // disable tab swipe
    }

    //inner = Non Static Nested classes
    inner class ExploreViewPagerAdapter(fp: Fragment) : FragmentStateAdapter(fp){

        val tabTitles = arrayOf("Android Core", "User Interface", "Data management")

        override fun getItemCount(): Int {
            return tabTitles.size
        }

        override fun createFragment(slideIndex: Int): Fragment {
            return when (slideIndex){
                0 -> CoreFragment()
                1 -> UIFragment()
                else -> CoreFragment()
            }
        }
    }

}