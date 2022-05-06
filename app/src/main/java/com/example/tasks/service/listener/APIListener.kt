package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

interface APIListener<T> {

    //tipo genérico pra ser usado no código inteiro
    fun onSuccess(model: T)

    fun onFailure(message: String)

}