package com.idutvuk.go_maf

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idutvuk.go_maf.databinding.FragmentRulesBinding

class RulesFragment : Fragment() {

    companion object {
        fun newInstance() = RulesFragment()

        var string = "<h2>Sdoba</h2><br><p>revolver</p><br>"

    }

    private lateinit var viewModel: RulesViewModel
    lateinit var b: FragmentRulesBinding
            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        b = FragmentRulesBinding.inflate(inflater,container,false)
        b.tvHtmlViewer.text = Html.fromHtml(string,Html.FROM_HTML_MODE_COMPACT)
        return b.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[RulesViewModel::class.java]
        // TODO: Use the ViewModel
    }

}