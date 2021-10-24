package com.example.androidteststudyguide.features.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidteststudyguide.databinding.FragmentSnackbarBinding
import com.google.android.material.snackbar.Snackbar


class SnackBarFragment: Fragment() {

    private lateinit var binding: FragmentSnackbarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSnackbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {

            Snackbar.make(
                binding.myCoordinatorLayout,
                "Lets go",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }
}