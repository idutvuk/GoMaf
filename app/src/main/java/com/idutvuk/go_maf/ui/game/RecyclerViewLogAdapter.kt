package com.idutvuk.go_maf.ui.game

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
import org.w3c.dom.Text
import java.lang.IllegalArgumentException

class RecyclerViewLogAdapter(private var dataList: ArrayList<GameMessage>) :
    RecyclerView.Adapter<RecyclerViewLogAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var materialDialog: MaterialAlertDialogBuilder
        lateinit var textView: TextView
        lateinit var cardView: MaterialCardView
        fun bind(eventImportance: EventImportance) {
            when (eventImportance) {
                EventImportance.SILENT -> TODO()
                EventImportance.REGULAR -> bindCard()
                EventImportance.IMPORTANT -> bindDivider()
            }
        }

        private fun bindDivider() {
            textView = itemView.findViewById(R.id.tvDivider)
        }

        private fun bindCard() {
            textView = itemView.findViewById(R.id.tv_heading)
            cardView = itemView.findViewById(R.id.card_view)
        }
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
    ): ViewHolder {
        val layout = when(viewType) {
            TYPE_SILENT -> throw IllegalArgumentException("Silent actions must not show up")
            TYPE_REGULAR -> R.layout.game_action_message
            TYPE_IMPORTANT -> R.layout.game_action_important_message
            else -> throw IllegalArgumentException("Invalid type")
        }

        val viewHolder = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(layout, parent, false))

        if (viewType == TYPE_REGULAR) {
            viewHolder.materialDialog = MaterialAlertDialogBuilder(parent.context)
            viewHolder.cardView.setOnClickListener {
                viewHolder.materialDialog.show()
            }
        }

        return viewHolder
    }


    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val message = dataList[position]

        holder.textView.text = message.heading

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
            when (state.mainBtnState.importance) {
                EventImportance.SILENT -> {}
                EventImportance.REGULAR -> {}
                EventImportance.IMPORTANT -> {}
                else -> {}
            }
            dataList.add(
                GameMessage(
                    state.mainBtnState.description,
                    state.toString(),
                    state.mainBtnState.importance
                )
            )
        }
//        this.notifyItemInserted(0)
        this.notifyDataSetChanged()
    }

    override fun getItemCount() = dataList.size-1

    companion object {
        private const val TYPE_SILENT = 0
        private const val TYPE_REGULAR = 1
        private const val TYPE_IMPORTANT = 2
    }
}