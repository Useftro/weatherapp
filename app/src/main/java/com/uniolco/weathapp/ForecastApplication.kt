package com.uniolco.weathapp

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.uniolco.weathapp.data.db.ForecastDatabase
import com.uniolco.weathapp.data.network.*
import com.uniolco.weathapp.data.provider.LocationProvider
import com.uniolco.weathapp.data.provider.LocationProviderImpl
import com.uniolco.weathapp.data.repository.ForecastRepository
import com.uniolco.weathapp.data.repository.ForecastRepositoryImpl
import com.uniolco.weathapp.ui.favorite.list.FavoriteListWeatherViewModelFactory
import com.uniolco.weathapp.ui.favorite.detail.FavoriteDetailWeatherViewModelFactory
import com.uniolco.weathapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.uniolco.weathapp.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import com.uniolco.weathapp.ui.weather.future.list.FutureListWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*
import org.threeten.bp.LocalDate

class ForecastApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastDatabase(instance()) }
        bind() from singleton { instance<ForecastDatabase>().currentWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().weatherLocationDao() }
        bind() from singleton { instance<ForecastDatabase>().futureWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().favoriteWeatherDao() }
        bind() from singleton { instance<ForecastDatabase>().userDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiWeatherService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
        // doesn't need to be a singleton, then we can straight use provider
        bind() from provider { CurrentWeatherViewModelFactory(instance()) } // each time creating a new instance of Factory
        bind() from provider { FutureListWeatherViewModelFactory(instance()) } // each time creating a new instance of Facto
        bind() from provider { FavoriteListWeatherViewModelFactory(instance()) }
//        bind() from provider { FavoriteDetailWeatherViewModelFactory(instance())}
        bind() from factory { detailDate: LocalDate -> FutureDetailWeatherViewModelFactory(detailDate, instance()) }
        bind() from factory { location: String -> FavoriteDetailWeatherViewModelFactory(location, instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this) // using ThreeTen because of Java 8 which is not
        // good to use because of new java.time
    }

}