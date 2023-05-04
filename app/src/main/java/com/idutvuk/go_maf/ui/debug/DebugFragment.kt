package com.idutvuk.go_maf.ui.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.idutvuk.go_maf.databinding.FragmentDebugBinding

class DebugFragment : Fragment() {
    private lateinit var viewModel: DebugViewModel
    private lateinit var b: FragmentDebugBinding
    private lateinit var mBottleImageView: ImageView
    private var lastAngle: Float = -1F
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[DebugViewModel::class.java]
        b = FragmentDebugBinding.inflate(inflater,container,false)
        viewModel.initViews(b,requireContext())

        mBottleImageView = b.imageviewBottle

        b.btnDebug.setOnClickListener {
            spinBottle()
        }
        return b.root
    }

    private fun spinBottle() {
        val angle = 90F
        // Центр вращения
        val pivotX: Float = (mBottleImageView.width / 2).toFloat()
        val pivotY: Float = (mBottleImageView.height / 2).toFloat()
        val animation: Animation =
            RotateAnimation(if (lastAngle == -1F) 0F else lastAngle, angle+lastAngle, pivotX, pivotY)
        lastAngle = angle
        animation.duration = 300
        animation.fillAfter = true
        mBottleImageView.startAnimation(animation)
    }
}