package com.idutvuk.go_maf.ui.mainmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.kmafia.R

import com.example.kmafia.databinding.FragmentMainMenuBinding
import com.idutvuk.go_maf.ui.game.GameFragmentViewModel


class MainMenuFragment : Fragment() {
    private lateinit var viewModel: MainMenuFragmentViewModel
    private lateinit var b: FragmentMainMenuBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MainMenuFragmentViewModel::class.java]
        b = FragmentMainMenuBinding.inflate(layoutInflater)
        navController = findNavController()
        viewModel.initViews(b,navController)

        return b.root
    }


}