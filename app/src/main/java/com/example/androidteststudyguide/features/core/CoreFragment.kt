package com.example.androidteststudyguide.features.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidteststudyguide.R
import com.example.androidteststudyguide.databinding.FragmentCoreBinding


class CoreFragment: Fragment() {

    private lateinit var binding: FragmentCoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSnackbar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_snackBarFragment)
        }

        binding.btnNotification.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
        }

        binding.btnWorkmanager.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_workManagerFragment)
        }

        binding.btnLocalize.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_localizationFragment)
        }



    }


}