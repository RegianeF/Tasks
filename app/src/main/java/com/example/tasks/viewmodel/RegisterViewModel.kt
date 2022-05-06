package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val _personRepository = PersonRepository(application)
    private val _sharedPreferences = SecurityPreferences(application)

    private val _create = MutableLiveData<ValidationListener>()
    val create: LiveData<ValidationListener> = _create


    fun create(name: String, email: String, password: String) {
        _personRepository.create(name, email, password, object: APIListener<HeaderModel>{
            override fun onSuccess(model: HeaderModel) {
                _sharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                _sharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                _sharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                _create.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                _create.value = ValidationListener(message)
            }

        })
    }

}