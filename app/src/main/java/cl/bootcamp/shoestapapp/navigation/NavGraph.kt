package cl.bootcamp.shoestapapp.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.bootcamp.shoestapapp.view.CartScreen
import cl.bootcamp.shoestapapp.view.MainScreen
import cl.bootcamp.shoestapapp.view.ProductDetailScreen
import cl.bootcamp.shoestapapp.viewmodel.ProductViewModel
import android.net.Uri
import androidx.compose.material3.SnackbarHostState

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ProductDetailScreen : Screen("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/${Uri.encode(productId)}"
    }

    data object CartScreen : Screen("cart_screen")

}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: ProductViewModel,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        // Pantalla principal
        composable(Screen.MainScreen.route) {
            MainScreen(
                viewModel = viewModel,
                navController = navController,
                snackbarHostState = snackbarHostState,
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetailScreen.createRoute(productId))
                }
            )
        }

        // Pantalla de detalles del producto
        composable(Screen.ProductDetailScreen.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val productList by viewModel.productList.collectAsState()
            val product = productList.find { it.id == productId }

            if (product != null) {
                ProductDetailScreen(
                    product = product,
                    initialIsFavorite = viewModel.isFavorite(product.id),
                    onAddToCart = { product, selectedSize, selectedColor ->
                        viewModel.addToCart(product, selectedSize, selectedColor)
                    },
                    onFavoriteClick = { productId, _ -> viewModel.toggleFavorite(productId) },
                    navController = navController
                )
            } else {
                Text("Producto no encontrado")
            }
        }

        // Pantalla del carrito de compras
        composable(Screen.CartScreen.route) {
            val cartItems by viewModel.cartItems.collectAsState()

            CartScreen(
                cartItems = cartItems,
                onRemoveFromCart = { cartItem -> viewModel.removeFromCart(cartItem) },
                onClearCart = { viewModel.clearCart() },
                navController = navController
            )
        }


    }
}
