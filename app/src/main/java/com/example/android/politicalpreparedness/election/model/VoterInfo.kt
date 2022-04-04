package com.example.android.politicalpreparedness.election.model

data class VoterInfo(
    val name: String,
    val ballotInfoUrl: String,
    val votingLocationInfoUrl: String,
    val date: String,
    val stateHeader: String,
    val address: String
)