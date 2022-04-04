package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.CivicsApiService
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.election.model.VoterInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
) : ElectionDataSource {
    override suspend fun getUpcomingElections(): Result<List<Election>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(api.getUpcomingElections().elections)
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
        }

    override fun getSavedElections(): LiveData<List<Election>> = dao.getElections()

    override suspend fun getVoterInfoForElection(
        address: String,
        electionId: Long
    ): Result<VoterInfo> = withContext(ioDispatcher) {
        return@withContext try {
            val response = api.getVoterInfoWithElection(address, electionId)
            val electionAdmin = response.state?.first()?.electionAdministrationBody
            Result.Success(
                VoterInfo(
                    response.election.name,
                    electionAdmin?.ballotInfoUrl ?: "",
                    electionAdmin?.votingLocationFinderUrl ?: "",
                    response.election.electionDay.toString(),
                    electionAdmin?.name ?: "",
                    electionAdmin?.correspondenceAddress?.toFormattedString() ?: ""
                )
            )
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun followElection(election: Election) = withContext(ioDispatcher) {
        dao.insertElection(election)
    }

    override suspend fun getElectionById(id: Long): Election? = withContext(ioDispatcher) {
        return@withContext dao.getElectionById(id)
    }

    override suspend fun unfollowElection(eleciton: Election) = withContext(ioDispatcher) {
        return@withContext dao.deleteElection(eleciton)
    }

}