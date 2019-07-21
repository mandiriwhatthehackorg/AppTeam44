package id.co.bankmandiri.di.module

import androidx.room.Room
import id.co.bankmandiri.common.api.BankMandiriService
import id.co.bankmandiri.common.api.ServiceGenerator
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import id.co.bankmandiri.R

/**
 * @author hendrawd on 04 Jul 2019
 */
val appModule = module {
    // API service
    single { ServiceGenerator() }
    single { get<ServiceGenerator>().createService<BankMandiriService>() }

    // TODO App Database if needed
    // single {
    //     val dbName = "${androidApplication().getString(R.string.app_name)}.db"
    //     Room.databaseBuilder(
    //             androidApplication(),
    //             AppDatabase::class.java,
    //             dbName
    //     ).build()
    // }
}