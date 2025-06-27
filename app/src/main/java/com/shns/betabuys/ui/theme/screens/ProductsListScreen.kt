package com.shns.betabuys.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

data class Product(val name: String, val price: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val products = listOf(
        Product("Floral Summer Dress", "KES 3,500"),
        Product("Gold Hoop Earrings", "KES 1,200"),
        Product("Mini Shoulder Bag", "KES 2,800"),
        Product("Pink Platform Heels", "KES 4,500"),
        Product("Scented Lip Gloss", "KES 950"),
        Product("Silk Scrunchies Set", "KES 700"),
        Product("Butterfly Necklace", "KES 1,800"),
        Product("Heart Print Pajamas", "KES 2,200")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beta Buys 🛒") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products.size) { index ->
                val product = products[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text(product.price, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val cartItem = hashMapOf(
                                    "name" to product.name,
                                    "price" to product.price,
                                    "timestamp" to System.currentTimeMillis()
                                )

                                db.collection("cart")
                                    .add(cartItem)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Failed to add ${product.name}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        ) {
                            Text("Add to Cart")
                        }
                    }
                }
            }
        }
    }
}
