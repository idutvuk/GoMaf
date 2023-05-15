package com.idutvuk.go_maf.ui.game

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.GameMessage

class RecyclerViewLogAdapter(private var dataList: ArrayList<GameMessage>) :
    RecyclerView.Adapter<RecyclerViewLogAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headingTextView: TextView = itemView.findViewById(R.id.tv_heading)
        val cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
        lateinit var materialDialog: MaterialAlertDialogBuilder
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.game_action_message, parent, false))

        viewHolder.materialDialog = MaterialAlertDialogBuilder(parent.context)
        viewHolder.cardView.setOnClickListener {
            viewHolder.materialDialog.show()
        }

        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = dataList[position]

        holder.headingTextView.text = message.heading

        holder.materialDialog
            .setTitle(message.heading)
            .setMessage(message.description)
            .setPositiveButton("OK") {_,_ -> }
//            .setNeutralButton("More") {_,_ -> }

    }

    fun updateMessagesList() {
        dataList = ArrayList()
        for (i in CmdManager.stateHistory.size-2 downTo 0) {
            val state = CmdManager.stateHistory[i]
            if (state.mainBtnState.eventType != EventImportance.SILENT)  dataList.add(GameMessage(state.mainBtnState.description, state.toString()))
        }
//        this.notifyItemInserted(0)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = dataList.size-1

}