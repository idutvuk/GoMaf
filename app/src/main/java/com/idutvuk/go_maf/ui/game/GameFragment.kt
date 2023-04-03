package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.RecyclerViewLogAdapter


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)

        viewModel.initViews(b,requireContext(), Game.numPlayers)

        //TODO: implement binding interactions here instead of ViewModel


        return b.root
    }

}