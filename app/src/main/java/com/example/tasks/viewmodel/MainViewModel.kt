package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val msharedPreferences = SecurityPreferences(application)

    //Para Observar na activity
    private val mUserName = MutableLiveData<String>()
    val userName: LiveData<String> = mUserName

    private val mLogout = MutableLiveData<Boolean>()
    val logout : LiveData<Boolean> = mLogout

    fun loadUserName() {
        mUserName.value = msharedPreferences.get(TaskConstants.SHARED.PERSON_NAME)

        // ou:  mUserName.value = msharedPreferences.get("personname")
    }

    fun logout() {
        //remover o que tem armazenado em sharedPreferences
        msharedPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        msharedPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        msharedPreferences.remove(TaskConstants.SHARED.PERSON_NAME)

        mLogout.value = true //daqui ela vai ser observada/ouvida através do LiveData lá pra Main Activity
    }
}