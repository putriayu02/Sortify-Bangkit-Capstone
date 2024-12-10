package com.bangkit.sortify

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.sortify.databinding.FragmentHomeBinding
import com.bangkit.sortify.factory.ViewModelFactory
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnScan.setOnClickListener {
            pickImage()
        }

        return binding.root
    }

    private fun pickImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Option")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                Log.d("@@camera","take photo")
                val fileName = "new-photo-name.jpg"
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, fileName)
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
                imageUri = requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, 100)
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 200)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageBitmap = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageBitmap)
            val thumbnailBitmap = ThumbnailUtils.extractThumbnail(bitmap, 150, 150)

            val inputStream = requireContext().contentResolver.openInputStream(data.data!!)
            val file = File(requireContext().cacheDir, "temp_image.jpg")
            FileOutputStream(file).use { output ->
                inputStream?.copyTo(output)
            }

            val requestBody: RequestBody = file.asRequestBody("image/jpg".toMediaType())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestBody)

            homeViewModel.postFoto(body)
            homeViewModel.predict.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    Log.d("@@Success", response.message)
                }
            }
        } else if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            Log.d("@@camera", imageUri!!.path!!)
            Log.d("@@camera", "here")


            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
            val file = File(requireContext().cacheDir, "new-photo-name.jpg")
            FileOutputStream(file).use { output ->
                inputStream?.copyTo(output)
            }

            val requestBody: RequestBody = file.asRequestBody("image/jpg".toMediaType())
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)

            homeViewModel.postFoto(body)
            homeViewModel.predict.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    val intent = Intent(requireContext(),ResultActivity::class.java)
                    intent.putExtra("data",response.data.result)
                    intent.putExtra("image", imageUri.toString())
                    startActivity(intent)
                }

            }
        } else {
            Log.d("@@camera", "data null")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
