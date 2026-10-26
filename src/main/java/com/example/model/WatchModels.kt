package com.example.model

enum class WatchCategory(val displayName: String) {
    ALL("All Watches"),
    MENS("Men's Watches"),
    WOMENS("Women's Watches"),
    LUXURY("Luxury Watches"),
    SMART("Smart Watches"),
    SPORTS("Sports Watches"),
    MINIMALIST("Minimalist Watches")
}

data class WatchProduct(
    val id: String,
    val name: String,
    val brand: String,
    val category: WatchCategory,
    val price: Double,
    val originalPrice: Double,
    val discountPercent: Int,
    val imageUrl: String,
    val additionalImages: List<String> = emptyList(),
    val rating: Double,
    val reviewCount: Int,
    val description: String,
    val colors: List<String>,
    val strapMaterials: List<String>,
    val stockStatus: String = "In Stock",
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val isBestSeller: Boolean = false,
    val movement: String = "Automatic Calibre",
    val caseSize: String = "40mm",
    val waterResistance: String = "100m (10 ATM)",
    val warranty: String = "2 Years International Warranty",
    val isFictionalSample: Boolean = false
)

data class CartItem(
    val id: String,
    val watch: WatchProduct,
    val selectedColor: String,
    val selectedStrap: String,
    val quantity: Int = 1
) {
    val totalPrice: Double
        get() = watch.price * quantity
}

data class CustomerReview(
    val id: String,
    val watchId: String,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val verifiedPurchase: Boolean = true
)

data class ShippingAddress(
    val fullName: String = "Karan Kumar",
    val phone: String = "+91 98765 43210",
    val addressLine: String = "42 Heritage Boulevard, Bandra West",
    val city: String = "Mumbai",
    val state: String = "Maharashtra",
    val pinCode: String = "400050"
)

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val shipping: Double,
    val tax: Double,
    val total: Double,
    val shippingAddress: ShippingAddress,
    val paymentMethod: String,
    val orderDate: String,
    val status: String = "Processing" // "Processing", "Shipped", "Out for Delivery", "Delivered"
)

data class UserProfile(
    val name: String = "Alexander Hayes",
    val email: String = "alexander.hayes@timecraft.luxury",
    val phone: String = "+91 98765 43210",
    val address: ShippingAddress = ShippingAddress(),
    val isLoggedIn: Boolean = false
)

enum class Screen {
    HOME,
    CATALOG,
    PRODUCT_DETAIL,
    CART,
    WISHLIST,
    CHECKOUT,
    ORDER_SUCCESS,
    MY_ORDERS,
    PROFILE,
    AUTH
}
