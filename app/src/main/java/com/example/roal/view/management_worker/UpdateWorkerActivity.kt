package com.example.roal.view.management_worker

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.example.roal.R
import com.example.roal.databinding.ActivityManagementWorkerBinding
import com.example.roal.databinding.ActivityUpdateWorkerBinding
import com.example.roal.models.Workers
import com.example.roal.providers.WorkersProvider
import com.example.roal.utils.ChargeDialog
import com.example.roal.utils.toolbarStyle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateWorkerActivity : AppCompatActivity() {

    var workersProvider = WorkersProvider()
    private var progressDialog: Dialog? = null
    lateinit var binding: ActivityUpdateWorkerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarStyle(this@UpdateWorkerActivity,binding.include.toolbar,"Actualización de datos")
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        gettingDataPerUser()
    }

    private fun gettingDataPerUser(){
        showLoading()
        val typeDNI = intent.getStringExtra("dni").toString()
        workersProvider.getWorkers(typeDNI)?.enqueue(object : Callback<Workers> {
            override fun onResponse(call: Call<Workers>, response: Response<Workers>) {
                if (response.body() != null) {
                    hideLoading()
                    val textIdentification = response.body()?.dni
                    val textName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = textName +"\r" + textLastName
                    val textArea = response.body()?.area
                    val textAllergies = response.body()?.allergies
                    val textPhone = response.body()?.phone
                    val textPhoto = response.body()?.photo
                    val textSecondaryPhone = response.body()?.phoneEmergency
                    val textDiseases = response.body()?.diseases
                    textPhoto.let {
                        Glide.with(this@UpdateWorkerActivity)
                            .load(textPhoto)
                            .apply(
                                RequestOptions.centerCropTransform()
                                    .placeholder(R.drawable.ic_baseline_supervised_user_circle_24)
                                    .error(R.drawable.ic_baseline_supervised_user_circle_24)
                                    .priority(Priority.HIGH)
                            )
                            .into(binding.imageViewUser)
                    }
                    binding.textName.text = textFullName
                    binding.textDNI.text = textIdentification
                    binding.textAllergies.setText(textAllergies)
                    binding.textPhone.setText(textPhone)
                    binding.textPhoneEmergency.setText(textSecondaryPhone)
                    binding.textAllergies.setText(textAllergies)
                    binding.textArea.setText(textArea)
                    binding.textIllness.setText(textDiseases)
                } else {
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<Workers>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@UpdateWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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
}