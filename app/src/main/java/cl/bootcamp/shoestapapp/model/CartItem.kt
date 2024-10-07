package cl.bootcamp.shoestapapp.model

data class CartItem(
    val product: Product,
    val selectedSize: String,
    val selectedColor: String
)
