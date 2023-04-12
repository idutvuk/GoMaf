package com.idutvuk.go_maf.model

import android.app.Dialog
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.idutvuk.go_maf.R

class RecyclerViewLogAdapter(private var mMessages: List<GameMessage>) : RecyclerView.Adapter<RecyclerViewLogAdapter.ViewHolder>(){

    //TODO: Pin recycler to the bottom
            // Provide a direct reference to each of the views within a data item
            // Used to cache the views within the item layout for fast access
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val headingTextView: TextView = itemView.findViewById(R.id.tv_heading)
        val importanceTextView: TextView = itemView.findViewById(R.id.tv_d_importance)

//            val descriptionTextView: TextView = itemView.findViewById(R.id.tv_description)
            val cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.game_action_message, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = mMessages[position]
        holder.headingTextView.text = message.heading
        holder.importanceTextView.text = message.importance.toString()
//        holder.descriptionTextView.text = message.description
        if(position >= CmdManager.currentIndex) {
            Log.d("GraphLog","card with action $position disabled")
//            holder.cardView.background =
        //TODO: disable all the cards after $position
        } else {
            holder.cardView.setOnClickListener {
                // Launch dialog displaying message description
                val dialog = Dialog(holder.itemView.context)
                dialog.setContentView(R.layout.dialog_message_description)
                dialog.findViewById<TextView>(R.id.tv_description).text = message.description
                dialog.show()
            }
        }
    }
    fun updateMessagesList() {
        mMessages = GameMessage.getGameActionsList()
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mMessages.size
    }
}