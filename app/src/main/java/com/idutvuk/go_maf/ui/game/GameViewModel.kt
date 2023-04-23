package com.idutvuk.go_maf.ui.game


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idutvuk.go_maf.databinding.FragmentGameBinding
import com.idutvuk.go_maf.model.gamedata.Game
import com.idutvuk.go_maf.model.GameMessage
import com.idutvuk.go_maf.model.gamedata.GameState


class GameViewModel : ViewModel() {

    private var logMsg = ""

    private lateinit var messages: ArrayList<GameMessage>
    val gameMessages = MutableLiveData<List<GameMessage>>()

    //Объявляю LiveData, содержащую в себе Int внутри ViewModel
    /**
     * for education purposes only!
     */
    val ldNumber = MutableLiveData<Int>(0) //TODO remove
    val gameState: GameState = GameState()

    fun initViews( //TODO: Destroy the initviews
        b: FragmentGameBinding,
    ) {





//        Меняем состояние значения livedata ldNumber по нажатию на кнопку
//        b.btnDTest.setOnClickListener {
//            ldNumber.value = ldNumber.value?.plus(1)
//            Log.d("GameLog", "test button activated")
//        }
    }

    private val blinkDur = 2_000
    fun foulTV(id: Int, b: FragmentGameBinding) {
        b.tvBig.text = Game.players[id].fouls.toString()
        b.tvUpper.text = "Player #${Game.players[id].strNum} fouls: "
        blink(blinkDur, b)
    }

    fun gameEndTV(b: FragmentGameBinding) {
        b.tvBig.text = ""
        b.tvUpper.text = "Game over"
        blink(blinkDur, b)
    }

    fun blink(dur: Int, b: FragmentGameBinding) {
        val appearDuration = dur / 4
        val disappearDuration = dur / 4
        b.tvBig.alpha = 0f
        b.tvBig.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.tvBig.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.tvBig.alpha = 0f
                            }
                        })
                }
            })

        b.tvUpper.alpha = 0f
        b.tvUpper.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    b.tvUpper.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                b.tvUpper.alpha = 0f
                            }
                        })
                }
            })


    }
    fun controlUndoRedo(arr: IntArray, b: FragmentGameBinding, adapter: RecyclerViewLogAdapter) {
        when (arr[0]) {
            -1 -> b.bottomSheetLayout.btnUndo.isEnabled = false
            1 -> b.bottomSheetLayout.btnUndo.isEnabled = true
            else -> {}
        }
        when (arr[1]) {
            -1 -> b.bottomSheetLayout.btnRedo.isEnabled = false
            1 -> b.bottomSheetLayout.btnRedo.isEnabled = true
            else -> {}
        }
        adapter.updateMessagesList() //TODO: УБРАТЬ GOVNOCODE
    }
}



