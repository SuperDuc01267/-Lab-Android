package com.example.baitaplab.lab7

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {
    val expenses = dao.observeAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList(),
    )

    fun add(title: String, amountText: String, note: String): Boolean {
        val amount = amountText.toDoubleOrNull() ?: return false
        if (title.trim().isEmpty() || amount <= 0.0) return false
        viewModelScope.launch {
            dao.insert(
                Expense(
                    title = title.trim(),
                    amount = amount,
                    note = note.trim(),
                )
            )
        }
        return true
    }

    fun remove(expense: Expense) {
        viewModelScope.launch { dao.delete(expense) }
    }

    class Factory(private val dao: ExpenseDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ExpenseViewModel(dao) as T
    }
}
