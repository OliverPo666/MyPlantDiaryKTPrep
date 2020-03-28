package app.plantdiary.myplantdiaryktprep.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.plantdiary.myplantdiaryktprep.LocationViewModel
import app.plantdiary.myplantdiaryktprep.R
import app.plantdiary.myplantdiaryktprep.dto.Photo
import app.plantdiary.myplantdiaryktprep.dto.Plant
import app.plantdiary.myplantdiaryktprep.dto.Specimen
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class MainFragment : DiaryFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    private val LOCATION_REQUEST_CODE = 1997
    private lateinit var _plants : ArrayList<Plant>
    private var plantId = 0;
    private var specimen = Specimen()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activity.let {
            viewModel = ViewModelProviders.of(it!!).get(MainViewModel::class.java)
        }
        // TODO: Use the ViewModel
        viewModel.plants.observe(this, Observer {
            // do something here to wire up the objects, from the feed of JSON data, to be the autocomplete's data source.
            plants ->  actPlants.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, plants))
            _plants = plants
            prepRequestLocationUpdates()
        })
         actPlants.setOnItemClickListener { parent, view, position, id ->
            var selectedPlant = parent.getItemAtPosition(position) as Plant
             if (selectedPlant != null) {
                 plantId = selectedPlant.plantId
             }
         }
        viewModel.specimens.observe(this, Observer {
            specimens -> spnSpecimens.setAdapter(ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, specimens))
        })

        btnTakePhoto.setOnClickListener{
            prepTakePhoto()
        }

        btnLogon.setOnClickListener {
            logon()
            // prepOpenGallery()
        }

        btnSaveEvent.setOnClickListener {
            saveSpecimen()
        }
    }

    private fun logon() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), AUTH_REQUEST_CODE
        )
    }

    private fun saveSpecimen() {
        if (user == null) {
            logon()
        }
        user ?: return

        viewModel.save(specimen, photos, user!!)

        // new specimen for the next go.
        specimen = Specimen()
        photos = ArrayList<Photo>()

    }

    internal fun storeSpecimen() {
        specimen.plantName = actPlants.text.toString()
        specimen.description = txtDescription.text.toString()
        specimen.latitude = lblLatitudeValue.text.toString()
        specimen.longitude = lblLongitudeValue.text.toString()
        specimen.plantID = plantId
        specimen.specimenID = "1"
        viewModel.specimen = specimen
    }

    private fun prepOpenGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SAVE_IMAGE_REQUEST_CODE) {
                Toast.makeText(context, "Image Saved", Toast.LENGTH_LONG).show()
                var myPhoto = Photo(localUri = photoURI.toString(), dateTaken = Date())
                photos.add(myPhoto)
                photoURI = null
            } else if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                imgPlant.setImageBitmap(imageBitmap)
            } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                val selectedImage = data!!.data
                val source = ImageDecoder.createSource(activity!!.contentResolver, selectedImage!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                imgPlant.setImageBitmap(bitmap)
            } else if (requestCode == AUTH_REQUEST_CODE) {
                user = FirebaseAuth.getInstance().currentUser
                var i = 1 + 1
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            if (requestCode == AUTH_REQUEST_CODE) {
                Toast.makeText(context!!, "Kan't logon", Toast.LENGTH_LONG).show()
            }
        }
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
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
         }

    }

}
