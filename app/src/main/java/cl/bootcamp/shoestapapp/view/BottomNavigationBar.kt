package cl.bootcamp.shoestapapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import cl.bootcamp.shoestapapp.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        modifier = Modifier
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        val items = listOf(
            Pair(Icons.Default.Home, "Inicio"),
            Pair(Icons.Default.ShoppingCart, "Carrito")
        )

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.first, contentDescription = item.second) },
                label = { Text(item.second) },
                selected = false, // Aquí podrías manejar el estado de selección
                onClick = {
                    when (item.second) {
                        "Inicio" -> navController.navigate(Screen.MainScreen.route)
                        "Carrito" -> navController.navigate(Screen.CartScreen.route)
                    }
                },
                selectedContentColor = Color.Black, // Color para el ícono y texto seleccionado
                unselectedContentColor = Color.Gray, // Color para el ícono y texto no seleccionado
                modifier = Modifier.background(Color.White)
            )
        }
    }
}