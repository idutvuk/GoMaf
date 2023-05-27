package com.idutvuk.go_maf.legacy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.gamedata.MafiaGameState
import com.idutvuk.go_maf.ui.game.EventImportance
import com.idutvuk.go_maf.ui.game.MainBtnState
import java.lang.IllegalArgumentException
@Deprecated("Use compose")
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
        if (CmdManager.stateHistory.size < 2) return
        val state = CmdManager.stateHistory[CmdManager.stateHistory.size - 2]
        if (state.mainBtnState.importance == EventImportance.SILENT) return

        dataList.add(
            0,
            GameMessage(
                gameActionFormatter(state.mainBtnState.overwriteText ?: state.mainBtnState.description, state),
                state.toString(),
                state.mainBtnState.importance
            )
        )
        this.notifyItemInserted(0)
    }

    private fun gameActionFormatter(string: String, state: MafiaGameState): String {
        var s = string
        when(state.mainBtnState) {
            MainBtnState.START_DAY -> s += " ${(state.currentPhaseNumber)/2 + 1}"
            MainBtnState.START_NIGHT -> s += " ${(state.currentPhaseNumber)/2}"

            MainBtnState.START_SPEECH -> s = s.replace("#", state.cursor.toString())

            MainBtnState.MAFIA_KILL -> s =
                if (state.mafiaMissStreak == 0) s+ " ${(state.cursor)}"
                else "Misfire ( ${state.mafiaMissStreak}/3)"

            MainBtnState.CHECK_DON -> {}//TODO
            MainBtnState.CHECK_SHR -> {}//TODO

            MainBtnState.BEST_MOVE -> {}//TODO
            else -> {}
        }
        return s
    }

    override fun getItemCount() = dataList.size-1

    companion object {
        private const val TYPE_SILENT = 0
        private const val TYPE_REGULAR = 1
        private const val TYPE_IMPORTANT = 2
    }
}