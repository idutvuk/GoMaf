package com.idutvuk.go_maf

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idutvuk.go_maf.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {

    companion object {
        fun newInstance() = RulesFragment()
    }

    private lateinit var viewModel: RulesViewModel
    lateinit var b: FragmentRulesBinding
            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        b = FragmentRulesBinding.inflate(inflater,container,false)

        return b.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RulesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}