package com.idutvuk.go_maf.ui.rules

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.idutvuk.go_maf.R

class RulesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_rules, container, false)
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.loadUrl("file:///android_asset/index.html")
        return view
    }
}