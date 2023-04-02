package com.idutvuk.go_maf.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kmafia.databinding.FragmentSettingsBinding

internal class SettingsFragment : Fragment() {
    private lateinit var settingsFragmentViewModel: SettingsFragmentViewModel
    private lateinit var b: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSettingsBinding.inflate(inflater,container,false)
        settingsFragmentViewModel.initViews(b)


        return b.root
    }
}