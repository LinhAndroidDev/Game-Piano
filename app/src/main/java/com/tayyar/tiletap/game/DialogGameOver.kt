package com.tayyar.tiletap.game

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.tayyar.tiletap.R
import com.tayyar.tiletap.databinding.LayoutGameOverBinding
import com.tayyar.tiletap.utils.highlight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialogGameOver : DialogFragment() {
    private var binding: LayoutGameOverBinding? = null
    var onClickReplay: (() -> Unit)? = null
    private val enterView by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.enter_from_center) }
    private val exitView by lazy { AnimationUtils.loadAnimation(requireActivity(), R.anim.exit_to_center) }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGameOverBinding.inflate(layoutInflater)
        binding?.root?.startAnimation(enterView)
        initView()
        onClickView()
        return binding?.root
    }

    private fun initView() {
        val score: Int = arguments?.getInt(GameActivity.SCORE_PLAYER) ?: 0
        binding?.score?.text = score.toString()

        val stars = listOf(binding?.star1, binding?.star2, binding?.star3, binding?.star4, binding?.star5)

        when(score) {
            in 0 until 20 -> {

            }

            in 20 until 40 -> {
                lifecycleScope.launch {
                    delay(400)
                    stars[0]?.highlight(requireActivity())
                }
            }

            in 40 until 60 -> {
                lifecycleScope.launch {
                    for(i in 0..1) {
                        delay(400)
                        stars[i]?.highlight(requireActivity())
                    }
                }
            }

            in 60 until 80 -> {
                lifecycleScope.launch {
                    for(i in 0..2) {
                        delay(400)
                        stars[i]?.highlight(requireActivity())
                    }
                }
            }

            in 80 until 100 -> {
                lifecycleScope.launch {
                    for(i in 0..3) {
                        delay(400)
                        stars[i]?.highlight(requireActivity())
                    }
                }
            }

            else -> {
                lifecycleScope.launch {
                    for(i in 0..4) {
                        delay(400)
                        stars[i]?.highlight(requireActivity())
                    }
                }
            }
        }
    }

    private fun onClickView() {
        binding?.replay?.setOnClickListener {
            exitGameOver()
            lifecycleScope.launch {
                delay(430)
                withContext(Dispatchers.Main) {
                    dismiss()
                    onClickReplay?.invoke()
                }
            }
        }
        binding?.home?.setOnClickListener {
            exitGameOver()
            lifecycleScope.launch {
                delay(430)
                withContext(Dispatchers.Main) {
                    dismiss()
                    (context as GameActivity).finish()
                }
            }
        }
        binding?.share?.setOnClickListener {
            exitGameOver()
        }
    }

    private fun exitGameOver() {
        binding?.root?.startAnimation(exitView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setLayout(resources.getDimensionPixelSize(R.dimen.Dialog_GameOver_Width), ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = false
        }
    }
}