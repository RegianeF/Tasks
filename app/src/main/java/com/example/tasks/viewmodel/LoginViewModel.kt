package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.helper.FingerprintHelper
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _personRepository = PersonRepository(application)
    private val _priorityRepository = PriorityRepository(application)

    private val _sharedPreferences = SecurityPreferences(application)

    private val _login = MutableLiveData<ValidationListener>()
    val login: LiveData<ValidationListener> = _login

    private val _fingerprint = MutableLiveData<Boolean>()
    val fingerprint: LiveData<Boolean> = _fingerprint

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        _personRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {

                _sharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                _sharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                _sharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                RetrofitClient.addHeader(model.token, model.personKey)

                _login.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                _login.value = ValidationListener(message)
            }

        })
    }

    fun isAuthenticationAvailable() {
        if (FingerprintHelper.isAuthenticationAvaliable(getApplication())){
            _fingerprint.value
        }
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val token = _sharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = _sharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeader(token, person) //headers api

        val logged = (token != "" && person != "") //verifica se está logado

        if (!logged) {
            _priorityRepository.all()
        }
        _loggedUser.value = logged
    }

}