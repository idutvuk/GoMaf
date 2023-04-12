package com.idutvuk.go_maf.ui.debug

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.FragmentDebugBinding
import com.idutvuk.go_maf.ui.TimerHandler

class DebugFragment : Fragment() {

    companion object {
        fun newInstance() = DebugFragment()
    }

    private lateinit var viewModel: DebugViewModel
    private lateinit var b: FragmentDebugBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewModel = ViewModelProvider(this)[DebugViewModel::class.java]
        b = FragmentDebugBinding.inflate(inflater,container,false)
        viewModel.initViews(b,requireContext())
//        TimerHandler.initialize(requireContext())

        b.fabPause.setOnClickListener {
            if(TimerHandler.isRunning) {
                TimerHandler.pauseTimer()
                b.fabPause.setImageResource(R.drawable.baseline_play_arrow_24)
            } else {
                TimerHandler.resumeTimer(b.tvTimer, b.pbTimer)
                b.fabPause.setImageResource(R.drawable.baseline_pause_24)
            }
        }
        //TODO: remove
        b.btnStart.setOnClickListener {
            TimerHandler.startTimer(b.tvTimer, b.pbTimer,60000 )
        }
        b.fabAdd.setOnClickListener{
            TimerHandler.addTime(b.tvTimer,b.pbTimer, 5000)
        }
        b.fabSkip.setOnClickListener {
            TimerHandler.skipTimer(b.tvTimer, b.pbTimer)
        }

        return b.root
    }


}