package com.example.android.politicalpreparedness.data.network.jsonadapter

import com.example.android.politicalpreparedness.data.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson(ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter, "")
            .substringBefore("/")
        var state = ocdDivisionId.substringAfter(stateDelimiter, "")
            .substringBefore("/")
        if (state.isBlank()) {
            val disctrictDelimiter = "district:"
            state = ocdDivisionId.substringAfter(disctrictDelimiter, "")
                .substringBefore("/")
        }
        return Division(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson(division: Division): String {
        return division.id
    }
}