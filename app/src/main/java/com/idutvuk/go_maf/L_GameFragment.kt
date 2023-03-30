package com.idutvuk.go_maf
/*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.kmafia.R
import com.example.kmafia.databinding.FragmentGameBinding
import java.lang.IllegalStateException
import kotlin.math.cos
import kotlin.math.sin


class GameFragmentOld : Fragment() {
    private val selectionTimeMillis = 3000

    // Define the number of player circles and the radius of the table
    private val numPlayers = 10
    private val underTableRadiusDp = 180
    private val tableRadiusDp = 130

    //declared here bcs I have to use them outside the OnCreate method.
    private var pivotPoints: Array<IntArray> = Array(numPlayers) {IntArray(2)}
    private var players: Array<Player> = Array(numPlayers) {Player()}
    private var playerButtons: ArrayList<View> = ArrayList() //TODO: replace
    private var curBtn = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameBinding.inflate(layoutInflater)
        val game = Game()
        //TODO: replace table creating with the other class (@antoxa)

//        for (i in 0 until numPlayers) {
//            (playerButtons[i].parent as ViewGroup).removeView(playerButtons[i])
//        }
        // Generate the pivot points for the player circles
        pivotPoints = generatePivotPoints(
            numPlayers,
            tableRadiusDp,
            15
        )
        Log.d("ViewLog", "pivotPoints created")
        /**
         * Очень криво, но тут отображается последняя кнопка,
         * которую нажал чел. Нужно для нажатия сразу нескольких кнопок
         * AS зачем-то преобразовал стринг в это. Ну и ладно, всё равно костыль
         * none-kill-foul-vote
         */


        // Add the table layout to the table container
        val underTableLayout = createTableLayout(underTableRadiusDp, 10)

        binding.tableContainer.setOnClickListener {
            Log.d("", "test")
        }
        val tableLayout = createTableLayout(tableRadiusDp, 50)
        binding.tableContainer.addView(tableLayout)

        // Add the player circles to the table layout
        for (i in 0 until numPlayers) {
            val playerCircle = layoutInflater.inflate(R.layout.player_circle, binding.tableContainer)
            playerCircle.id = i + 1 // set the ID to the player number
            val playerNumberText = playerCircle.findViewById<TextView>(R.id.player_number_text)
            playerNumberText.text = (i + 1).toString()
            val pivotPoint = pivotPoints[i]
            players[i] = Player(i)
            playerButtons.add(
                addViewToTableLayout(underTableLayout, playerCircle, pivotPoint[0], pivotPoint[1]))

            playerButtons[i].setOnClickListener {

                when (curBtn) {
                    "kill" -> {
                        clearPlayerButtonsAnimations(playerButtons)
                        movePlayerCircle(i, true)
                        players[i].kill(playerButtons[i])
                        curBtn = "none"
                    }

                    "foul" -> {
                        players[i].foul()
                        curBtn = "none"
                    }

                    "vote" -> //                            game.addToVoteList(finalI);
                        curBtn = "none"

                    "none" -> {}

                    else -> {
                        Log.e("GameLog", "Unexpected value: " + curBtn[0])
                        throw IllegalStateException("Unexpected value: " + curBtn[0])
                    }
                }
            }

        }
        
        binding.btnVote.setOnClickListener { curBtn = "vote" }
        binding.btnKill.setOnClickListener(View.OnClickListener {
            if (curBtn == "kill") {
                clearPlayerButtonsAnimations(playerButtons)
                curBtn = "none"
                return@OnClickListener
            }
            curBtn = "kill"
            for (i in 0 until numPlayers) {
                playerButtons[i]!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.shake
                    )
                )
            }
            Handler().postDelayed({
                clearPlayerButtonsAnimations(playerButtons)
                curBtn = "none"
            }, selectionTimeMillis.toLong()) // 2 seconds delay before clearing animation
        })
        binding.btnFoul.setOnClickListener { curBtn = "foul" }
        binding.btnDUniversal.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "sdoba",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.btnDGenerate.setOnClickListener {
            for (i in 0 until numPlayers) {
                if (i == 0) players[i] = Player(i, "DON")
                if (i == 1 || i == 2) players[i] = Player(i, "MAF")
                if (i == 3) players[i] = Player(i, "SHR")
                if (i >= 4) players[i] = Player(i, "CIV")
            }
            Log.d("GameLog", "Default player setup generated.")
        }
        binding.btnDState.setOnClickListener {
            var msg = ""
            for (i in 0 until numPlayers) {
                msg += """
                    ${players[i].toString()}
                    
                    """.trimIndent()
            }
            Log.d("GameLog", "Player state:\n$msg")
            Log.d(
                "GameLog",
                "Game state:\n" //                        "+Vote list: "+game.getCurrentVoteList()
            )
        }
        return binding.root
    }

    /*
    ================================================================================================
    ================================================================================================
    ================================================================================================
     */

    private fun movePlayerCircle(i: Int, hide: Boolean) {
        if (players[i].isAlive != hide) return
        val newPivot = generatePivotPoints(
            numPlayers,
            tableRadiusDp, if (hide) -53 else +53
        )[i]
        val oldPivot = pivotPoints!![i]
        val deltaX = newPivot[0] - oldPivot[0]
        val deltaY = newPivot[1] - oldPivot[1]
        val animation = TranslateAnimation(
            0F, deltaX.toFloat(),
            0F, deltaY.toFloat()
        )
        animation.duration = 300
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val params = playerButtons[i].layoutParams as FrameLayout.LayoutParams
                params.leftMargin += deltaX
                params.topMargin += deltaY
                playerButtons[i].layoutParams = params
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        playerButtons[i].startAnimation(animation)
    }

    private fun clearPlayerButtonsAnimations(playerButtons: ArrayList<View>) {
        for (i in 0 until numPlayers) {
            playerButtons[i].clearAnimation()
        }
    }

    //TODO: Replace the following function with simple xml inflater (also create the xml file)
    private fun createTableLayout(radiusDp: Int, elevation: Int): CardView {
        val radiusPx = dp2px(radiusDp)
        val tableLayout = CardView(requireContext())

        // Set the layout params with width and height set to the radiusPx * 2
        val layoutParams = ConstraintLayout.LayoutParams(radiusPx * 2, radiusPx * 2)

        // Set the horizontal and vertical constraints to center the CardView
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        tableLayout.layoutParams = layoutParams
        tableLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
        tableLayout.cardElevation = elevation.toFloat()
        tableLayout.radius = radiusPx.toFloat()
        tableLayout.clipToOutline = true
        return tableLayout
    }

    private fun generatePivotPoints(
        numPlayers: Int,
        tableRadiusDp: Int,
        offsetDp: Int
    ): Array<IntArray> {
        val pivotPoints: Array<IntArray> = Array(numPlayers) { IntArray(2) }
        val centerX = underTableRadiusDp.toFloat()
        val centerY = underTableRadiusDp.toFloat()
        val radius = (tableRadiusDp + offsetDp).toFloat()

        /*
         * The following code don't work properly, it just leaves empty symmetrical space
         * between dots
         * But, who cares. The degrees below are completely not attached to the reality lol
         */
        val angleOffset = Math.toRadians(60.0)
        for (i in 0 until numPlayers) {
            val angle =
                ((2 * Math.PI - angleOffset) / (numPlayers - 1) * i + angleOffset / 2).toFloat()
            val x = (centerX - radius * sin(angle.toDouble()).toFloat()).toInt()
            val y = (centerY + radius * cos(angle.toDouble()).toFloat()).toInt()
            pivotPoints[i] = intArrayOf(x, y)
        }
        return pivotPoints
    }

    private fun addViewToTableLayout(tableLayout: CardView, view: View, x: Int, y: Int): View {
        val tableWidth = tableLayout.width
        val tableHeight = tableLayout.height

        // Calculate the left and top margins for the view
        val viewWidth = view.width
        val viewHeight = view.height
        //        int marginLeft = x - (viewWidth / 2); // it's just don't work
//        int marginTop = y - (viewHeight / 2); // it's just don't work
        var marginLeft = x - 24
        var marginTop = y - 24
        //TODO: Replace this shit

        // Convert the margins from dp to pixels
        marginLeft = dp2px(marginLeft)
        marginTop = dp2px(marginTop)

        // Set the layout parameters for the view
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        // Set the offset of the blocks
        layoutParams.leftMargin = marginLeft
        layoutParams.topMargin = marginTop
        view.layoutParams = layoutParams

        // Add the view to the table layout
        if (view.parent != null) {
            (view.parent as ViewGroup).removeView(view)
        }
        tableLayout.addView(view)
        return view
    }

    private fun dp2px(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

}
*/

