package com.example.roal.view.home_options

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.roal.R
import com.example.roal.databinding.ActivityCreateWorkerBinding
import com.example.roal.models.ResponseHttp
import com.example.roal.models.Workers
import com.example.roal.providers.WorkersProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.ChargeDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@SuppressLint("SourceLockedOrientationActivity")
class CreateWorkerActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateWorkerBinding
    var postWorkersProvider = WorkersProvider()
    private var imageFile: File? = null
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityCreateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Registro de trabajador"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnRegistrarTrabajador.setOnClickListener { creatingWorkers() }
        binding.imageViewUser.setOnClickListener { selectImage() }
        binding.btnCalendar.setOnClickListener { calendarView("textBornDate") }
        binding.btnCalendarJoin.setOnClickListener { calendarView("textJoinDate") }
    }

    private fun calendarView(text: String) {
        when (text) {
            "textBornDate" -> {
                val myCalendar = Calendar.getInstance()

                val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, month)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
                    updateLabel(myCalendar, 0)
                }

                DatePickerDialog(
                    this@CreateWorkerActivity, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            "textJoinDate" -> {
                val myCalendar = Calendar.getInstance()

                val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, month)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
                    updateLabel(myCalendar, 1)
                }

                DatePickerDialog(
                    this@CreateWorkerActivity, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun updateLabel(myCalendar: Calendar, direction: Int) {
        when (direction) {
            0 -> {
                val myFormat = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.textBornDate.setText(sdf.format(myCalendar.time))
            }
            1 -> {
                val myFormat = "dd-MM-yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.textJoinDate.setText(sdf.format(myCalendar.time))
            }
        }
    }

    private fun creatingWorkers() {

        val dni = binding.textDNI.text.toString()
        val name = binding.textName.text.toString()
        val lastname = binding.textLastname.text.toString()
        val date_birth = binding.textBornDate.text.toString()
        //    val date_join = binding.textJoinDate.text.toString()
        val area = binding.textArea.text.toString()
        val blood_type = binding.textBlood.text.toString()
        val diseases = binding.textIllness.text.toString()
        val allergies = binding.textAllergies.text.toString()
        val phone = binding.textPhone.text.toString()
        val phone_emergency = binding.textPhoneEmergency.text.toString()

        showLoading()
        if (isValidForm(dni, name, lastname, area, phone, phone_emergency)) {
            startImageForResult.let {
                if (imageFile != null) {
                    val imageBase = imageFile?.toUri()?.let { it1 -> getBase64ForUriAndPossiblyCrash(it1) }
                    val workerUser = Workers(
                        dni = dni,
                        name = name,
                        lastname = lastname,
                        date_birth = date_birth,
                        //   date_join = date_join,
                        area = area,
                        blood_type = blood_type,
                        diseases = diseases,
                        allergies = allergies,
                        phone = phone,
                        phone_emergency = phone_emergency,
                        photo = imageBase
                    )

                    CoroutineScope(Dispatchers.Default).launch {
                        postWorkersProvider.postWorkers(workerUser)?.enqueue(object : Callback<ResponseHttp> {
                            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                                if (response.body()?.code == 200) {
                                    hideLoading()
                                    runBlocking {
                                        deleteCache(this@CreateWorkerActivity)
                                    }
                                    clearForm()
                                    SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(getString(R.string.title_200_register_worker))
                                        .setContentText(getString(R.string.subtitle_200_register_worker))
                                        .show();
                                } else {
                                    hideLoading()
                                    SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(getString(R.string.title_error_register))
                                        .setContentText(getString(R.string.subtitle_error_register))
                                        .show();
                                }
                            }

                            override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                                hideLoading()
                                Toast.makeText(this@CreateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                            }

                        })
                    }
                } else {
                    hideLoading()
                    SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.title_404_image))
                        .setContentText(getString(R.string.subtitle_image_description))
                        .show();
                }
            }
        } else {
            hideLoading()
            SweetAlertDialog(this@CreateWorkerActivity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.title_missing_parameters))
                .setContentText(getString(R.string.subtitle_missing_parameters))
                .show();
        }
    }

    private fun clearForm() {
        if (imageFile != null) {
            imageFile = null
            binding.imageViewUser.setImageResource(R.drawable.ic_baseline_image_24)
            binding.textDNI.setText("")
            binding.textName.setText("")
            binding.textLastname.setText("")
            binding.textBornDate.setText("")
            binding.textJoinDate.setText("")
            binding.textArea.setText("")
            binding.textBlood.setText("")
            binding.textIllness.setText("")
            binding.textAllergies.setText("")
            binding.textPhone.setText("")
            binding.textPhoneEmergency.setText("")
        } else {
            binding.textDNI.setText("")
            binding.textName.setText("")
            binding.textLastname.setText("")
            binding.textBornDate.setText("")
            binding.textJoinDate.setText("")
            binding.textArea.setText("")
            binding.textBlood.setText("")
            binding.textIllness.setText("")
            binding.textAllergies.setText("")
            binding.textPhone.setText("")
            binding.textPhoneEmergency.setText("")
        }
    }

    private fun isValidForm(
        dni: String,
        name: String,
        lastname: String,
        area: String,
        phone: String,
        phoneEmergency: String
    ): Boolean {

        if (dni.isBlank() || dni.length < 8) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textDNI, messageError)
            return false
        }
        if (area.isBlank() || area.isEmpty()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textArea, messageError)
            return false
        }
        if (name.isBlank()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textName, messageError)
            return false
        }
        if (lastname.isBlank()) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textLastname, messageError)
            return false
        }
//        if (dateJoin.isBlank()) {
//            return false
//        }
        if (phone.isBlank() || phone.length < 9) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhone, messageError)
            return false
        }
        if (phoneEmergency.isBlank() || phoneEmergency.length < 9) {
            val messageError = getString(R.string.mandatory)
            toggleTextInputLayoutError(binding.textPhoneEmergency, messageError)
            return false
        }
        return true
    }

    private fun toggleTextInputLayoutError(
        textInputEditText: TextInputEditText,
        msg: String?
    ) {
        textInputEditText.error = msg
        textInputEditText.isTextInputLayoutFocusedRectEnabled = msg != null
    }

    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            expectedResult(result)
        }

    private fun expectedResult(result: ActivityResult) {
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = fileUri?.path?.let { File(it) }
            binding.imageViewUser.setImageURI(fileUri)
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