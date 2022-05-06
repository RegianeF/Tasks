package com.example.tasks.service.model

import com.google.gson.annotations.SerializedName

class HeaderModel {
    //tipo de retorno
    @SerializedName("token")
    var token : String = ""

    @SerializedName("personKey")
    var personKey : String = ""

    @SerializedName("name")
    var name : String = ""

}