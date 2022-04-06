package com.example.android.politicalpreparedness.data

import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativeRepository(
    private val api: CivicsApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepresentativeDataSource {
    override suspend fun getMyRepresentatives(address: String): Result<List<Representative>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = api.getMyRepresentatives(address)
                val representatives = mutableListOf<Representative>()
                response.offices.map { representatives.addAll(it.getRepresentatives(response.officials)) }
                Result.Success(representatives)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }
}