package com.idutvuk.go_maf.ui.settings

import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.databinding.FragmentSettingsBinding

class SettingsFragmentViewModel : ViewModel() {
    fun initViews(
        b: FragmentSettingsBinding
    ) {
        b.tvComingSoon.text = "Coming soon"
    }

}