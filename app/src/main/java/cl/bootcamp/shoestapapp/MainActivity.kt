package cl.bootcamp.shoestapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import cl.bootcamp.shoestapapp.navigation.NavGraph
import cl.bootcamp.shoestapapp.ui.theme.ShoesTapAppTheme
import cl.bootcamp.shoestapapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoesTapAppTheme {
                val navController = rememberNavController()  // Controlador de navegación
                val snackbarHostState =
                    remember { SnackbarHostState() }  // SnackbarHostState para las notificaciones

                // Llama directamente al NavGraph en lugar de AppScreen
                NavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}