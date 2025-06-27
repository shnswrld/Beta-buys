package com.shns.betabuys.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject

data class CartItem(
    val name: String = "",
    val price: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onNavigateToConfirmation: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val cartItems = remember { mutableStateListOf<CartItem>() }
    var listener: ListenerRegistration? = null

    DisposableEffect(Unit) {
        listener = db.collection("cart").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(context, "Error fetching cart", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            cartItems.clear()
            snapshot?.documents?.forEach { doc ->
                val item = doc.toObject<CartItem>()
                if (item != null) {
                    cartItems.add(item)
                }
            }
        }

        onDispose {
            listener?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart 🛒") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (cartItems.isEmpty()) {
                Text("Your cart is empty.", style = MaterialTheme.typography.bodyLarge)
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(cartItems.size) { index ->
                            val item = cartItems[index]
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text(item.price, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        db.collection("cart").get()
                            .addOnSuccessListener { snapshot ->
                                val batch = db.batch()
                                snapshot.documents.forEach { doc ->
                                    batch.delete(doc.reference)
                                }
                                batch.commit()
                                    .addOnSuccessListener {
                                        onNavigateToConfirmation()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Purchase failed", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to fetch cart", Toast.LENGTH_SHORT).show()
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Purchase Item(s)")
                }
            }
        }
    }
}
