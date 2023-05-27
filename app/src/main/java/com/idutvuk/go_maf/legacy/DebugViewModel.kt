package com.idutvuk.go_maf.legacy

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.idutvuk.go_maf.databinding.FragmentDebugBinding

class DebugViewModel : ViewModel() {
    private lateinit var messages: ArrayList<GameMessage>
    fun initViews(
        b: FragmentDebugBinding,
        context: Context //TODO: remake the ViewModel so it wouldn't use context
    ) {

        // Initialize contacts
//        messages = GameMessage.getGameActionsList()
        // Create adapter passing in the sample user data
        val adapter = RecyclerViewLogAdapter(messages)
        // Attach the adapter to the recyclerview to populate items
        b.rvLog.adapter = adapter
        // Set layout manager to position the items
        b.rvLog.layoutManager = LinearLayoutManager(context)





    }


}