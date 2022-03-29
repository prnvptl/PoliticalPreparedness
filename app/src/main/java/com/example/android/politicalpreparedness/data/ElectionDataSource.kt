package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.models.Election

interface ElectionDataSource {

    suspend fun getUpcomingElections(): Result<List<Election>>
}