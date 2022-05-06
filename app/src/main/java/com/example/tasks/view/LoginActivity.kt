package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.service.helper.FingerprintHelper
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializa eventos
        setListeners();

        observe()

        // mViewModel.isAuthenticationAvailable()
        verifyLoggedUser()

    }

    override fun onClick(view: View) {

        if (view.id == R.id.button_login) {
            handleLogin()
        } else if (view.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showAutentication() {
        //É preciso três coisas:

        //Executor = auxilia a resposta do fingerprint quando é processada ou não
        val executor: Executor = ContextCompat.getMainExecutor(this)

        //BiometricPrompt
        val biometricPrompt = BiometricPrompt(
            this@LoginActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                }
            })

        //BiometricPrompt INFO
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Título")
            .setSubtitle("Subtítulo")
            .setDescription("Descrição para o usuário")
            .setNegativeButtonText("Cancelar") //esse é obrigatório
            .build()

        biometricPrompt.authenticate(info) // como só mostrar a autenticação
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        binding.buttonLogin.setOnClickListener(this)
        binding.textRegister.setOnClickListener(this)
    }


    //Verifica se usuário está logado
    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }

    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                val message = it.faluire()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.loggedUser.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {

        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, getString(R.string.ERROR_UNEXPECTED), Toast.LENGTH_SHORT)
                .show()
        } else {
            mViewModel.doLogin(email, password)
        }

    }
}

