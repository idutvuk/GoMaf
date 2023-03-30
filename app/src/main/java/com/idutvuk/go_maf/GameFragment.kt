package com.idutvuk.go_maf

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.kmafia.databinding.FragmentGameBinding
import kotlin.math.cos
import kotlin.math.sin


class GameFragment : Fragment() {
    private val numPlayers = Game.numPlayers
    var logMessage = ""

    //declared here bcs I have to use them outside the OnCreate method.
    private var pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
    private var players: Array<L_Player> = Array(numPlayers) { L_Player() }
    private var playerButtons: ArrayList<View> = ArrayList() //TODO: replace
    private var curBtn = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val b = FragmentGameBinding.inflate(layoutInflater)
        Log.d("GraphLog","view created")

        b.btnDState.setOnClickListener { Game.getState() }

        val containerRadius = (Resources.getSystem().getDisplayMetrics().heightPixels * 0.8 / 2).toInt()
        val tableRadius = ((containerRadius - dpToPx(70,requireContext())) / 2).toInt()

        Log.d("GraphLog","tableR: $tableRadius, containerR: $containerRadius")
        val points = generatePivotPoints(
            numPlayers,
            containerRadius,
            tableRadius,
            30
        )

        for (i in 0 until numPlayers) {
            val cardView = CardView(requireContext())
            logMessage += "Player CV $i created. x = ${points[i][0]}, y = ${points[i][1]}\n"
            cardView.x = points[i][0].toFloat()
            cardView.y = points[i][1].toFloat()


            b.root.addView(cardView)
        }
        Log.d("GraphLog",logMessage); logMessage=""


        Game.startGame()
        return b.root
    }

    override fun onDestroyView() {
        Game.endGame()
        super.onDestroyView()
    }

    private fun generatePivotPoints(
        numPlayers: Int,
        containerRadius: Int,
        tableRadius: Int,
        offsetDp: Int,
    ): Array<IntArray> {
        val pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
        val radius = (tableRadius + offsetDp).toFloat()

        val angleOffset = Math.toRadians(60.0)
        for (i in 0 until numPlayers) {
            val angle =
                ((2 * Math.PI - angleOffset) / (numPlayers - 1) * i + angleOffset / 2).toFloat()
            val x = (radius * sin(angle.toDouble())).toInt() + containerRadius
            val y = (radius * cos(angle.toDouble())).toInt() + containerRadius
            pivotPoints[i] = intArrayOf(x, y)
        }
        return pivotPoints
    }

    fun dpToPx(dp: Int, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
    }

}