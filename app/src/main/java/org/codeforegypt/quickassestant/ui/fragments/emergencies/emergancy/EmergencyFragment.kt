package org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.databinding.FragmentMyEmergencyBinding

@AndroidEntryPoint
class EmergencyFragment : Fragment() {
    private var _binding: FragmentMyEmergencyBinding? = null
    private val binding get() = _binding!!

    val emergenciesViewModel: EmergencyViewModule by viewModels()

    private val adapter = EmergencyAdapter()

    val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission -> }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEmergencyList()
        initToolbar()
        observeOnEmergencies()
        adapter.onCallClick = {
            if (requireActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                callPhone(it.phoneNumber)
            else
                permission.launch(android.Manifest.permission.CALL_PHONE)
        }

        adapter.onShowLocationClick = {
            //if (requireActivity().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            showLocation(it.latitude, it.longitude)
        }

    }

    private fun showLocation(latitude: Double, longitude: Double) {
        val destinationLocation = "$latitude,$longitude"
        val uri = "http://maps.google.com/maps?saddr=&daddr=$destinationLocation"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }


    private fun callPhone(phone: String) {
        if (requireActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            permission.launch(android.Manifest.permission.CALL_PHONE)
        else {
            val dial = "tel: $phone"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(dial))
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        emergenciesViewModel.getEmergency()
    }

    private fun initEmergencyList() {
        binding.rvEmergency.apply {
            setHasFixedSize(true)
            adapter = EmergencyAdapter()
        }
    }

    private fun observeOnEmergencies() {
        viewLifecycleOwner.lifecycleScope.launch {
            emergenciesViewModel.emergencyState
                .collectLatest { result ->
                    adapter.submitList(result)
                }
        }
    }

    private fun initToolbar() {
        binding.ivReturn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}