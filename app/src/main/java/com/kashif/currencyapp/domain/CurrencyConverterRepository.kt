package com.kashif.currencyapp.domain

import com.kashif.currencyapp.data.CurrencyConverterDataSource
import javax.inject.Inject

class CurrencyConverterRepository @Inject constructor(
    private val currencyConverterDataSource: CurrencyConverterDataSource
) {
    suspend fun fetchCurrencies() = currencyConverterDataSource.fetchCurrencies()

    suspend fun fetchExchangeRates(base: String, symbol: String) =
        currencyConverterDataSource.fetchExchangeRate(base, symbol)
}
