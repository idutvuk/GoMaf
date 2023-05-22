package com.idutvuk.go_maf.ui.gameslist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.imageview.ShapeableImageView
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.databinding.GameCardBinding
import com.idutvuk.go_maf.ui.gameslist.placeholder.GamesContent
import java.util.Random

/**
 * [RecyclerView.Adapter] that can display a [GamesContent.GameItem].
 */
class GamesRecyclerViewAdapter(
    private val values: List<GamesContent.GameItem>,
) : RecyclerView.Adapter<GamesRecyclerViewAdapter.ViewHolder>() {
    private val rand = Random()

    fun getRandomImageId(isRed: Boolean?, context: Context): Int {
        var imgName: String
        val capCount: Int = when(isRed) {
            true-> {
                imgName = "red"
                RED_IMAGES_COUNT
            }
            false-> {
                imgName = "black"
                BLACK_IMAGES_COUNT
            }
            null->{
                imgName = "unknown"
                UNKNOWN_IMAGES_COUNT
            }
        }
        imgName += rand.nextInt(capCount)+1
        return context.resources.getIdentifier(imgName, "drawable", context.packageName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            GameCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.apply {
            image.setImageResource(getRandomImageId(item.isWinnerRed, image.context))

            idView.text = item.id.toString()
            date.text = item.date.toString()
            time.text = item.time.toString()
            duration.text = item.duration.toString()
            number.text = item.numPlayers.toString()
            chip.text = when (item.isWinnerRed) {
                true, false -> "Finished"
                null -> "Active"
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: GameCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.tvGameNumber
        val date: TextView = binding.tvGameDate
        val time: TextView = binding.tvGameTime
        val duration: TextView = binding.tvGameDuration
        val number: TextView = binding.tvGamePlayerNumber
        val chip: Chip = binding.chipGameStatus
        val image: ShapeableImageView = binding.ivPfp
    }

    companion object {
        const val RED_IMAGES_COUNT = 4
        const val BLACK_IMAGES_COUNT = 8
        const val UNKNOWN_IMAGES_COUNT = 1
    }
}