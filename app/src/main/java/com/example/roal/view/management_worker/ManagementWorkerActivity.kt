package com.example.roal.view.management_worker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import com.example.roal.R
import com.example.roal.databinding.ActivityManagementWorkerBinding
import com.example.roal.models.Workers
import com.example.roal.providers.WorkersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.ChargeDialog

@SuppressLint("SourceLockedOrientationActivity")
class ManagementWorkerActivity : AppCompatActivity() {

    lateinit var binding: ActivityManagementWorkerBinding
    var workersProvider = WorkersProvider()
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        binding.include.toolbar.setTitleTextAppearance(this, R.style.titulosNavbar)
        binding.include.toolbar.title = "Mantenimiento de trabajadores"
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fabWorkerCreate.setOnClickListener { goToWorkerCreate() }
        binding.btnDelete.setOnClickListener { deleteWorker() }
        searchWorkers()
    }

    private fun deleteWorker() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.title_delete_data))
            .setContentText(getString(R.string.subtitle_delete_data))
            .setCancelText("No").setCancelClickListener { obj: SweetAlertDialog -> obj.dismiss() }
            .setConfirmText("Si")
            .setConfirmClickListener {
                deleteWorkers()
                it.dismiss()
            }.show()
    }

    private fun deleteWorkers() {

        val actualIdentification = binding.textViewDNI.text.toString()
        showLoading()
        workersProvider.deleteWorker(actualIdentification)?.enqueue(object : Callback<Workers> {
            override fun onResponse(call: Call<Workers>, response: Response<Workers>) {
                hideLoading()
                binding.textNoData.visibility = View.VISIBLE
                binding.cardViewWorker.visibility = View.GONE
                SweetAlertDialog(this@ManagementWorkerActivity, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.successful_delete_data)).show()
            }
            override fun onFailure(call: Call<Workers>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@ManagementWorkerActivity, "Error : $t", Toast.LENGTH_LONG).show()
            }

        })

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

    private fun goToWorkerCreate() {
        val i = Intent(this, CreateWorkerActivity::class.java)
        startActivity(i)
    }

    private fun getWorkers() {
        showLoading()
        val dni = binding.searchBox.text.toString()
        workersProvider.getWorkers(dni)?.enqueue(object : Callback<Workers> {
            override fun onResponse(call: Call<Workers>, response: Response<Workers>) {
                if (response.body() != null) {
                    hideLoading()
                    binding.textNoData.visibility = View.GONE
                    binding.cardViewWorker.visibility = View.VISIBLE
                    val textIdentification = response.body()?.dni
                    val textName = response.body()?.name
                    val textLastName = response.body()?.lastname
                    val textFullName = "$textName $textLastName"
                    val textArea = response.body()?.area
                    val textDateBirth = response.body()?.dateBirth
                    val textJoinDate = response.body()?.dateJoin
                    val textPhone = response.body()?.phone
                    val textPhoto = response.body()?.photo
                    textPhoto.let {
                        Glide.with(this@ManagementWorkerActivity)
                            .load(textPhoto)
                            .apply(
                                centerCropTransform()
                                    .placeholder(R.drawable.ic_baseline_supervised_user_circle_24)
                                    .error(R.drawable.ic_baseline_supervised_user_circle_24)
                                    .priority(Priority.HIGH)
                            )
                            .into(binding.imageWorker)
                    }

                    binding.textViewDNI.text = textIdentification
                    binding.textViewNomCompleto.text = textFullName
                    binding.textArea.text = textArea
                    binding.textbornDate.text = textDateBirth
                    binding.textdateJoin.text = textJoinDate
                    binding.textPhone.text = textPhone
                    resetStatus()
                } else {
                    hideLoading()
                    Toast.makeText(this@ManagementWorkerActivity, "No data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Workers>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@ManagementWorkerActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

    private fun resetStatus() {
        binding.searchBox.setText("")
    }

    private fun performSearch() {
        binding.searchBox.clearFocus()
        val `in`: InputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.searchBox.windowToken, 0)
    }
}