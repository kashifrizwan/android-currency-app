package com.kashif.currencyapp.model

sealed class CurrenciesDataModel {
    data class Currencies(val currencies: HashMap<String, String>): CurrenciesDataModel()
    data class ExchangeRates(val rates: HashMap<String, Double>): CurrenciesDataModel()
    data class ApiError(val message: String) : CurrenciesDataModel()
    object NetworkException : CurrenciesDataModel()
}
