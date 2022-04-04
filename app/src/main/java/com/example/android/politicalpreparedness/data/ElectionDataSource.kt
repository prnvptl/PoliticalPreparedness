package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.model.VoterInfo

interface ElectionDataSource {

    suspend fun getUpcomingElections(): Result<List<Election>>

    fun getSavedElections(): LiveData<List<Election>>

    suspend fun getVoterInfoForElection(
        address: String,
        electionId: Long
    ): Result<VoterInfo>

    suspend fun followElection(election: Election)

    suspend fun getElectionById(id: Long): Election?

    suspend fun unfollowElection(eleciton: Election)


}