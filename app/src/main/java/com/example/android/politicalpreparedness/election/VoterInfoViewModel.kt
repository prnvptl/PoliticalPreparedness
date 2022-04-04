package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.election.model.VoterInfo
import kotlinx.coroutines.launch
import timber.log.Timber


class VoterInfoViewModel(
    private val electionRepository: ElectionRepository,
    private val arg_election_id: Int,
    private val arg_division: Division
) : ViewModel() {

    //TODO: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfo?>(null)
    val voterInfo: LiveData<VoterInfo?> get() = _voterInfo

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs
    private val _urlToOpen = MutableLiveData<String?>(null)
    val urlToOpen: LiveData<String?> get() = _urlToOpen

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    init {
        getVoterInfo()
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            if (arg_division.state.isNotBlank()) {
                when (val result = electionRepository.getVoterInfoForElection(
                    "${arg_division.country}:${arg_division.state}",
                    arg_election_id
                )) {
                    is Result.Success<*> -> {
                        val voterInfo = result.data as VoterInfo
                        _voterInfo.value = voterInfo
                    }
                    is Result.Error -> {
                        // TODO: Do something with the error
                        Timber.i("VoterInfoViewModel api call some ERROR")
                    }
                }
            }
        }
    }

    fun openUrlLink(url: String) {
        _urlToOpen.value = url
    }

    fun openUrlDone() {
        _urlToOpen.value = null
    }

}