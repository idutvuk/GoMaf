package com.idutvuk.go_maf.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.RecyclerViewLogAdapter


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var b: FragmentGameBinding
    private lateinit var buttons: List<MaterialButton>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        b = FragmentGameBinding.inflate(inflater, container, false)

        buttons = listOf(
            b.btn1, b.btn2, b.btn3, b.btn4, b.btn5, b.btn6,
            b.btn7, b.btn8, b.btn9, b.btn10, b.btn11, b.btn12
        )

        val messages = GameMessage.getGameActionsList()
        val adapter = RecyclerViewLogAdapter(messages)

        viewModel.initViews(b, Game.numPlayers, buttons, adapter)



        viewModel.ldNumber.observe(viewLifecycleOwner) {
            b.tvTopBar.text = "current: " + viewModel.ldNumber.value.toString()
            Log.d("GameLog", "observed")
        }


        b.rvLog.adapter = adapter
        b.rvLog.layoutManager = LinearLayoutManager(context)


        //TODO: implement binding interactions here instead of ViewModel
        return b.root
    }

}