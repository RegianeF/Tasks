package com.example.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository(context) {

    private val _remote = RetrofitClient.createService(PriorityService::class.java)
    private val _priorityDataBase = TaskDatabase.getDatabase(context).priorityDao()

    fun all() {
        if (!isConnectionAvailable(context)) {
            return
        }

        val call: Call<List<PriorityModel>> = _remote.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == 200) {
                    _priorityDataBase.clear()
                    response.body()?.let { _priorityDataBase.save(it) }
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {

            }
        })
    }

    fun list() = _priorityDataBase.list()

    fun getDescription(id: Int) = _priorityDataBase.getDescription(id)
}