package org.codeforegypt.quickassestant.ui.splachFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentFirstSplachBinding


class FirstSplachFragment : Fragment() {
   private var _binding: FragmentFirstSplachBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstSplachBinding.inflate(inflater, container, false)
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val hasSeenSplash = sharedPreferences.getBoolean("hasSeenSplash", false)
        if (hasSeenSplash) {
            // If the user has seen the splash screen, navigate to the next destination
            findNavController().navigate(R.id.action_firstSplachFragment_to_mainFragment2)
            return null // Return null to pre

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_firstSplachFragment_to_secondFragment)
        }
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("hasSeenSplash", true)
            apply()
        }
    }
}