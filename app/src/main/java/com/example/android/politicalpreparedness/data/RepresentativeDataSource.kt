package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.representative.model.Representative

interface RepresentativeDataSource {
    suspend fun getMyRepresentatives(address: String): Result<List<Representative>>
}