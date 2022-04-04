package com.example.android.politicalpreparedness

import android.app.Application
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModule = module {
            single<ElectionDao> { ElectionDatabase.getInstance(get()).electionDao }
            single<CivicsApiService> { CivicsApi.retrofitService }
            single { ElectionRepository(get(), get()) }
            viewModel { ElectionsViewModel(get()) }
            viewModel { (election: Election) ->
                VoterInfoViewModel(
                    get(),
                    election
                )
            }
        }
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule)
        }
        Timber.plant(Timber.DebugTree())
    }
}