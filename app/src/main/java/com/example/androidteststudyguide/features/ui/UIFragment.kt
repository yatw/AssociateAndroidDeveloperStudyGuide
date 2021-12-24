package com.example.androidteststudyguide.features.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidteststudyguide.R
import com.example.androidteststudyguide.databinding.FragmentUiBinding
import timber.log.Timber

class UIFragment: Fragment() {

    private lateinit var binding: FragmentUiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCustomTheme.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_customThemeFragment)
        }

        binding.btnPager.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pager3Fragment)
        }

        binding.btnPagerAndRemote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pager3MediatorFragment)
        }

    }


}