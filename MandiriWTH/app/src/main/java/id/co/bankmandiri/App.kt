package id.co.bankmandiri

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import id.co.bankmandiri.di.module.*
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(), KoinComponent {

    companion object {
        lateinit var INSTANCE: App
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, viewModelModule, repositoryModule, useCaseModule, daoModule))
        }
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/MyriadProRegular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())
    }
}