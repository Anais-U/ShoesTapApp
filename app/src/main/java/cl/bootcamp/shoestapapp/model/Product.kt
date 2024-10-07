package cl.bootcamp.shoestapapp.model


data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val category: String,
    val sizes: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val description: String


)
