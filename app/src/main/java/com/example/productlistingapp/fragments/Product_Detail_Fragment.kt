package com.example.productlistingapp.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.productlistingapp.Adapter.CustomAutoCompleteAdapter
import com.example.productlistingapp.ViewModel.Product_Viewmodel
import com.example.productlistingapp.databinding.FragmentProductDetailBinding
import com.example.productlistingapp.utils.ImagePicker
import com.example.productlistingapp.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class Product_Detail_Fragment : Fragment() {

    private var fragmentProductDetailBinding: FragmentProductDetailBinding?= null
    private val binding get() = fragmentProductDetailBinding!!
    private val options = listOf("Product", "Electronics", "Service","Books","Food")
    private lateinit var adapter: CustomAutoCompleteAdapter
    private val productViewmodel: Product_Viewmodel by activityViewModels()
    private var typeString: String = ""
    private var check: Boolean= false
    private var imageUri: Uri?= null
    private var imagePart: MultipartBody.Part?= null
    private val GALLERY_PERMISSION_REQUEST_CODE = 100

    val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            imageUri= selectedImageUri
            Glide.with(this)
                .load(selectedImageUri)
                .into(binding.profilePic)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentProductDetailBinding= FragmentProductDetailBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            bindObservers()
            setAdapter()
            setHandlers()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun createImageFile(context: Context, imageUri: Uri?): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(imageUri!!, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val imagePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()

        return if (imagePath != null) File(imagePath) else null
    }

    fun createImagePart(file: File): MultipartBody.Part {
        // Create a request body for the file
        val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)

        // Create a multipart/form-data part using the request body
        return MultipartBody.Part.createFormData("files[]", file.name, requestFile)
    }

    private fun setHandlers() {

        try {
            binding.AddProduct.setOnClickListener(View.OnClickListener {
                if(ValidateFields()) {

                    if(imageUri!=null) {
                        val imageFile: File? = createImageFile(requireContext(), imageUri)
                        if(imageFile!=null) {
                           imagePart= createImagePart(imageFile)
                        }
                    }else {
                        imagePart = null
                    }
                    productViewmodel.addData(
                        binding.edtTextForProductName.text.toString(), typeString,
                        binding.edtTextForProductPrice.text.toString(),
                        binding.edtTextForProductTax.text.toString().toDouble(),imagePart
                    )
                }
            })

            binding.profilePic.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openImagePicker()
                } else {
                    // Permission not granted, request it from the user
                    requestGalleryPermission()
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestGalleryPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            GALLERY_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform the gallery-related task
                openImagePicker()
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(
                    requireContext(),
                    "Gallery permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun ValidateFields(): Boolean {

        try {
            if(binding.edtTextForProductName.text.toString().isEmpty()){
                binding.errorTxt.text= "Please Fill Product Name"
                binding.errorTxt.visibility= View.VISIBLE
                return false
            }
            else  if(binding.edtTextForProductPrice.text.toString().isEmpty()){
                binding.errorTxt.text= "Please Fill Product Price"
                binding.errorTxt.visibility= View.VISIBLE
                return false
            }
            else  if(binding.edtTextForProductTax.text.toString().isEmpty()){String
                binding.errorTxt.text= "Please Fill Product Tax"
                binding.errorTxt.visibility= View.VISIBLE
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        check= true
        return true
    }

    private fun openImagePicker() {

        try {
            ImagePicker.pickImage(requireActivity(),imagePickerLauncher)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindObservers() {

        try {
            productViewmodel.productModelLiveData.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = View.INVISIBLE
                when (it) {
                    is NetworkResult.Success -> {
                        println("Successful")

                        if(check) {
                            findNavController().popBackStack()
                        }
                    }
                    is NetworkResult.Error -> {}
                    is NetworkResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun setAdapter() {
        try {
            adapter = CustomAutoCompleteAdapter(activity!!.applicationContext, options)
            binding.edtTextForProductType.setAdapter(adapter)
            binding.edtTextForProductType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedItem = parent.getItemAtPosition(position).toString()
                        println(selectedItem)
                        typeString=selectedItem
                        // Do something with the selected item
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Handle the case where no item is selected
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        fragmentProductDetailBinding= null
        productViewmodel.productModelLiveData.removeObservers(viewLifecycleOwner)
    }
}