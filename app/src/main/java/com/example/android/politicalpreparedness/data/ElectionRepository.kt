package com.example.android.politicalpreparedness.data

import android.util.Log
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

/**
 * Concrete implementation of a data source as a db & a network source as an api.
 *
 * The repository is implemented so that you can focus on only testing it.
 *
 * @param dao the dao that does the Room db operations
 * @param api the api service for network requests
 * @param ioDispatcher a coroutine dispatcher to offload the blocking IO tasks
 */
class ElectionRepository(
    private val api: CivicsApiService,
    private val dao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ElectionDataSource {
    override suspend fun getUpcomingElections(): Result<List<Election>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(api.getUpcomingElections().elections)
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
}