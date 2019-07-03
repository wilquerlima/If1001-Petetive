package br.ufpe.cin.petetive.util.controllers

interface RequestCallback {
    fun onSuccess(objects: Any)
    fun onError(msgError: String)
}