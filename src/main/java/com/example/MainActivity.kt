package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Screen
import com.example.model.WatchCategory
import com.example.ui.components.DrawerNavigationMenu
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.LuxuryTopBar
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ObsidianBlack
import com.example.viewmodel.WatchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: WatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                TimecraftApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TimecraftApp(viewModel: WatchViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistIds.collectAsStateWithLifecycle()
    val wishlistWatches by viewModel.wishlistWatches.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredWatches by viewModel.filteredWatches.collectAsStateWithLifecycle()
    val showFilterSheet by viewModel.showFilterSheet.collectAsStateWithLifecycle()
    val selectedWatch by viewModel.selectedWatch.collectAsStateWithLifecycle()
    val appliedCoupon by viewModel.appliedCoupon.collectAsStateWithLifecycle()
    val couponError by viewModel.couponError.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val lastPlacedOrderId by viewModel.lastPlacedOrderId.collectAsStateWithLifecycle()

    val cartTotalCount = remember(cartItems) { cartItems.sumOf { it.quantity } }
    val wishlistTotalCount = remember(wishlistIds) { wishlistIds.size }

    // Collect toast messages
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    // Android physical back button handler
    BackHandler(enabled = true) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            val handled = viewModel.navigateBack()
            if (!handled) {
                // Exit app or stay on home
                (context as? ComponentActivity)?.finish()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerNavigationMenu(
                currentScreen = currentScreen,
                selectedCategory = filterState.selectedCategory,
                cartCount = cartTotalCount,
                wishlistCount = wishlistTotalCount,
                isLoggedIn = userProfile.isLoggedIn,
                userName = userProfile.name,
                onNavigate = { screen ->
                    viewModel.navigateTo(screen)
                    scope.launch { drawerState.close() }
                },
                onSelectCategory = { cat ->
                    viewModel.setCategory(cat)
                    viewModel.navigateTo(Screen.CATALOG)
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    viewModel.logout()
                    scope.launch { drawerState.close() }
                },
                onClose = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                LuxuryTopBar(
                    cartCount = cartTotalCount,
                    wishlistCount = wishlistTotalCount,
                    isLoggedIn = userProfile.isLoggedIn,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onSearchClick = {
                        viewModel.navigateTo(Screen.CATALOG)
                    },
                    onWishlistClick = {
                        viewModel.navigateTo(Screen.WISHLIST)
                    },
                    onCartClick = {
                        viewModel.navigateTo(Screen.CART)
                    },
                    onAccountClick = {
                        viewModel.navigateTo(if (userProfile.isLoggedIn) Screen.PROFILE else Screen.AUTH)
                    }
                )
            },
            containerColor = ObsidianBlack,
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(ObsidianBlack)
            ) {
                when (currentScreen) {
                    Screen.HOME -> {
                        HomeScreen(
                            watches = viewModel.allWatches,
                            wishlistIds = wishlistIds,
                            onSelectWatch = { watch -> viewModel.selectWatch(watch) },
                            onToggleWishlist = { watch -> viewModel.toggleWishlist(watch) },
                            onAddToCart = { watch ->
                                viewModel.addToCart(
                                    watch = watch,
                                    color = watch.colors.firstOrNull() ?: "Standard",
                                    strap = watch.strapMaterials.firstOrNull() ?: "Steel",
                                    quantity = 1
                                )
                            },
                            onNavigateToCategory = { cat ->
                                viewModel.setCategory(cat)
                                viewModel.navigateTo(Screen.CATALOG)
                            },
                            onNavigateToCatalog = { viewModel.navigateTo(Screen.CATALOG) },
                            onSubscribeNewsletter = { email -> viewModel.subscribeNewsletter(email) }
                        )
                    }

                    Screen.CATALOG -> {
                        CatalogScreen(
                            watches = filteredWatches,
                            totalWatchCount = viewModel.allWatches.size,
                            searchQuery = searchQuery,
                            filterState = filterState,
                            wishlistIds = wishlistIds,
                            onSearchChange = { q -> viewModel.setSearchQuery(q) },
                            onCategoryChange = { cat -> viewModel.setCategory(cat) },
                            onSortChange = { s -> viewModel.setSortOption(s) },
                            onOpenFilterSheet = { viewModel.openFilterSheet() },
                            onResetFilters = { viewModel.resetFilters() },
                            onSelectWatch = { watch -> viewModel.selectWatch(watch) },
                            onToggleWishlist = { watch -> viewModel.toggleWishlist(watch) },
                            onAddToCart = { watch ->
                                viewModel.addToCart(
                                    watch = watch,
                                    color = watch.colors.firstOrNull() ?: "Standard",
                                    strap = watch.strapMaterials.firstOrNull() ?: "Steel",
                                    quantity = 1
                                )
                            }
                        )
                    }

                    Screen.PRODUCT_DETAIL -> {
                        selectedWatch?.let { watch ->
                            ProductDetailScreen(
                                watch = watch,
                                isWishlisted = wishlistIds.contains(watch.id),
                                reviewsFlow = viewModel.getReviewsForSelectedWatch(watch.id),
                                relatedWatches = viewModel.allWatches.filter { it.category == watch.category && it.id != watch.id },
                                onBackClick = { viewModel.navigateBack() },
                                onToggleWishlist = { w -> viewModel.toggleWishlist(w) },
                                onAddToCart = { w, color, strap, qty ->
                                    viewModel.addToCart(w, color, strap, qty)
                                },
                                onBuyNow = { w, color, strap, qty ->
                                    viewModel.addToCart(w, color, strap, qty)
                                    viewModel.navigateTo(Screen.CART)
                                },
                                onSelectRelatedWatch = { rel -> viewModel.selectWatch(rel) },
                                onSubmitReview = { author, rating, comment ->
                                    viewModel.submitReview(watch.id, author, rating, comment)
                                }
                            )
                        } ?: run {
                            viewModel.navigateTo(Screen.CATALOG)
                        }
                    }

                    Screen.CART -> {
                        CartScreen(
                            cartItems = cartItems,
                            appliedCoupon = appliedCoupon,
                            couponError = couponError,
                            userProfile = userProfile,
                            onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                            onRemoveItem = { id -> viewModel.removeFromCart(id) },
                            onApplyCoupon = { code -> viewModel.applyCoupon(code) },
                            onRemoveCoupon = { viewModel.removeCoupon() },
                            onPlaceOrder = { items, sub, disc, ship, tax, total, method ->
                                viewModel.placeOrder(items, sub, disc, ship, tax, total, method)
                            },
                            onContinueShopping = { viewModel.navigateTo(Screen.CATALOG) }
                        )
                    }

                    Screen.WISHLIST -> {
                        WishlistScreen(
                            wishlistWatches = wishlistWatches,
                            onSelectWatch = { watch -> viewModel.selectWatch(watch) },
                            onRemoveFromWishlist = { watch -> viewModel.toggleWishlist(watch) },
                            onMoveToCart = { watch ->
                                viewModel.addToCart(
                                    watch = watch,
                                    color = watch.colors.firstOrNull() ?: "Standard",
                                    strap = watch.strapMaterials.firstOrNull() ?: "Steel",
                                    quantity = 1
                                )
                                viewModel.toggleWishlist(watch)
                            },
                            onExploreWatches = { viewModel.navigateTo(Screen.CATALOG) }
                        )
                    }

                    Screen.CHECKOUT -> {
                        CartScreen(
                            cartItems = cartItems,
                            appliedCoupon = appliedCoupon,
                            couponError = couponError,
                            userProfile = userProfile,
                            onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                            onRemoveItem = { id -> viewModel.removeFromCart(id) },
                            onApplyCoupon = { code -> viewModel.applyCoupon(code) },
                            onRemoveCoupon = { viewModel.removeCoupon() },
                            onPlaceOrder = { items, sub, disc, ship, tax, total, method ->
                                viewModel.placeOrder(items, sub, disc, ship, tax, total, method)
                            },
                            onContinueShopping = { viewModel.navigateTo(Screen.CATALOG) }
                        )
                    }

                    Screen.ORDER_SUCCESS -> {
                        OrderSuccessScreen(
                            orderId = lastPlacedOrderId,
                            onViewMyOrders = { viewModel.navigateTo(Screen.MY_ORDERS) },
                            onContinueShopping = { viewModel.navigateTo(Screen.HOME) }
                        )
                    }

                    Screen.MY_ORDERS -> {
                        OrdersScreen(
                            orders = orders,
                            onExploreWatches = { viewModel.navigateTo(Screen.CATALOG) }
                        )
                    }

                    Screen.PROFILE -> {
                        ProfileScreen(
                            userProfile = userProfile,
                            onUpdateProfile = { name, email, phone, addr ->
                                viewModel.updateProfile(name, email, phone, addr)
                            },
                            onNavigateToOrders = { viewModel.navigateTo(Screen.MY_ORDERS) },
                            onNavigateToWishlist = { viewModel.navigateTo(Screen.WISHLIST) },
                            onLogout = { viewModel.logout() }
                        )
                    }

                    Screen.AUTH -> {
                        AuthScreen(
                            onLogin = { email, name ->
                                viewModel.login(email, name)
                            },
                            onContinueAsGuest = {
                                viewModel.navigateTo(Screen.HOME)
                            }
                        )
                    }
                }

                // Filter Bottom Sheet modal
                if (showFilterSheet) {
                    FilterBottomSheet(
                        filterState = filterState,
                        availableBrands = viewModel.availableBrands,
                        availableStrapMaterials = viewModel.availableStrapMaterials,
                        availableDialColors = viewModel.availableDialColors,
                        onApplyFilter = { viewModel.closeFilterSheet() },
                        onResetFilter = { viewModel.resetFilters() },
                        onCategoryChange = { cat -> viewModel.setCategory(cat) },
                        onBrandToggle = { brand -> viewModel.toggleBrand(brand) },
                        onPriceRangeChange = { min, max -> viewModel.setPriceRange(min, max) },
                        onStrapChange = { strap -> viewModel.setStrapMaterial(strap) },
                        onDialColorChange = { color -> viewModel.setDialColor(color) },
                        onRatingChange = { rating -> viewModel.setMinRating(rating) },
                        onInStockChange = { inStock -> viewModel.setInStockOnly(inStock) },
                        onSortChange = { sort -> viewModel.setSortOption(sort) },
                        onDismiss = { viewModel.closeFilterSheet() }
                    )
                }
            }
        }
    }
}
