package com.idutvuk.go_maf.ui.debug

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.FragmentDebugBinding

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
        return b.root
    }


}