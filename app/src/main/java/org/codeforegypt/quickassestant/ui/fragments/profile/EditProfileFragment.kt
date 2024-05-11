package org.codeforegypt.quickassestant.ui.fragments.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.codeforegypt.quickassestant.data.model.User
import org.codeforegypt.quickassestant.data.model.UserSignUpModel
import org.codeforegypt.quickassestant.databinding.FragmentEditProfileBinding
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    val viewModel: EditProfileViewModel by viewModels()

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var selectedBloodType: String? = null


    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                getFileFromUri(uri, requireContext())?.let { file ->
                    if (file.exists())
                        viewModel.editProfilePicture(file.path)
                }
            }
        }

    private val photoPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            if (!isGranted.all { it.value }) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            } else {
                photoPickerLauncher.launch("image/*")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEditProfileResult()
        observeEditProfiolePictureResult()

        val spinnerItem = arrayOf("Male", "Female")
        val arrayAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinnerItem
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = arrayAdapter

        binding.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(requireContext(), "${spinnerItem[p2]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "please select item", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgEditProfilePicture.setOnClickListener {
            checkRequiredPermissionsGranted {
                photoPickerLauncher.launch("image/*")
            }
        }

        binding.btnPost.setOnClickListener {

            val validationResult = validateSignUpDetails()
            if (validationResult != null) {
                Toast.makeText(requireContext(), validationResult, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fullName = binding.edFullName.text.toString()
            val phoneNumber = binding.edPhoneNumber.text.toString()
            val email = binding.edEmail.text.toString()
            val age = binding.edAge.text.toString()
            val bloodType = binding.edBloodType.text.toString()


            viewModel.editProfile(
                email, age, phoneNumber, fullName, bloodType
            )


        }
    }

    private fun requestMultiplePermissions() {
        photoPermission.launch(arrayOf(imagesPermission()))
    }

    private fun checkRequiredPermissionsGranted(onGranted: () -> Unit) {
        if (isImagesPermissionGranted()) {
            onGranted()
        } else {
            requestMultiplePermissions()
        }
    }

    private fun isImagesPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun imagesPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun getFileFromUri(uri: Uri, context: Context): File? {
        context.contentResolver.openInputStream(uri)?.use { input ->
            val file = File(context.cacheDir, getFileName(uri, context))
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }
                output.flush()
                return file
            }
        }
        return null
    }


    private fun getFileName(uri: Uri, context: Context): String {

        return try {
            DocumentFile.fromSingleUri(context, uri)?.name ?: System.currentTimeMillis().toString()
        } catch (e: NullPointerException) {
            System.currentTimeMillis().toString()
        }
    }

    private fun observeEditProfiolePictureResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pictureState.collectLatest { state ->
                when (state) {
                    EditProfilePictureResult.NORMAL -> {}
                    EditProfilePictureResult.SUCCESS -> findNavController().navigateUp()
                    EditProfilePictureResult.FAILED -> Toast.makeText(
                        requireContext(),
                        "Something went wrong! please try again.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun observeEditProfileResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editProfileState
                .collectLatest { state ->
                    when (state) {
                        EditProfileResult.NORMAL -> {}
                        EditProfileResult.SUCCESS -> findNavController().navigateUp()
                        EditProfileResult.FAILED -> Toast.makeText(
                            requireContext(),
                            "Something went wrong! please try again.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
    }

    private fun validateSignUpDetails(): String? {
        if (binding.edFullName.text.toString().isBlank())
            return "Full name is required"
        if (binding.edEmail.text.toString().isBlank())
            return "Email address is required"
        val phoneNumber = binding.edPhoneNumber.text.toString()
        if (binding.edPhoneNumber.text.toString().isBlank())
            return "Phone number is required"
        else if (phoneNumber.length == 11 && !phoneNumber.startsWith("01"))
            return "Invalid Phone number"
        else if (phoneNumber.length == 13 && !phoneNumber.startsWith("+20"))
            return "Invalid phone number"
        if (selectedBloodType.isNullOrBlank())
            return "Gender is required"
        if (binding.edAge.text.isBlank())
            return "Age is required"
        return null

    }

}