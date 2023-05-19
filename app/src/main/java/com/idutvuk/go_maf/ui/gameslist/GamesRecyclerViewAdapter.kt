package com.idutvuk.go_maf.ui.gameslist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.idutvuk.go_maf.R

import com.idutvuk.go_maf.ui.gameslist.placeholder.PlaceholderContent.PlaceholderItem
import com.idutvuk.go_maf.databinding.GameCardBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GamesRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
) : RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            GameCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: GameCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.tvGameNumber
        val contentView: TextView = binding.tvGameDate

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}