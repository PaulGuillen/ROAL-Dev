package com.example.roal.view.emergency

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.example.roal.R
import com.example.roal.databinding.ActivityEmergencyBinding
import com.example.roal.databinding.ActivityPerfilBinding
import com.example.roal.models.Workers
import com.example.roal.providers.WorkersProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import utils.ChargeDialog
import java.io.File
import java.io.IOException

class EmergencyActivity : AppCompatActivity() {

    lateinit var binding: ActivityEmergencyBinding
    var workersProvider = WorkersProvider()
    private var progressDialog: Dialog? = null
    var imageFile: File? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Emergencia"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSearchByDNI.setOnClickListener { searchingByDNI() }
        binding.btnSearchByPhoto.setOnClickListener { searchingByPhoto() }
        binding.btnSearchByQR.setOnClickListener { searchingByQR() }

        binding.btnPhonePrincipal.setOnClickListener { goToCall("phonePrincipal") }
        binding.btnPhoneSecondary.setOnClickListener { goToCall("phoneSecondary") }

        binding.imagePhoto.setOnClickListener { selectImage() }

    }

    private fun goToCall(text: String){
        when(text) {
            "phonePrincipal" -> {
                val number: String = binding.textPrincipal.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
                startActivity(intent)
            }
            "phoneSecondary" -> {
                val number: String = binding.textSecondary.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
                startActivity(intent)
            }
            else -> {
                /**Casuistica no contemplada*/
                TODO()
            }
        }
    }

    private fun searchingByDNI() {
        binding.searchBox.visibility = View.VISIBLE
        binding.imagePhoto.visibility = View.GONE
        binding.imageQR.visibility = View.GONE
        /**Si el DNI existe se muestra*/
        searchWorkers()
    }

    private fun searchingByPhoto() {
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.VISIBLE
        binding.imageQR.visibility = View.GONE
        binding.linearDNI.visibility = View.GONE
    }

    private fun searchingByQR() {
        binding.searchBox.visibility = View.GONE
        binding.imagePhoto.visibility = View.GONE
        binding.imageQR.visibility = View.VISIBLE
        binding.linearDNI.visibility = View.GONE
    }

    private fun searchWorkers() {
        binding.searchBox.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getWorkers()
                performSearch()
            }
            false
        }
    }

    private fun getWorkers() {
        showLoading()
        val dni = binding.searchBox.text.toString()
        workersProvider.getWorkers(dni)?.enqueue(object : Callback<Workers> {
            override fun onResponse(call: Call<Workers>, response: Response<Workers>) {
                if (response.body() != null) {
                    hideLoading()
                    binding.linearDNI.visibility = View.VISIBLE

                    /**Data base*/
                    val textIdentification = response.body()?.dni
                    val textName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = "$textName $textLastName"

                    /**Celulares*/
                    val textPrincipalPhone = response.body()?.phone
                    val textSecondaryPhone = response.body()?.phone_emergency

                    val textArea = response.body()?.area
                    val textDateBirth = response.body()?.date_birth
                    val textJoinDate = response.body()?.date_join
                    val textBloodType = response.body()?.blood_type
                    val textDiseases = response.body()?.diseases
                    val textAllergies = response.body()?.allergies

                    binding.textDNI.text = textIdentification
                    binding.textFullName.text = textFullName
                    binding.textArea.text = textArea
                    binding.textBornDate.text = textDateBirth
                    binding.textJoinDate.text = textJoinDate
                    binding.textPrincipal.text = textPrincipalPhone
                    binding.textSecondary.text = textSecondaryPhone
                    binding.textBloodType.text = textBloodType
                    binding.textDiseases.text = textDiseases
                    binding.textAllergies.text = textAllergies
                } else {
                    hideLoading()
                    Toast.makeText(this@EmergencyActivity, "No data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Workers>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@EmergencyActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

    private fun performSearch() {
        binding.searchBox.clearFocus()
        val `in`: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
    }

    /**Searching by PHOTO methods*/
    private fun sendImageToBE() {
        CoroutineScope(Dispatchers.Default).launch {
            startImageForResult.let {
                imageFile?.let {
                    val imageInBase64 = getBase64ForUriAndPossiblyCrash(it.toUri())
                    val user = Workers(
                        photo = imageInBase64,
                        photoFormat = imageFile?.name
                    )
                    workersProvider.consultByPhoto(user)?.enqueue(object : Callback<Workers> {
                        override fun onResponse(call: Call<Workers>, response: Response<Workers>) {
                            Toast.makeText(this@EmergencyActivity, "Se guardo la informacion", Toast.LENGTH_LONG).show()
                            if (response.isSuccessful){
                                binding.linearDNI.visibility = View.VISIBLE
                                clearForm()
                                Timber.d("Response = ${response.body()}")
                            }else{
                                binding.linearDNI.visibility = View.GONE
                            }
                            deleteCache(this@EmergencyActivity)
                        }

                        override fun onFailure(call: Call<Workers>, t: Throwable) {
                            binding.linearDNI.visibility = View.GONE
                            Toast.makeText(this@EmergencyActivity, "No se pudo guardar el dato", Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
        }
    }

    private fun clearForm() {
        imageFile = null
        binding.imagePhoto.setImageResource(R.drawable.ic_baseline_image_24)
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            dataResult(result)
        }

    private fun dataResult(result : ActivityResult){
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = fileUri?.path?.let { File(it) }
            binding.imagePhoto.setImageURI(fileUri)
            sendImageToBE()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Tarea se cancelo", Toast.LENGTH_LONG).show()
        }
    }

    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent -> startImageForResult.launch(intent) }
    }

    private fun getBase64ForUriAndPossiblyCrash(uri: Uri): String {
        return try {
            val bytes = contentResolver.openInputStream(uri)?.readBytes()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace() // This exception always occurs
            "Ha ocurrido un error"
        }
    }

    fun deleteCache(context: Context) {
        try {
            val dir: File = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list() as Array<String>
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}
