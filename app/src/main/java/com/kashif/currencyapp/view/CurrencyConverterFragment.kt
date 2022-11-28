package com.kashif.currencyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kashif.currencyapp.common.showAlertDialog
import com.kashif.currencyapp.databinding.FragmentCurrencyConverterBinding
import com.kashif.currencyapp.presentation.CurrencyConverterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment() {

    private val viewModel: CurrencyConverterViewModel by viewModels()

    private var _binding: FragmentCurrencyConverterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCurrencyConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onFragmentCreated()

        viewModel.dialogCommand.observe(viewLifecycleOwner) { alertMessage ->
            showAlertDialog(message = alertMessage)
        }

        viewModel.viewState.observe(viewLifecycleOwner) { viewState ->
            println(viewState)
        }
    }
}
