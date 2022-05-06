package com.example.tasks.service.helper

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager

//a autenticação digital só acontece a partir da versão 23 do android, então por isso
//é necessario verificar qual versão o user tem, se ele tiver usa, se não não.
class FingerprintHelper {

    companion object {
        fun isAuthenticationAvaliable(context: Context): Boolean {

            //SDK_INT é a versão do dispositivo e versão do código 23 - M
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return false
            }

            val biometricManager: BiometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> return true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false //versão 23, mas o dispositivo não le digital
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false // queimou leitor, ou qualquer coisa que não esteja disponivel
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false //leitor de digital não configurado corretamente
            }
            return false
        }
    }
}