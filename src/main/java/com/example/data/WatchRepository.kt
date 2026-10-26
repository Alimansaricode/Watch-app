package com.example.data

import com.example.model.CartItem
import com.example.model.CustomerReview
import com.example.model.Order
import com.example.model.ShippingAddress
import com.example.model.UserProfile
import com.example.model.WatchCategory
import com.example.model.WatchProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class WatchRepository(private val database: WatchDatabase) {

    private val catalogMap: Map<String, WatchProduct> =
        WatchCatalogData.sampleWatches.associateBy { it.id }

    fun getAllWatches(): List<WatchProduct> = WatchCatalogData.sampleWatches

    fun getWatchById(id: String): WatchProduct? = catalogMap[id]

    // Cart operations backed by Room
    val cartItemsFlow: Flow<List<CartItem>> = database.cartDao().getAllCartItems().map { entities ->
        entities.mapNotNull { entity ->
            val watch = catalogMap[entity.watchId] ?: return@mapNotNull null
            CartItem(
                id = entity.id,
                watch = watch,
                selectedColor = entity.selectedColor,
                selectedStrap = entity.selectedStrap,
                quantity = entity.quantity
            )
        }
    }

    suspend fun addToCart(
        watch: WatchProduct,
        selectedColor: String,
        selectedStrap: String,
        quantity: Int = 1
    ) {
        val existingItems = database.cartDao().getAllCartItems().first()
        val matching = existingItems.firstOrNull {
            it.watchId == watch.id && it.selectedColor == selectedColor && it.selectedStrap == selectedStrap
        }

        if (matching != null) {
            database.cartDao().update(
                matching.copy(quantity = matching.quantity + quantity)
            )
        } else {
            val id = UUID.randomUUID().toString()
            database.cartDao().insertOrUpdate(
                CartEntity(
                    id = id,
                    watchId = watch.id,
                    selectedColor = selectedColor,
                    selectedStrap = selectedStrap,
                    quantity = quantity
                )
            )
        }
    }

    suspend fun updateCartQuantity(cartId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            database.cartDao().deleteById(cartId)
        } else {
            val current = database.cartDao().getAllCartItems().first().firstOrNull { it.id == cartId }
            if (current != null) {
                database.cartDao().update(current.copy(quantity = newQuantity))
            }
        }
    }

    suspend fun removeFromCart(cartId: String) {
        database.cartDao().deleteById(cartId)
    }

    suspend fun clearCart() {
        database.cartDao().clearCart()
    }

    // Wishlist operations backed by Room
    val wishlistIdsFlow: Flow<Set<String>> = database.wishlistDao().getAllWishlist().map { list ->
        list.map { it.watchId }.toSet()
    }

    suspend fun toggleWishlist(watchId: String): Boolean {
        val isInWishlist = database.wishlistDao().isInWishlist(watchId)
        if (isInWishlist) {
            database.wishlistDao().deleteByWatchId(watchId)
            return false
        } else {
            database.wishlistDao().insert(WishlistEntity(watchId = watchId))
            return true
        }
    }

    // Orders backed by Room
    val ordersFlow: Flow<List<Order>> = database.orderDao().getAllOrders().map { orderEntities ->
        orderEntities.map { entity ->
            Order(
                orderId = entity.orderId,
                items = emptyList(), // summaries retained in entity
                subtotal = entity.subtotal,
                discount = entity.discount,
                shipping = entity.shipping,
                tax = entity.tax,
                total = entity.total,
                shippingAddress = ShippingAddress(
                    fullName = entity.customerName,
                    addressLine = entity.shippingAddress
                ),
                paymentMethod = entity.paymentMethod,
                orderDate = entity.orderDate,
                status = entity.status
            )
        }
    }

    suspend fun placeOrder(
        items: List<CartItem>,
        subtotal: Double,
        discount: Double,
        shipping: Double,
        tax: Double,
        total: Double,
        address: ShippingAddress,
        paymentMethod: String
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val orderDate = dateFormat.format(Date())
        val orderId = "TC-" + (100000..999999).random()

        val summary = items.joinToString("; ") { "${it.watch.name} x${it.quantity}" }

        val orderEntity = OrderEntity(
            orderId = orderId,
            subtotal = subtotal,
            discount = discount,
            shipping = shipping,
            tax = tax,
            total = total,
            itemsSummary = summary,
            customerName = address.fullName,
            shippingAddress = "${address.addressLine}, ${address.city}, ${address.state} - ${address.pinCode}",
            paymentMethod = paymentMethod,
            orderDate = orderDate,
            status = "Processing"
        )

        database.orderDao().insertOrder(orderEntity)
        clearCart()
        return orderId
    }

    // Reviews operations
    fun getReviewsForWatch(watchId: String): Flow<List<CustomerReview>> {
        val staticReviews = WatchCatalogData.sampleReviews.filter { it.watchId == watchId }
        return database.reviewDao().getReviewsForWatch(watchId).map { roomReviews ->
            val userReviews = roomReviews.map {
                CustomerReview(
                    id = it.id,
                    watchId = it.watchId,
                    authorName = it.authorName,
                    rating = it.rating,
                    comment = it.comment,
                    date = it.date,
                    verifiedPurchase = it.verifiedPurchase
                )
            }
            userReviews + staticReviews
        }
    }

    suspend fun addReview(watchId: String, authorName: String, rating: Int, comment: String) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val today = dateFormat.format(Date())
        val entity = ReviewEntity(
            id = UUID.randomUUID().toString(),
            watchId = watchId,
            authorName = authorName,
            rating = rating,
            comment = comment,
            date = today,
            verifiedPurchase = true
        )
        database.reviewDao().insertReview(entity)
    }
}
