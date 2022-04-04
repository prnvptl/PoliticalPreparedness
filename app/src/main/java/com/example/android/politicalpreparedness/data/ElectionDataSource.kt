package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.model.VoterInfo

interface ElectionDataSource {

    suspend fun getUpcomingElections(): Result<List<Election>>

    suspend fun getVoterInfoForElection(
        address: String,
        electionId: Int
    ): Result<VoterInfo>
}