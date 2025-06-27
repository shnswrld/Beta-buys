package com.shns.betabuys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import com.shns.betabuys.ui.theme.screens.CartScreen
import com.shns.betabuys.ui.theme.screens.DashboardScreen
import com.shns.betabuys.ui.theme.screens.ProductListScreen
import com.shns.betabuys.ui.theme.screens.PurchaseCompleteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "dashboard") {

                composable("dashboard") {
                    DashboardScreen(
                        onNavigateToProducts = {
                            navController.navigate("products")
                        }
                    )
                }

                composable("products") {
                    ProductListScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToCart = { navController.navigate("cart") }
                    )
                }

                composable("cart") {
                    CartScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToConfirmation = {
                            navController.navigate("confirmation") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    )
                }

                composable("confirmation") {
                    PurchaseCompleteScreen(
                        onReturnHome = {
                            navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    )
                }


            }
            }
        }
    }

