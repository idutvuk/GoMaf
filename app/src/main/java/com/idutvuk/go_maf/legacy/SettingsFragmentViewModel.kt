package com.idutvuk.go_maf.legacy

import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.databinding.FragmentSettingsBinding
@Deprecated("Use compose")
class SettingsFragmentViewModel : ViewModel() {
    fun initViews(
        b: FragmentSettingsBinding
    ) {
        b.tvComingSoon.text = "Coming soon"
    }

}