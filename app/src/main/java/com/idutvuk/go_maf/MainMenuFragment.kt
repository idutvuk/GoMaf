package com.idutvuk.go_maf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kmafia.R
import com.example.kmafia.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainMenuBinding.inflate(layoutInflater)

        binding.btnNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }
        return binding.root
    }


}