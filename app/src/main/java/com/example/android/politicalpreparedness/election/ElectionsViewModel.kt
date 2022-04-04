package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val electionsRepository: ElectionRepository
) : ViewModel() {
//    private val database = ElectionDatabase.getInstance(application)
//    private val apiService = CivicsApi.retrofitService
//    private val asteroidsRepository = ElectionRepository(apiService, database.electionDao)


    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _navigateToVoterInfo = MutableLiveData<Election?>(null)
    val navigateToVoterInfo: LiveData<Election?> get() = _navigateToVoterInfo

    init {
        getElections()
    }

    private fun getElections() {
        viewModelScope.launch {
            when (val result = electionsRepository.getUpcomingElections()) {
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

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfoWith(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun navigateToVoterInfoDone() {
        _navigateToVoterInfo.value = null
    }
}