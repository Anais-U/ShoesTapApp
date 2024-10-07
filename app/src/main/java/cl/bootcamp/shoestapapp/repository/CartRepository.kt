package cl.bootcamp.shoestapapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import cl.bootcamp.shoestapapp.model.CartItem
import cl.bootcamp.shoestapapp.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("cart_prefs")

class CartRepository @Inject constructor(private val context: Context) {

    private val CART_ITEMS_KEY =
        stringPreferencesKey("cart_items") // Clave para almacenar los productos

    // Guardar carrito en DataStore
    suspend fun saveCartItems(cartItems: List<CartItem>) {
        val itemsAsString = cartItems.joinToString(";") {
            "${it.product.id},${it.selectedSize},${it.selectedColor},${it.product.price},${it.product.imageUrl},${it.product.name}"
        }
        context.dataStore.edit { preferences ->
            preferences[CART_ITEMS_KEY] = itemsAsString
        }
    }

    // Leer el carrito de DataStore
    val cartItemsFlow: Flow<List<CartItem>> = context.dataStore.data.map { preferences ->
        preferences[CART_ITEMS_KEY]?.let { itemsString ->
            itemsString.split(";").mapNotNull { item ->
                val parts = item.split(",")
                // Verificar si hay exactamente 6 partes
                if (parts.size == 6) {
                    val productId = parts[0]
                    val selectedSize = parts[1]
                    val selectedColor = parts[2]
                    val price = parts[3].toDoubleOrNull() ?: 0.0 // Maneja conversión a doble
                    val imageUrl = parts[4]
                    val name = parts[5]

                    CartItem(
                        product = Product(
                            id = productId,
                            name = name,
                            imageUrl = imageUrl,
                            price = price,
                            category = "", // Asigna la categoría según tu lógica
                            sizes = emptyList(), // Asigna las tallas según tu lógica
                            colors = emptyList(), // Asigna los colores según tu lógica
                            description = ""
                        ),
                        selectedSize = selectedSize,
                        selectedColor = selectedColor
                    )
                } else {
                    // Log para manejar errores de formato
                    null
                }
            }
        } ?: emptyList()
    }


    // Limpiar carrito
    suspend fun clearCart() {
        context.dataStore.edit { preferences ->
            preferences[CART_ITEMS_KEY] = "" // Vaciar el carrito
        }
    }
}
