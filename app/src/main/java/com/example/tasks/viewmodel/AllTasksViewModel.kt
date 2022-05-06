package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val _taskRepository = TaskRepository(application)

    private val _validation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = _validation

    private val _list = MutableLiveData<List<TaskModel>>()
    val taskList: LiveData<List<TaskModel>> = _list

    private var mTaskFilter = 0

    fun list(taskFilter: Int) {
        mTaskFilter = taskFilter

        val listener = object : APIListener<List<TaskModel>> {
            override fun onSuccess(model: List<TaskModel>) {
                _list.value = model
            }

            override fun onFailure(message: String) {
                _list.value = arrayListOf() //se der erro deixar um array vazio de proposito
                _validation.value = ValidationListener(message)
            }
        }

        if (mTaskFilter == TaskConstants.FILTER.ALL) {
            _taskRepository.all(listener)
        } else if (mTaskFilter == TaskConstants.FILTER.NEXT) {
            _taskRepository.nextWeek(listener)
        } else {
            _taskRepository.overdue(listener)
        }

    }

    fun complete(id: Int) {
        updateStatus(id, true)
    }

    fun undo(id: Int) {
        updateStatus(id, false)
    }

    //clicar em cima da descrição
    fun delete(id: Int) {
        _taskRepository.delete(id, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mTaskFilter)
                _validation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                _validation.value = ValidationListener(message)
            }
        })
    }

    private fun updateStatus(id: Int, complete: Boolean) {
        _taskRepository.updateStatus(id, complete, object : APIListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                list(mTaskFilter)
            }

            override fun onFailure(message: String) {
            }
        })
    }
}
