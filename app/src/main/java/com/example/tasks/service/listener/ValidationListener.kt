package com.example.tasks.service.listener

class   ValidationListener(string: String = "") {

    private var _status: Boolean = true
    private var _message: String = ""


    //quando já inicializa essa classe verifica se os valores mudaram
    init {
        if (string != "") {
            _status = false
            _message = string
        }
    }

    fun success() = _status

    fun faluire() = _message


}