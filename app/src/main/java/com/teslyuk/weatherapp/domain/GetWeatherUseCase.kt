package com.teslyuk.weatherapp.domain

import com.teslyuk.weatherapp.data.IWeatherDataSource
import com.teslyuk.weatherapp.data.WeatherRepository
import com.teslyuk.weatherapp.data.model.Weather
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class GetWeatherUseCase(private val repository: WeatherRepository) :
    UseCase<String, List<Weather>>() {
    override fun executeUseCase(requestValues: String?) {
        repository.getWeather(requestValues!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<IWeatherDataSource.WeatherData> {
                override fun onSuccess(t: IWeatherDataSource.WeatherData) {
                    responseValue = t.data
                    useCaseCallback?.onSuccess(t.data)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    useCaseCallback?.onError(Throwable("Data not found"))
                }
            })

    }
}