package com.example.baitaplab.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

class Lab7ExpenseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = AppDatabase.getInstance(applicationContext).expenseDao()
        setContent {
            MaterialTheme {
                val viewModel: ExpenseViewModel = viewModel(
                    factory = ExpenseViewModel.Factory(dao)
                )
                Lab7ExpenseScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lab7ExpenseScreen(viewModel: ExpenseViewModel) {
    val context = LocalContext.current
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    LaunchedEffect(expenses) {
        val dbFile = context.getDatabasePath("lab7_expenses.db")
        val extDir = context.getExternalFilesDir(null)
        if (dbFile.exists() && extDir != null) {
            listOf("", "-wal", "-shm").forEach { suffix ->
                val src = java.io.File(dbFile.absolutePath + suffix)
                if (src.exists()) {
                    src.copyTo(java.io.File(extDir, "lab7_expenses.db" + suffix), overwrite = true)
                }
            }
        }
    }
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quản lý khoản chi") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tiêu đề") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Số tiền") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Ghi chú") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    if (viewModel.add(title, amountText, note)) {
                        title = ""
                        amountText = ""
                        note = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thêm")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chưa có khoản chi")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(expenses, key = { it.id }) { expense ->
                        ExpenseItem(
                            expense = expense,
                            onDelete = { viewModel.remove(expense) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${expense.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (expense.note.isNotBlank()) {
                    Text(
                        text = expense.note,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            TextButton(onClick = onDelete) {
                Text("Xóa")
            }
        }
    }
}
