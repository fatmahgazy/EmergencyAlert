package org.codeforegypt.quickassestant.ui.fragments.emergencies.myemergency

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentMyEmergencyBinding
import org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy.EmergencyAdapter
import org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy.EmergencyResult

@AndroidEntryPoint
class MyEmergencyFragment : Fragment() {
    private var _binding: FragmentMyEmergencyBinding? = null
    private val binding get() = _binding!!

    private val myEmergencyAdapter = MyEmergencyAdapter()
    val myEmergencyViewModel: MyEmergencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initEmergencyList()
        observeOnEmergencies()
        myEmergencyViewModel.getMyEmergency()
        myEmergencyAdapter.onShowLocationClick = {
            //if (requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            showLocation(it.longitude, it.longitude)
        }
    }

    private fun showLocation(latitude: Double, longitude: Double) {
        val destinationLocation = "$latitude,$longitude"
        val uri = "http://maps.google.com/maps?saddr=&daddr=$destinationLocation"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    private fun observeOnEmergencies() {
        viewLifecycleOwner.lifecycleScope.launch {
            myEmergencyViewModel.emergencyState
                .collectLatest { result ->
                    myEmergencyAdapter.submitList(result)
                }
        }
    }


    private fun initEmergencyList() {
        binding.rvEmergency.apply {
            setHasFixedSize(true)
            adapter = EmergencyAdapter()
        }
    }
    private fun initToolbar() {
        binding.ivReturn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}