package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.database.ElectionDatabase
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.ElectionResponse
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ElectionDatabase.getInstance(application)
    private val apiService = CivicsApi.retrofitService
    private val asteroidsRepository = ElectionRepository(apiService, database.electionDao)


    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    init {
        viewModelScope.launch {
            val result = asteroidsRepository.getUpcomingElections()
            when(result) {
                is Result.Success<*> -> {
                    val elections = result.data as List<Election>
                    _upcomingElections.value = elections
                }
                is Result.Error -> {
                    // TODO: Do something with the error
                }
            }
        }
    }
//
    fun yay() {
        Log.i("SOMETAG", "upcomingElections.value?.size.toString())")
    }

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
}