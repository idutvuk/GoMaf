package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.idutvuk.go_maf.databinding.FragmentGameBinding

import com.idutvuk.go_maf.model.Game


class GameFragment : Fragment() {

    private lateinit var gameFragmentViewModel: GameFragmentViewModel
    private lateinit var b: FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        gameFragmentViewModel = ViewModelProvider(this)[GameFragmentViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)

        gameFragmentViewModel.initViews(b, Game.numPlayers)




        // Implement other button click listeners here

        return b.root
    }

}