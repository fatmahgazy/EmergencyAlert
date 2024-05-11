package org.codeforegypt.quickassestant.ui.fragments.emergencies.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.data.model.ModifyEmergency
import org.codeforegypt.quickassestant.databinding.FragmentPublishReportBinding
import org.codeforegypt.quickassestant.databinding.FragmentUserProfileBinding
import org.codeforegypt.quickassestant.ui.LocationProvider
import org.codeforegypt.quickassestant.ui.fragments.emergencies.emergancy.EmergencyResult
import java.util.UUID

@AndroidEntryPoint
class PublishReportFragment : Fragment() {

    private var _binding: FragmentPublishReportBinding? = null
    private val binding get() = _binding!!

    val viewModel: AddEmergencyViewModel by viewModels()

    private val locationPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        val allPermissionsNotGranted = results.values.all { it }
        if (allPermissionsNotGranted)
            observeLocation()
        else
            findNavController().navigateUp()
    }

    private fun observeLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            LocationProvider(requireContext()).observeLocationUpdate()
                .onStart {
                    binding.flLoading.isVisible = true
                }
                .collectLatest { location ->
                    if (location != null) {
                        viewModel.userLocation = location
                        binding.flLoading.isVisible = false
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPublishReportBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserLocation()
        observeOnAddEmergency()
        binding.ivReturn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnPost.setOnClickListener {
            sendEmergency()
        }
    }

    private fun sendEmergency() {
        if (viewModel.userLocation == null) {
            Toast.makeText(requireContext(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show()
            return
        }

        val enteredEmergencyText = binding.etEmeregencyText.text.toString()
        val emergencyText = if (enteredEmergencyText.trim().isBlank())
            "Help me please!"
        else
            enteredEmergencyText

        val emergency = ModifyEmergency(
            viewModel.userLocation!!.latitude,
            viewModel.userLocation!!.longitude,
            emergencyText,
            ""
        )
        viewModel.addEmergency(emergency)
    }

    /*private fun sendNotification(body: String) {
        // Create a Notification message
        val notificationMessage = RemoteMessage.Builder("your-topic-name")
            .setMessageId(UUID.randomUUID().toString())
            .addData("title", "Notification Title")
            .addData("body", "Notification Body")
            .build()

// Send the notification
        FirebaseMessaging.getInstance().send(notificationMessage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Notification sent successfully")
                } else {
                    Log.e(TAG, "Failed to send notification", task.exception)
                }
            }
    }*/

    private fun getUserLocation() {
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            locationPermissions.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        else
            observeLocation()
    }

    private fun observeOnAddEmergency(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addEmergencyState.collectLatest { result ->
                when(result){
                    EmergencyResult.SUCCESS -> findNavController().navigateUp()
                    EmergencyResult.FAILED -> Toast.makeText(requireContext(),"Something went wrong!", Toast.LENGTH_SHORT).show()
                    EmergencyResult.NORMAL -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}