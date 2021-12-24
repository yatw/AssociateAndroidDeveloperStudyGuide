package com.example.androidteststudyguide.features.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidteststudyguide.databinding.FragmentLocalizationBinding


class LocalizationFragment: Fragment() {

    private lateinit var binding: FragmentLocalizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocalizationBinding.inflate(inflater, container, false)
        return binding.root
    }

}