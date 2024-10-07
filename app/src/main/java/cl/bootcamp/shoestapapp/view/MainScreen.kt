package cl.bootcamp.shoestapapp.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cl.bootcamp.shoestapapp.components.ProductCard
import cl.bootcamp.shoestapapp.model.Product
import cl.bootcamp.shoestapapp.viewmodel.ProductViewModel

@Composable
fun MainScreen(
    viewModel: ProductViewModel,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    onProductClick: (String) -> Unit

) {
    val productList = viewModel.productList.collectAsState().value
    val favorites = viewModel.favorites.collectAsState().value

    // Filtrar productos por categorías
    val zapatillas = productList.filter { it.category == "Zapatillas" }
    val zapatos = productList.filter { it.category == "Zapatos" }
    val sandalias = productList.filter { it.category == "Sandalias" }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            AppTopBar()
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Sección deslizante horizontal de una categoría
            fun categorySection(
                title: String,
                products: List<Product>
            ) {
                item {
                    Column {
                        // Título de la categoría
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )

                        // LazyRow para mostrar los productos de la categoría de manera horizontal
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
                        ) {
                            items(products) { product ->
                                var showSnackbar by remember { mutableStateOf(false) }

                                ProductCard(
                                    product = product,
                                    onProductClick = { onProductClick(product.id) },
                                    onFavoriteClick = { viewModel.toggleFavorite(product.id) },
                                    isFavorite = favorites.contains(product.id), // Cambia solo el color del ícono
                                    onAddToCartClick = { size, color ->
                                        viewModel.addToCart(product, size, color)
                                        showSnackbar = true
                                    },
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .width(200.dp) // Tamaño personalizado para que las tarjetas se vean bien en horizontal
                                )

                                // Mostrar Snackbar al agregar al carrito
                                if (showSnackbar) {
                                    LaunchedEffect(Unit) {
                                        snackbarHostState.showSnackbar("¡Agregado a tu carrito!")
                                        showSnackbar = false
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Mostrar las secciones de productos por categorías
            categorySection("Zapatillas", zapatillas)
            categorySection("Zapatos", zapatos)
            categorySection("Sandalias", sandalias)
        }
    }
}