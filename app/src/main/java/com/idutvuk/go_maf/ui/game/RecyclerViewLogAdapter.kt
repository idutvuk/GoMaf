package com.idutvuk.go_maf.ui.game

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.GameMessage

class RecyclerViewLogAdapter(private var mMessages: List<GameMessage>) :
    RecyclerView.Adapter<RecyclerViewLogAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headingTextView: TextView = itemView.findViewById(R.id.tv_heading)
        val cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.game_action_message, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = mMessages[position]

        holder.headingTextView.text = message.heading
//      holder.descriptionTextView.text = message.description

        holder.cardView.setOnClickListener {
            // Launch dialog displaying message description
            val dialog = Dialog(holder.itemView.context)
            dialog.setContentView(R.layout.dialog_message_description)
//            dialog.findViewById<TextView>(R.id.tv_description).text = message.description
            dialog.show()
        }
    }

    fun updateMessagesList() {
        mMessages = GameMessage.getGameActionsList()
        this.notifyItemInserted(mMessages.size - 1)
    }

    override fun getItemCount(): Int {
        return mMessages.size - 1
    }
}