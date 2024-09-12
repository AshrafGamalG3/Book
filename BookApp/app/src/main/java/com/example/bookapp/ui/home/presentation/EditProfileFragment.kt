package com.example.bookapp.ui.home.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bookapp.R
import com.example.bookapp.databinding.FragmentEditProfileBinding
import com.example.bookapp.ui.auth.data.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by viewModels()
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                // Set the selected image in the profile ImageView
                binding.imageProfile.setImageURI(it)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                // Set the captured image in the profile ImageView
                binding.imageProfile.setImageBitmap(it)
            }
        }

    companion object {

        private const val CAMERA_PERMISSION_CODE = 2001
        private const val GALLERY_PERMISSION_CODE = 2002

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditProfileBinding.bind(view)
        val user = EditProfileFragmentArgs.fromBundle(requireArguments()).user
        binding.edTextName.text = Editable.Factory.getInstance().newEditable(user.name)
        binding.imageProfile.setImageURI(user.imagePath.toUri())
        onClick(user)


    }

    private fun onClick(user: User) {


        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonUpdate.setOnClickListener {
            if (binding.edTextName.text.toString() == user.name) {
                Toast.makeText(requireContext(), "Your name is the same", Toast.LENGTH_SHORT).show()
            } else {
                binding.buttonUpdate.startAnimation()
                appViewModel.updateProfile(user.copy(name = binding.edTextName.text.toString()))
                binding.buttonUpdate.revertAnimation()
                findNavController().navigateUp()
            }
        }

        binding.imageProfile.setOnClickListener {
            val dialog = ProfilePhotoDialogFragment(user,binding.imageProfile)
            dialog.show(parentFragmentManager, "ProfilePhotoDialogFragment")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}