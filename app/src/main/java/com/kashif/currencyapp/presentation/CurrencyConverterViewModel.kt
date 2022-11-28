package com.kashif.currencyapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.currencyapp.domain.CurrencyConverterRepository
import com.kashif.currencyapp.model.CurrenciesDataModel.ApiError
import com.kashif.currencyapp.model.CurrenciesDataModel.Currencies
import com.kashif.currencyapp.model.CurrenciesDataModel.NetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val currencyConverterRepository: CurrencyConverterRepository
) : ViewModel() {

    val dialogCommand: MutableLiveData<String> = MutableLiveData()
    val viewState: MutableLiveData<CurrencyConverterViewState> = MutableLiveData()

    fun onFragmentCreated() {
        viewModelScope.launch {
            when (val response = currencyConverterRepository.fetchCurrencies()) {
                is Currencies -> {
                    val currentViewState = viewState.value ?: CurrencyConverterViewState()
                    val updatedViewState = currentViewState.copy(
                        availableCurrencies = response.currencies.values.toList().sorted()
                    )
                    viewState.postValue(updatedViewState)
                }
                is ApiError -> dialogCommand.postValue(response.message)
                NetworkException -> dialogCommand.postValue(
                    "Network error. Please check your internet connection."
                )
            }
        }
    }
}
