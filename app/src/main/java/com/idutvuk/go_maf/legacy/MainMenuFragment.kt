package com.idutvuk.go_maf.legacy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

import androidx.navigation.fragment.findNavController
import com.idutvuk.go_maf.R


import com.idutvuk.go_maf.databinding.FragmentMainMenuBinding

@Deprecated("Use compose")
class MainMenuFragment : Fragment() {
    private lateinit var b: FragmentMainMenuBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentMainMenuBinding.inflate(layoutInflater)
        navController = findNavController()
        b.btnNewGame.setOnClickListener {
            navController.navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }
        b.btnSavedGames.setOnClickListener {
            navController.navigate(R.id.action_mainMenuFragment_to_gamesViewerFragment)
        }
        b.btnSettings.setOnClickListener {
            navController.navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }

        b.btnRules.setOnClickListener {
            navController.navigate(R.id.action_mainMenuFragment_to_rulesFragment)
        }

        b.btnExitApp.setOnClickListener {
            TODO("Not yet implemented")
        }

        b.btnDPlayground.setOnClickListener {
            navController.navigate(R.id.action_mainMenuFragment_to_debugFragment)
        }

        return b.root
    }


}