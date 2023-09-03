package com.example.hoopshot

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.hoopshot.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    var score: Int = 0
    var frameNumber = 0
    private var bestScore = 0

    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var backgroundArrow: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        return binding.root


    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefScore =
            requireContext().getSharedPreferences(SCORE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefScore.edit()


        binding.countText.text = score.toString()


        val animThrow = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_ball_throw)
        val animGoal = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_goal)
        val animLeftClosely =
            AnimationUtils.loadAnimation(requireContext(), R.anim.animate_left_closely_miss)
        val animLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_left_miss)
        val animRightClosely =
            AnimationUtils.loadAnimation(requireContext(), R.anim.animate_right_closely)
        val animRight = AnimationUtils.loadAnimation(requireContext(), R.anim.animate_right_miss)
        backgroundArrow = binding.arrow
        backgroundArrow.setBackgroundResource(R.drawable.animation_list_arrow)
        animationDrawable = backgroundArrow.background as AnimationDrawable
        animationDrawable.start()

        if (sharedPrefScore.contains(SCORE_NAME)) {
            bestScore = sharedPrefScore.getInt(SCORE_NAME, 0)
        }


        animThrow.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                when (frameNumber) {
                    0, 4 -> {
                        binding.ball.startAnimation(animGoal)
                    }

                    1, 3 -> {
                        missListener(animLeftClosely)
                        binding.ball.startAnimation(animLeftClosely)
                    }

                    2 -> {
                        missListener(animLeft)
                        binding.ball.startAnimation(animLeft)
                    }

                    6 -> {
                        missListener(animRight)
                        binding.ball.startAnimation(animRight)
                    }

                    else -> {
                        missListener(animRightClosely)
                        binding.ball.startAnimation(animRightClosely)
                    }
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        animGoal.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                score++
                binding.countText.text = score.toString()
                binding.ball.isClickable = true
                animationDrawable.start()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        binding.ball.setOnClickListener {
            binding.ball.isClickable = false

            animationDrawable.stop()
            for (i in 0..animationDrawable.numberOfFrames) {
                val checkFrame = animationDrawable.getFrame(i)
                val currentFrame = animationDrawable.current
                if (checkFrame == currentFrame) {
                    frameNumber = i
                    break
                }
            }
            it.startAnimation(animThrow)
            binding.arrow.clearAnimation()
        }

        binding.backButton.setOnClickListener {
            if (score > bestScore) {
                editor.clear()
                editor.putInt(SCORE_NAME, score)
                editor.apply()
            }
            it.findNavController().navigate(R.id.action_gameFragment_to_startFragment)
        }

    }

    fun missListener(anim: Animation) {
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.ball.isClickable = true
                animationDrawable.start()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

    }

}