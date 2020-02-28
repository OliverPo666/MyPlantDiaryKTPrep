package app.plantdiary.myplantdiaryktprep.ui.main

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.EXTRA_MIME_TYPES
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import app.plantdiary.myplantdiaryktprep.LocationViewModel
import app.plantdiary.myplantdiaryktprep.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    private val LOCATION_REQUEST_CODE = 1997
    private val IMAGE_CAPTURE_REQUEST_CODE = 1998
    private val CAMERA_REQUEST_CODE = 1999
    private val SAVE_IMAGE_REQUEST_CODE=2000
    private val IMAGE_GALLERY_REQUEST_CODE=2001
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.plants.observe(this, Observer {
            // do something here to wire up the objects, from the feed of JSON data, to be the autocomplete's data source.
            plants ->  actPlants.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, plants))
            var i = 1 + 1
            prepRequestLocationUpdates()
        })

        btnTakePhoto.setOnClickListener{
            prepTakePhoto()
        }

        btnLogon.setOnClickListener {
            prepOpenGallery()
        }
    }

    private fun prepOpenGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
//            val mimeTypes = arrayOf("image/*")
//            putExtra(EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
        }
    }

    private fun prepTakePhoto() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhoto()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.CAMERA)
            requestPermissions(permissionRequest, CAMERA_REQUEST_CODE)
        }
    }

    private fun takePhoto() {
        Toast.makeText(context, "Click", Toast.LENGTH_LONG).show()
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{
            takePictureIntent -> takePictureIntent.resolveActivity(context!!.packageManager)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // fall back to thumbnail.
                null
            }
            if (takePictureIntent == null) {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
            } else {
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        activity!!.applicationContext,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, SAVE_IMAGE_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                imgPlant.setImageBitmap(imageBitmap)
            } else if (requestCode == SAVE_IMAGE_REQUEST_CODE) {
                Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
            } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                val selectedImage = data!!.data
                val source = ImageDecoder.createSource(activity!!.contentResolver, selectedImage!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                imgPlant.setImageBitmap(bitmap)
            }
        }
    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_REQUEST_CODE)
        }
    }

    private fun requestLocationUpdates() {
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationData().observe(this, Observer {
            lblLatitudeValue.text = it.latitude
            lblLongitudeValue.text = it.longitude
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_REQUEST_CODE  -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(context, "Unable to show GPS without location permission", Toast.LENGTH_LONG).show()
                }

            }
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto()
                } else {
                    Toast.makeText(context, "Unable to show GPS without location permission", Toast.LENGTH_LONG).show()
                }
            }
            else -> {

            }
        }

    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PlantDiary${timeStamp}", ".jpg", storageDir).apply{
            currentPhotoPath = absolutePath
        }

    }



}
