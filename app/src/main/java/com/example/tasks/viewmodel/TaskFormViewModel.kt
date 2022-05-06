package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _validation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = _validation

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    fun listPriorities() {
        _priorityList.value = mPriorityRepository.list()
    }

    fun save(task: TaskModel) {
        //repositorio
        if (task.id == 0) {
            mTaskRepository.create(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    _validation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    _validation.value = ValidationListener(message)
                }
            })
        } else {
            mTaskRepository.update(task, object : APIListener<Boolean> {
                override fun onSuccess(model: Boolean) {
                    _validation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    _validation.value = ValidationListener(message)
                }
            })
        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIListener<TaskModel> {
            override fun onSuccess(model: TaskModel) {
                _task.value = model //dessa maneira está passando o valor do task para a activity
            }

            override fun onFailure(message: String) {
                _validation.value = ValidationListener(message)
            }

        })
    }

}