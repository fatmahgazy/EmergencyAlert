package org.codeforegypt.quickassestant.ui.fragments

import android.Manifest
import android.R.attr.fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.codeforegypt.quickassestant.R
import org.codeforegypt.quickassestant.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment() {

   private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    val phoneCallPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReport.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_publishReportFragment)
        }

        phoneCallPermission.launch(Manifest.permission.CALL_PHONE)

        binding.btnMedical.setOnClickListener {
            emergencyCall("123")
        }
        binding.btnFire.setOnClickListener {
            emergencyCall("180")
        }

        binding.btnAccident.setOnClickListener {
            emergencyCall("911")
        }

        binding.btnDisaster.setOnClickListener {
            emergencyCall("115")
        }
    }

    private fun emergencyCall(tel: String) {
        if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            phoneCallPermission.launch(Manifest.permission.CALL_PHONE)
        } else {
            val dial = "tel:$tel"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(dial))
            startActivity(intent)
        }
    }

}