package com.idutvuk.go_maf

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.widget.TextView

object SmartTV {
    lateinit var tvPrimary: TextView
    lateinit var tvSecondary: TextView

    private const val blinkDur = 2_000

    fun foulTV(id:Int) {
        tvPrimary.text = Game.players[id].fouls.toString()
        tvSecondary.text = "Player #${Game.players[id].strNum} fouls: "
        blink(blinkDur)
    }

    fun gameEndTV() {
        tvPrimary.text = ""
        tvSecondary.text = "Game over"
        blink(blinkDur)
    }
    fun blink(dur: Int) {
        val appearDuration = dur / 4
        val disappearDuration = dur / 4
        tvPrimary.alpha = 0f
        tvPrimary.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    tvPrimary.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                tvPrimary.alpha = 0f
                            }
                        })
                }
            })

        tvSecondary.alpha = 0f
        tvSecondary.animate()
            .alpha(1f)
            .setDuration(appearDuration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    tvSecondary.animate()
                        .alpha(0f)
                        .setDuration(disappearDuration.toLong())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                tvSecondary.alpha = 0f
                            }
                        })
                }
            })


    }
    // Add more methods as needed to manipulate TextViews...
}
