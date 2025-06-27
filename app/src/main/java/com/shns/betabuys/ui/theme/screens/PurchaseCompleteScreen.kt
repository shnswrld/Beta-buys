package com.shns.betabuys.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseCompleteScreen(onReturnHome: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase Complete 🎉") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Thank you for your purchase!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onReturnHome) {
                Text("Return to Dashboard")
            }
        }
    }
}
