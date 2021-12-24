package com.example.androidteststudyguide.features.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidteststudyguide.databinding.FragmentCustomThemeBinding
import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper


class CustomThemeFragment: Fragment() {

    private lateinit var binding: FragmentCustomThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        val contextThemeWrapper: Context = ContextThemeWrapper(activity, com.example.androidteststudyguide.R.style.MyOrangeTheme)

        // clone the inflater using the ContextThemeWrapper
        val customInflater = inflater.cloneInContext(contextThemeWrapper)

        binding = FragmentCustomThemeBinding.inflate(customInflater, container, false)
        return binding.root
    }

}