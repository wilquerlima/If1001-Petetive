package br.ufpe.cin.petetive.controller

interface RequestCallback {
    fun onSuccess(objects: Any)
    fun onError(msgError: String)
}