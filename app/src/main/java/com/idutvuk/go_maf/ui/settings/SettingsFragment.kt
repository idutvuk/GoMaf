package com.idutvuk.go_maf.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.idutvuk.go_maf.databinding.FragmentSettingsBinding
import com.idutvuk.go_maf.ui.mainmenu.MainMenuFragmentViewModel

internal class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsFragmentViewModel
    private lateinit var b: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SettingsFragmentViewModel::class.java]
        b = FragmentSettingsBinding.inflate(inflater,container,false)
       viewModel.initViews(b)


        return b.root
    }
}