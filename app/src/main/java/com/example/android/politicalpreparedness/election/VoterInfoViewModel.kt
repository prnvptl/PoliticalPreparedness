package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.model.VoterInfo
import kotlinx.coroutines.launch
import timber.log.Timber


class VoterInfoViewModel(
    private val electionRepository: ElectionRepository,
    private val election: Election
) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfo?>(null)
    val voterInfo: LiveData<VoterInfo?> get() = _voterInfo

    private val _urlToOpen = MutableLiveData<String?>(null)
    val urlToOpen: LiveData<String?> get() = _urlToOpen

    private val _isFollowing = MutableLiveData<Boolean>(false)
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    init {
        getVoterInfo()
        setIsFollowing()
    }

    private fun getVoterInfo() {
        viewModelScope.launch {
            if (election.division.state.isNotBlank()) {
                when (val result = electionRepository.getVoterInfoForElection(
                    "${election.division.country}:${election.division.state}",
                    election.id
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

    private fun setIsFollowing() {
        viewModelScope.launch {
            val election = electionRepository.getElectionById(election.id)
            _isFollowing.value = election != null
        }
    }

    fun openUrlLink(url: String) {
        _urlToOpen.value = url
    }

    fun openUrlDone() {
        _urlToOpen.value = null
    }

    fun onFollowClicked() {
        viewModelScope.launch {
            if (_isFollowing.value == false) {
                electionRepository.followElection(election)
            } else {
                electionRepository.unfollowElection(election)
            }
            setIsFollowing()
        }
    }
}