package cl.bootcamp.shoestapapp.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.bootcamp.shoestapapp.model.Product
import coil.compose.rememberAsyncImagePainter
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean,
    onAddToCartClick: (String, String) -> Unit, // Aceptamos tamaño y color como parámetros
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(250.dp)
            .padding(8.dp)
            .clickable { onProductClick(product) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)

    ) {
        // Botón "Favorito"
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color.Black else Color.Gray
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del producto
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium, // Estilo de texto equivalente
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            val priceFormatted =
                NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(product.price)
            Text(
                text = priceFormatted,
                style = MaterialTheme.typography.bodySmall, // Texto equivalente a "bodySmall"
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

        }

    }
}
