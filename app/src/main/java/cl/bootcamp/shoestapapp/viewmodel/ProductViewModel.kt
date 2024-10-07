package cl.bootcamp.shoestapapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.bootcamp.shoestapapp.model.CartItem
import cl.bootcamp.shoestapapp.model.Product
import cl.bootcamp.shoestapapp.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<List<Product>>(
        listOf(
            Product(
                id = "1",
                name = "Clasicos H&M",
                imageUrl = "https://i.pinimg.com/564x/bc/7f/8b/bc7f8b794919cbd3eb1c1a10a36cfb80.jpg",
                price = 24990.0,
                colors = listOf("Negro", "Blanco"), // Agregamos colores
                sizes = listOf("38", "39", "40"),  // Agregamos tallas
                category = "Zapatos",
                description = "Elegancia y comodidad con estos clásicos de siempre "
            ),
            Product(
                id = "2",
                name = "Converse plataforma",
                imageUrl = "https://i.pinimg.com/564x/e0/b9/6e/e0b96ef70c4c55cdc23c789728fedd13.jpg",
                price = 39990.0,
                colors = listOf("Negro", "Rojo"),
                sizes = listOf("37", "38", "39", "40"),
                category = "Zapatillas",
                description = "Todo el diseño clásico de Converse y la moda actual con plataforma"
            ),
            Product(
                id = "3",
                name = "Nike Revolution",
                imageUrl = "https://i.pinimg.com/564x/3a/60/cc/3a60ccfb4ecbb7b480f29ff5b5aaa2bc.jpg",
                price = 54990.0,
                colors = listOf("Blanco", "Negro"),
                sizes = listOf("40", "41", "42"),
                category = "Zapatillas",
                description = "Confort y diseño con las nuevas Nike Revolution"
            ),
            Product(
                id = "4",
                name = "Columbia Peakfreak",
                imageUrl = "https://i.pinimg.com/564x/43/41/d5/4341d50a97f711674537d4906b62af20.jpg",
                price = 84990.0,
                colors = listOf("Gris", "Negro"),
                sizes = listOf("40", "41", "42", "43"),
                category = "Zapatillas",
                description = "Perfectas para trekking , máximo confort "
            ),
            Product(
                id = "5",
                name = "Sandalias Columbia",
                imageUrl = "https://i.pinimg.com/564x/58/d6/2d/58d62d753e8e970dcc3348e993070359.jpg",
                price = 19990.0,
                colors = listOf("Negro", "Azul"),
                sizes = listOf("36", "37", "38"),
                category = "Sandalias",
                description = "Sandalias confortables para toda ocasión"
            ),
            Product(
                id = "6",
                name = "Botines plataforma",
                imageUrl = "https://i.pinimg.com/564x/ab/8d/15/ab8d15dd858925860b332c6ecb6896f4.jpg",
                price = 24990.0,
                colors = listOf("Negro", "Marrón"),
                sizes = listOf("38", "39", "40"),
                category = "Zapatos",
                description = "Botines con estilo y máximo confort "
            ),
            Product(
                id = "7",
                name = "Adidas Campus",
                imageUrl = "https://i.pinimg.com/564x/e4/e0/bd/e4e0bd19389f87886f644994725db2f5.jpg",
                price = 34990.0,
                colors = listOf("Blanco", "Verde"),
                sizes = listOf("39", "40", "41"),
                category = "Zapatillas",
                description = "Tus adidas de siempre con un diseño clásico y confortable "
            ),
            Product(
                id = "8",
                name = "Sandalias Zara",
                imageUrl = "https://i.pinimg.com/564x/39/1c/95/391c95c7c121007c366b450a85d03d3c.jpg",
                price = 39990.0,
                colors = listOf("Negro", "Beige"),
                sizes = listOf("36", "37", "38"),
                category = "Sandalias",
                description = "Sandalias elegantes para toda ocasión"
            )
        )
    )

    val productList: StateFlow<List<Product>> = _productList

    // Flujos para manejar el carrito
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems

    init {
        loadCartItems() // Carga los items del carrito desde DataStore

    }

    // Función para cargar los items del carrito desde DataStore
    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.cartItemsFlow.collect { loadedCartItems ->
                _cartItems.value = loadedCartItems // Asignar los items cargados al estado
            }
        }
    }

    // Función para agregar un producto al carrito
    fun addToCart(product: Product, selectedSize: String, selectedColor: String) {
        val newCartItem =
            CartItem(product = product, selectedSize = selectedSize, selectedColor = selectedColor)
        val updatedCart = _cartItems.value.toMutableList()
        updatedCart.add(newCartItem)
        _cartItems.value = updatedCart

        // Guardar el carrito actualizado en DataStore
        viewModelScope.launch {
            cartRepository.saveCartItems(updatedCart)
        }
    }

    // Función para eliminar un producto del carrito
    fun removeFromCart(cartItem: CartItem) {
        val updatedCart = _cartItems.value.toMutableList()
        updatedCart.remove(cartItem)
        _cartItems.value = updatedCart

        // Actualizar DataStore
        viewModelScope.launch {
            cartRepository.saveCartItems(updatedCart)
        }
    }

    // Función para limpiar el carrito
    fun clearCart() {
        _cartItems.value = emptyList()
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    // Favoritos
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> get() = _favorites

    // Función para alternar favorito
    fun toggleFavorite(productId: String) {
        val currentFavorites = _favorites.value.toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        _favorites.value = currentFavorites
    }

    // Verificar si un producto es favorito
    fun isFavorite(productId: String): Boolean {
        return _favorites.value.contains(productId)
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.product.price }
    }

}
