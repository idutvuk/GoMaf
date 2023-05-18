package com.idutvuk.go_maf.ui.game

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.CmdManager
import com.idutvuk.go_maf.model.GameMessage
import org.w3c.dom.Text
import java.lang.IllegalArgumentException

class RecyclerViewLogAdapter(private var dataList: ArrayList<GameMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RegularViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvHeading)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val dialog: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(itemView.context)
    }

    class ImportantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvDivider)
        val dialog: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(itemView.context)
    }



    override fun getItemViewType(position: Int): Int {
        return when(dataList[position].importance) {
            EventImportance.SILENT -> TYPE_SILENT
            EventImportance.REGULAR -> TYPE_REGULAR
            EventImportance.IMPORTANT -> TYPE_IMPORTANT
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val holder = when(viewType) {
            TYPE_SILENT -> throw IllegalArgumentException("Silent actions must not show up")

            TYPE_REGULAR -> {
                RegularViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.game_action_message, parent, false)
                )
            }

            TYPE_IMPORTANT -> {
                ImportantViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.game_action_important_message, parent, false)
                )
            }

            else -> throw IllegalArgumentException("Invalid type")
        }
        return holder
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val message = dataList[position]
        when(holder.itemViewType) {
            TYPE_REGULAR -> {
                val regularHolder = holder as RegularViewHolder
                regularHolder.textView.text = message.heading
                regularHolder.cardView.setOnClickListener{
                    regularHolder.dialog.show()
                }
                regularHolder.dialog
                    .setTitle(message.heading)
                    .setMessage(message.description)
                    .setPositiveButton("OK") {_,_ -> }
            }

            TYPE_IMPORTANT -> {
                val importantHolder = holder as ImportantViewHolder
                importantHolder.textView.text = message.heading
                importantHolder.itemView.setOnClickListener{
                    importantHolder.dialog.show()
                }
                importantHolder.dialog
                    .setTitle(message.heading)
                    .setMessage(message.description)
                    .setPositiveButton("OK") {_,_ -> }
            }

            TYPE_SILENT -> {}
        }
    }

    fun updateMessagesList() {
        val state = CmdManager.stateHistory[CmdManager.stateHistory.size - 1]
        if (state.mainBtnState.importance == EventImportance.SILENT) return

        dataList.add(0,
            GameMessage(
                state.mainBtnState.description,
                state.toString(),
                state.mainBtnState.importance
            )
        )
        this.notifyItemInserted(0)
    }

    override fun getItemCount() = dataList.size-1

    companion object {
        private const val TYPE_SILENT = 0
        private const val TYPE_REGULAR = 1
        private const val TYPE_IMPORTANT = 2
    }
}