package com.idutvuk.go_maf

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.kmafia.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}