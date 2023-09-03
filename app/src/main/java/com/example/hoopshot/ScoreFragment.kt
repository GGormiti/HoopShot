package com.example.hoopshot

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.hoopshot.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPrefScore =
            requireContext().getSharedPreferences(SCORE_NAME, Context.MODE_PRIVATE)

        if (sharedPrefScore.contains(SCORE_NAME)){
            binding.bestScore.text = sharedPrefScore.getInt(SCORE_NAME,0).toString()
        } else{
            binding.bestScore.text = "0"
        }

        binding.backButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_scoreFragment_to_startFragment)
        }
    }


}