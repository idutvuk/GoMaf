package com.idutvuk.go_maf.ui.settings

import androidx.lifecycle.ViewModel
import com.example.kmafia.databinding.FragmentGameBinding
import com.example.kmafia.databinding.FragmentSettingsBinding

class SettingsFragmentViewModel : ViewModel() {
    fun initViews(
        b: FragmentSettingsBinding
    ) {
        b.tvComingSoon.text = "Coming soon"
    }

}