package cl.bootcamp.shoestapapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.bootcamp.shoestapapp.model.CartItem
import cl.bootcamp.shoestapapp.model.Product
import coil.compose.rememberAsyncImagePainter
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    onRemoveFromCart: (CartItem) -> Unit,
    onClearCart: () -> Unit,
    navController: NavController

) {
    val snackHostState = remember { SnackbarHostState() }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartActions(
                    totalPrice = calculateTotalPrice(cartItems), // Calcular precio total
                    onClearCart = onClearCart
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackHostState)

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    text = "El carrito está vacío",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemDisplay(
                            cartItem = cartItem,
                            onRemoveFromCart = onRemoveFromCart
                        )
                    }
                }
            }
        }
    }
}

// Función para calcular el precio total
private fun calculateTotalPrice(cartItems: List<CartItem>): Double {
    return cartItems.sumOf { it.product.price }
}

@Composable
fun CartItemDisplay(
    cartItem: CartItem,
    onRemoveFromCart: (CartItem) -> Unit
) {
    val product = cartItem.product // Obtenemos el producto del CartItem

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                // Carga de imagen usando Coil
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = product.name,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre del producto
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)

                // Mostrar talla y color seleccionados
                Text(
                    text = "Talla: ${cartItem.selectedSize}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Color: ${cartItem.selectedColor}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                val priceFormatted =
                    NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.price)
                Text(
                    text = priceFormatted,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Icono de eliminar producto
            IconButton(
                onClick = { onRemoveFromCart(cartItem) },
                modifier = Modifier
                    .align(Alignment.TopEnd) // Alineación en la parte superior derecha
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun CartActions(
    totalPrice: Double,
    onClearCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(47.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar el precio total
        val totalPriceFormatted =
            NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(totalPrice)
        Text(
            text = "Total: $totalPriceFormatted",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Button(
            onClick = onClearCart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vaciar Carrito")
        }
    }
}