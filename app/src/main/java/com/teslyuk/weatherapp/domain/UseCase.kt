package com.teslyuk.weatherapp.domain

abstract class UseCase<Q : Any, P : Any?> {

    var requestValue: Q? = null
    var responseValue: P? = null

    var useCaseCallback: UseCaseCallback<P>? = null

    internal fun run() {
        executeUseCase(requestValue)
    }

    internal fun run(value: Q?, callback: UseCaseCallback<P>?) {
        requestValue = value
        useCaseCallback = callback
        executeUseCase(requestValue)
    }

    protected abstract fun executeUseCase(requestValues: Q?)

    interface UseCaseCallback<R> {
        fun onSuccess(response: R)
        fun onError(t: Throwable)
    }
}