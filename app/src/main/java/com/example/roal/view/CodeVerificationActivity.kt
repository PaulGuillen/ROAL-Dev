package com.example.roal.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.roal.databinding.ActivityCodeVerificationBinding
import com.example.roal.models.MainUser
import com.example.roal.models.ResponseHttp
import com.example.roal.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utils.ChargeDialog

class CodeVerificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityCodeVerificationBinding
    var usersProvider = UsersProvider()
    var countDownTimer: CountDownTimer? = null
    var millisUntilFinished : Long? = null
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRecuperarContrasena.setOnClickListener { goToNewPassword() }
        binding.btnReenviarCorreo.setOnClickListener { sendingAgainEmail() }
        binding.btnBack.setOnClickListener { backView() }
        timerToShowView()
    }

    private fun sendingAgainEmail() {
        val email = intent.getStringExtra("email")
        val action = "recuperar_contraseña"
        val mainUser = MainUser(
            email = email,
            action = action
        )
        showLoading()
        usersProvider.recoveryPassword(mainUser)?.enqueue(object : Callback<ResponseHttp> {
            override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                if (response.body()?.code == 200) {
                    hideLoading()
                    binding.btnReenviarCorreo.visibility = View.GONE
                    binding.btnRecuperarContrasena.visibility = View.VISIBLE
                    timerToShowView()
                    Toast.makeText(this@CodeVerificationActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    hideLoading()
                    Toast.makeText(this@CodeVerificationActivity, "Los datos no son correctos", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                hideLoading()
                Toast.makeText(this@CodeVerificationActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun goToNewPassword() {
        val numberOne = binding.textNumberOne.text.toString()
        val numberTwo = binding.textNumberTwo.text.toString()
        val numberThree = binding.textNumberThree.text.toString()
        val numberFour = binding.textNumberFour.text.toString()
        val numberFive = binding.textNumberFive.text.toString()
        val fullNumber = "$numberOne$numberTwo$numberThree$numberFour$numberFive"
        val action = "code_verification"
        val email = intent.getStringExtra("email")

        val mainUser = MainUser(
            code = fullNumber.toInt(),
            email = email,
            action = action
        )
        if (isValidForm(numberOne, numberTwo, numberThree, numberFour, numberFive)) {
            showLoading()
            usersProvider.verificationCode(mainUser)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {
                    if (response.body()?.code == 200) {
                        hideLoading()
                        goToNewPasswordView()
                        Toast.makeText(this@CodeVerificationActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    } else {
                        hideLoading()
                        Toast.makeText(this@CodeVerificationActivity, "El código no es correcto", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(p0: Call<ResponseHttp>, t: Throwable) {
                    hideLoading()
                    Toast.makeText(this@CodeVerificationActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this@CodeVerificationActivity, "Datos no válidos", Toast.LENGTH_LONG).show()
        }
    }

    private fun hideLoading() {
        progressDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showLoading() {
        hideLoading()
        progressDialog = ChargeDialog.showLoadingDialog(this)
    }

    @SuppressLint("SetTextI18n")
    private fun timerToShowView(){
        millisUntilFinished = 60000
        binding.textTimer.text = "" + (millisUntilFinished?.div(1000)) + "s"
        val countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(time: Long) {
                binding.textTimer.text  = "Tiempo restante : " + (time / 1000) + "s"
            }
            override fun onFinish() {
                binding.btnReenviarCorreo.visibility = View.VISIBLE
                binding.btnRecuperarContrasena.visibility = View.GONE
            }
        }
        countDownTimer.start()
    }

    private fun isValidForm(
        numberOne: String,
        numberTwo: String,
        numberThree: String,
        numberFour: String,
        fullNumber: String
    ): Boolean {
        if (numberOne.isBlank()) {
            return false
        }
        if (numberTwo.isBlank()) {
            return false
        }
        if (numberThree.isBlank()) {
            return false
        }
        if (numberFour.isBlank()) {
            return false
        }
        if (fullNumber.isBlank()) {
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        countDownTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    private fun goToNewPasswordView() {
        val email = intent.getStringExtra("email")
        val i = Intent(this, NewPasswordActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        i.putExtra("email", email)
        startActivity(i)
    }

    private fun backView() {
        val i = Intent(this, ForgotPasswordActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        countDownTimer?.cancel()
        startActivity(i)
    }
}