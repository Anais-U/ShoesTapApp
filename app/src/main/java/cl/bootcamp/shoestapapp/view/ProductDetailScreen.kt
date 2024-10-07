package cl.bootcamp.shoestapapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.bootcamp.shoestapapp.model.Product
import coil.compose.rememberAsyncImagePainter
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    initialIsFavorite: Boolean,
    onAddToCart: (Product, String, String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit,
    navController: NavController
) {

    var isFavorite by remember { mutableStateOf(initialIsFavorite) }
    var selectedColor by remember { mutableStateOf(product.colors.first()) }
    var selectedSize by remember { mutableStateOf(product.sizes.first()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White)
    ) {
        TopAppBar(
            title = { Text(product.name) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Image(
            painter = rememberAsyncImagePainter(product.imageUrl),
            contentDescription = product.name,
            contentScale = ContentScale.Inside,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = product.name, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))
        val priceFormatted =
            NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.price)
        Text(
            text = priceFormatted,
            style = MaterialTheme.typography.titleSmall,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorite",
            modifier = Modifier.clickable {
                isFavorite = !isFavorite
                onFavoriteClick(product.id, isFavorite)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = product.description, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona un color:")

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            product.colors.forEach { colorString ->
                ColorSelectionBox(colorString, selectedColor) {
                    selectedColor = colorString
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selecciona una talla:")

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            product.sizes.forEach { size ->
                SizeSelectionButton(size, selectedSize) {
                    selectedSize = size
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = { onAddToCart(product, selectedSize, selectedColor) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar al carrito")
        }
    }
}

@Composable
fun ColorSelectionBox(colorString: String, selectedColor: String, onClick: () -> Unit) {
    val color = colorFromName(colorString)

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color)
            .border(1.dp, if (selectedColor == colorString) Color.Red else Color.Black)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selectedColor == colorString) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SizeSelectionButton(size: String, selectedSize: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = if (selectedSize == size) MaterialTheme.colorScheme.primary else Color.LightGray),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(size)
    }
}

fun colorFromName(colorName: String): Color {
    return when (colorName) {
        "Rojo" -> Color.Red
        "Negro" -> Color.Black
        "Blanco" -> Color.White
        "Gris" -> Color.Gray
        "Azul" -> Color.Blue
        "Marrón" -> Color(0xFFA52A2A) // Marrón
        "Beige" -> Color(0xFFF5F5DC) // Beige
        "Verde" -> Color(0xFF008000) // Verde
        else -> Color.Transparent
    }
}
