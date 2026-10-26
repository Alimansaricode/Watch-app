package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.WatchDatabase
import com.example.data.WatchRepository
import com.example.model.CartItem
import com.example.model.CustomerReview
import com.example.model.Order
import com.example.model.Screen
import com.example.model.ShippingAddress
import com.example.model.UserProfile
import com.example.model.WatchCategory
import com.example.model.WatchProduct
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption(val title: String) {
    POPULAR("Popularity"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    NEWEST("New Arrivals"),
    RATING("Customer Rating")
}

data class FilterState(
    val selectedCategory: WatchCategory = WatchCategory.ALL,
    val selectedBrands: Set<String> = emptySet(),
    val minPrice: Double = 0.0,
    val maxPrice: Double = 200000.0,
    val selectedStrapMaterial: String? = null,
    val selectedDialColor: String? = null,
    val minRating: Double = 0.0,
    val inStockOnly: Boolean = false,
    val sortOption: SortOption = SortOption.POPULAR
)

class WatchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WatchRepository
    init {
        val db = WatchDatabase.getDatabase(application)
        repository = WatchRepository(db)
    }

    // Navigation Stack
    private val screenStack = mutableListOf(Screen.HOME)
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Navigation drawer
    private val _drawerOpen = MutableStateFlow(false)
    val drawerOpen: StateFlow<Boolean> = _drawerOpen.asStateFlow()

    fun openDrawer() { _drawerOpen.value = true }
    fun closeDrawer() { _drawerOpen.value = false }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenStack.add(screen)
            _currentScreen.value = screen
        }
        _drawerOpen.value = false
    }

    fun navigateBack(): Boolean {
        return if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.lastIndex)
            _currentScreen.value = screenStack.last()
            true
        } else {
            false
        }
    }

    // Selected product for Details view
    private val _selectedWatch = MutableStateFlow<WatchProduct?>(null)
    val selectedWatch: StateFlow<WatchProduct?> = _selectedWatch.asStateFlow()

    fun selectWatch(watch: WatchProduct) {
        _selectedWatch.value = watch
        navigateTo(Screen.PRODUCT_DETAIL)
    }

    // Search and Catalog Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    fun setCategory(category: WatchCategory) {
        _filterState.value = _filterState.value.copy(selectedCategory = category)
    }

    fun toggleBrand(brand: String) {
        val current = _filterState.value.selectedBrands.toMutableSet()
        if (current.contains(brand)) current.remove(brand) else current.add(brand)
        _filterState.value = _filterState.value.copy(selectedBrands = current)
    }

    fun setPriceRange(min: Double, max: Double) {
        _filterState.value = _filterState.value.copy(minPrice = min, maxPrice = max)
    }

    fun setStrapMaterial(strap: String?) {
        _filterState.value = _filterState.value.copy(selectedStrapMaterial = strap)
    }

    fun setDialColor(color: String?) {
        _filterState.value = _filterState.value.copy(selectedDialColor = color)
    }

    fun setMinRating(rating: Double) {
        _filterState.value = _filterState.value.copy(minRating = rating)
    }

    fun setInStockOnly(inStock: Boolean) {
        _filterState.value = _filterState.value.copy(inStockOnly = inStock)
    }

    fun setSortOption(sort: SortOption) {
        _filterState.value = _filterState.value.copy(sortOption = sort)
    }

    fun resetFilters() {
        _filterState.value = FilterState()
        _searchQuery.value = ""
    }

    val allWatches: List<WatchProduct> = repository.getAllWatches()

    val availableBrands: List<String> = allWatches.map { it.brand }.distinct().sorted()

    val availableStrapMaterials: List<String> = allWatches.flatMap { it.strapMaterials }.distinct().sorted()

    val availableDialColors: List<String> = listOf("Black", "Blue", "Gold", "Silver", "Green", "White")

    // Filtered watches StateFlow
    val filteredWatches: StateFlow<List<WatchProduct>> = combine(
        _searchQuery,
        _filterState
    ) { query, filter ->
        allWatches.filter { watch ->
            // Category check
            val matchesCategory = (filter.selectedCategory == WatchCategory.ALL) || (watch.category == filter.selectedCategory)
            // Brand check
            val matchesBrand = filter.selectedBrands.isEmpty() || filter.selectedBrands.contains(watch.brand)
            // Price check
            val matchesPrice = watch.price >= filter.minPrice && watch.price <= filter.maxPrice
            // Strap check
            val matchesStrap = filter.selectedStrapMaterial == null || watch.strapMaterials.any { it.contains(filter.selectedStrapMaterial, ignoreCase = true) }
            // Color check
            val matchesColor = filter.selectedDialColor == null || watch.colors.any { it.contains(filter.selectedDialColor, ignoreCase = true) }
            // Rating check
            val matchesRating = watch.rating >= filter.minRating
            // Stock check
            val matchesStock = !filter.inStockOnly || watch.stockStatus.contains("In Stock", ignoreCase = true)
            // Search query check
            val matchesSearch = query.isBlank() ||
                watch.name.contains(query, ignoreCase = true) ||
                watch.brand.contains(query, ignoreCase = true) ||
                watch.category.displayName.contains(query, ignoreCase = true)

            matchesCategory && matchesBrand && matchesPrice && matchesStrap && matchesColor && matchesRating && matchesStock && matchesSearch
        }.let { list ->
            when (filter.sortOption) {
                SortOption.POPULAR -> list.sortedByDescending { it.reviewCount }
                SortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
                SortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
                SortOption.NEWEST -> list.sortedByDescending { if (it.isNewArrival) 1 else 0 }
                SortOption.RATING -> list.sortedByDescending { it.rating }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), allWatches)

    // Filter BottomSheet toggle
    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()
    fun openFilterSheet() { _showFilterSheet.value = true }
    fun closeFilterSheet() { _showFilterSheet.value = false }

    // Cart State
    val cartItems: StateFlow<List<CartItem>> = repository.cartItemsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon: StateFlow<String?> = _appliedCoupon.asStateFlow()

    private val _couponError = MutableStateFlow<String?>(null)
    val couponError: StateFlow<String?> = _couponError.asStateFlow()

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        when (trimmed) {
            "TIMECRAFT10" -> {
                _appliedCoupon.value = "TIMECRAFT10"
                _couponError.value = null
                emitToast("Coupon TIMECRAFT10 applied: 10% Extra Off!")
            }
            "LUXURY25" -> {
                _appliedCoupon.value = "LUXURY25"
                _couponError.value = null
                emitToast("Coupon LUXURY25 applied: ₹2,500 Festive Savings!")
            }
            "" -> {
                _appliedCoupon.value = null
                _couponError.value = null
            }
            else -> {
                _couponError.value = "Invalid coupon code. Try TIMECRAFT10 or LUXURY25"
            }
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        _couponError.value = null
    }

    fun addToCart(watch: WatchProduct, color: String, strap: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(watch, color, strap, quantity)
            emitToast("Added ${watch.name} to Cart")
        }
    }

    fun updateCartQuantity(cartId: String, newQuantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartId, newQuantity)
        }
    }

    fun removeFromCart(cartId: String) {
        viewModelScope.launch {
            repository.removeFromCart(cartId)
            emitToast("Item removed from Cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Wishlist State
    val wishlistIds: StateFlow<Set<String>> = repository.wishlistIdsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val wishlistWatches: StateFlow<List<WatchProduct>> = combine(
        wishlistIds
    ) { ids ->
        allWatches.filter { ids.first().contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleWishlist(watch: WatchProduct) {
        viewModelScope.launch {
            val added = repository.toggleWishlist(watch.id)
            if (added) {
                emitToast("Added ${watch.name} to Wishlist")
            } else {
                emitToast("Removed ${watch.name} from Wishlist")
            }
        }
    }

    fun isWishlisted(watchId: String): Boolean {
        return wishlistIds.value.contains(watchId)
    }

    // Orders State
    val orders: StateFlow<List<Order>> = repository.ordersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _lastPlacedOrderId = MutableStateFlow<String?>(null)
    val lastPlacedOrderId: StateFlow<String?> = _lastPlacedOrderId.asStateFlow()

    fun placeOrder(
        items: List<CartItem>,
        subtotal: Double,
        discount: Double,
        shipping: Double,
        tax: Double,
        total: Double,
        paymentMethod: String
    ) {
        viewModelScope.launch {
            val orderId = repository.placeOrder(
                items = items,
                subtotal = subtotal,
                discount = discount,
                shipping = shipping,
                tax = tax,
                total = total,
                address = _userProfile.value.address,
                paymentMethod = paymentMethod
            )
            _lastPlacedOrderId.value = orderId
            removeCoupon()
            navigateTo(Screen.ORDER_SUCCESS)
            emitToast("Order placed successfully! ID: $orderId")
        }
    }

    // User Profile & Authentication State (Demo / Local Storage)
    private val _userProfile = MutableStateFlow(UserProfile(isLoggedIn = false))
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun login(email: String, name: String = "Alexander Hayes") {
        _userProfile.value = _userProfile.value.copy(
            email = email,
            name = if (name.isNotBlank()) name else "Alexander Hayes",
            isLoggedIn = true
        )
        emitToast("Welcome back, ${_userProfile.value.name}!")
        navigateTo(Screen.PROFILE)
    }

    fun logout() {
        _userProfile.value = _userProfile.value.copy(isLoggedIn = false)
        emitToast("Logged out successfully.")
        navigateTo(Screen.HOME)
    }

    fun updateProfile(name: String, email: String, phone: String, address: ShippingAddress) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            email = email,
            phone = phone,
            address = address
        )
        emitToast("Profile updated successfully.")
    }

    // Reviews for selected watch
    fun getReviewsForSelectedWatch(watchId: String): StateFlow<List<CustomerReview>> {
        return repository.getReviewsForWatch(watchId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun submitReview(watchId: String, authorName: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.addReview(watchId, authorName, rating, comment)
            emitToast("Thank you for your review!")
        }
    }

    // Newsletter subscription
    private val _newsletterSubscribed = MutableStateFlow(false)
    val newsletterSubscribed: StateFlow<Boolean> = _newsletterSubscribed.asStateFlow()

    fun subscribeNewsletter(email: String): Boolean {
        return if (email.contains("@") && email.contains(".")) {
            _newsletterSubscribed.value = true
            emitToast("Subscribed! Exclusive horology private previews sent to $email")
            true
        } else {
            emitToast("Please enter a valid email address.")
            false
        }
    }

    // Toast / Feedback events
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.emit(msg)
        }
    }
}
