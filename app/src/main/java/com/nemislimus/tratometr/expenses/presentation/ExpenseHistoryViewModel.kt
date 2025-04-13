package com.nemislimus.tratometr.expenses.presentation

import androidx.lifecycle.ViewModel
import com.nemislimus.tratometr.expenses.domain.api.ExpenseHistoryInteractor
import javax.inject.Inject

class ExpenseHistoryViewModel@Inject constructor(
    private val interactor: ExpenseHistoryInteractor
): ViewModel() {

}