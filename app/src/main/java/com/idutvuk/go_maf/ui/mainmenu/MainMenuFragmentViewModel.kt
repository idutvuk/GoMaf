package com.idutvuk.go_maf.ui.mainmenu

import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.R

import androidx.navigation.NavController


import com.idutvuk.go_maf.databinding.FragmentMainMenuBinding

class MainMenuFragmentViewModel: ViewModel() {
    fun initViews(
        b: FragmentMainMenuBinding,
        n: NavController
    ) {
        b.btnNewGame.setOnClickListener {
            n.navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        b.btnSettings.setOnClickListener {
            n.navigate(R.id.action_mainMenuFragment_to_settingsFragment)
        }

        b.btnExitApp.setOnClickListener {
            TODO("Not yet implemented")
        }


    }
}