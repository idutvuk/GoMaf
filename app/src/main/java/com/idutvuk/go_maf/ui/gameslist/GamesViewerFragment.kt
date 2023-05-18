package com.idutvuk.go_maf.ui.gameslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idutvuk.go_maf.databinding.FragmentGamesViewerListBinding
import com.idutvuk.go_maf.ui.gameslist.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class GamesViewerFragment : Fragment() {
    private lateinit var b: FragmentGamesViewerListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        b = FragmentGamesViewerListBinding.inflate(inflater,container,false)
            b.rvGames.layoutManager = LinearLayoutManager(context)
            b.rvGames.adapter = GamesRecyclerViewAdapter(PlaceholderContent.ITEMS)

        return b.root
    }
}