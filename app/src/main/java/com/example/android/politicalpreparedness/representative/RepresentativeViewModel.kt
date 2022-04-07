package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.RepresentativeRepository
import com.example.android.politicalpreparedness.data.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel(
    private val representativeRepository: RepresentativeRepository
) : ViewModel() {

    private val _myRepresentatives = MutableLiveData<List<Representative>>()
    val myRepresentatives: LiveData<List<Representative>> get() = _myRepresentatives

    private val _searchResultsError = MutableLiveData<Boolean>(false)
    val searchResultsError: LiveData<Boolean> get() = _searchResultsError


    private fun getMyRepresentativesBy(address: String) {
        viewModelScope.launch {
            when (val result = representativeRepository.getMyRepresentatives(address)) {
                is Result.Success<*> -> {
                    val reps = result.data as List<Representative>
                    _myRepresentatives.value = reps
                }
                is Result.Error -> {
                    _searchResultsError.value = true
                    Timber.i("Show toast to user error")
                }
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */


    fun fetchMyRepresentativesByFields(
        address1: String,
        address2: String,
        city: String,
        state: String,
        zip: String
    ) {
        getMyRepresentativesBy("${address1} ${address2} ${city}, ${state} ${zip}")
    }

    fun searchResultsErrorShown() {
        _searchResultsError.value = false
    }

}
