package com.kashif.currencyapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kashif.currencyapp.domain.CurrencyConverterRepository
import com.kashif.currencyapp.model.CurrenciesDataModel.ApiError
import com.kashif.currencyapp.model.CurrenciesDataModel.Currencies
import com.kashif.currencyapp.model.CurrenciesDataModel.ExchangeRates
import com.kashif.currencyapp.model.CurrenciesDataModel.NetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val currencyConverterRepository: CurrencyConverterRepository
) : ViewModel() {

    val toAmount: MutableLiveData<Double> = MutableLiveData()
    val dialogCommand: MutableLiveData<String> = MutableLiveData()
    val currentExchangeRate: MutableLiveData<Double> = MutableLiveData()
    val availableCurrenciesList: MutableLiveData<List<String>> = MutableLiveData()

    fun onFragmentCreated() {
        viewModelScope.launch {
            when (val response = currencyConverterRepository.fetchCurrencies()) {
                is Currencies -> availableCurrenciesList.postValue(
                    response.currencies.keys.toList().sorted()
                )
                is ApiError -> dialogCommand.postValue(response.message)
                NetworkException -> dialogCommand.postValue(
                    "Network error. Please check your internet connection."
                )
                is ExchangeRates -> Unit
            }
        }
    }

    fun onCurrencyChangeAction(baseCurrency: String, toCurrency: String) {
        viewModelScope.launch {
            when (val response = currencyConverterRepository.fetchExchangeRates(baseCurrency, toCurrency)) {
                is ExchangeRates -> updateViewStateAfterCurrencyChange(
                    exchangeRate = response.rates[toCurrency] ?: 1.0
                )
                is ApiError -> dialogCommand.postValue(response.message)
                NetworkException -> dialogCommand.postValue(
                    "Network error. Please check your internet connection."
                )
                is Currencies -> Unit
            }
        }
    }

    fun onBaseAmountChanged(baseAmount: Double) {
        val exchangeRate = currentExchangeRate.value ?: 1.0
        toAmount.postValue(
            String.format("%.3f", baseAmount * exchangeRate).toDouble()
        )
    }

    private fun updateViewStateAfterCurrencyChange(exchangeRate: Double) {
        currentExchangeRate.postValue(exchangeRate)
    }
}
