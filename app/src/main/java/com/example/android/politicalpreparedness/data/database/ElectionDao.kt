package com.example.android.politicalpreparedness.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertElection(election: Election)

    @Query("select * from election_table")
    fun getElections(): LiveData<List<Election>>

    @Query("select * from election_table where id = :id")
    suspend fun getElectionById(id: Long): Election?

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("DELETE FROM election_table")
    suspend fun clearAllElections()
}