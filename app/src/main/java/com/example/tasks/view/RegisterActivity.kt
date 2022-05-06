package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.databinding.ActivityRegisterBinding
import com.example.tasks.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.button_save) {

            val name = edit_name.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()

            mViewModel.create(name, email, password)
        }
    }

    private fun observe() {
        mViewModel.create.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, it.faluire(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun listeners() {
        //button_save.setOnClickListener(this)
        binding.buttonSave.setOnClickListener(this)
    }
}
