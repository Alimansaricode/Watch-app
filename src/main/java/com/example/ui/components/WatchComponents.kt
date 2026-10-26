package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.Screen
import com.example.model.WatchCategory
import com.example.model.WatchProduct
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.CharcoalBorder
import com.example.ui.theme.CharcoalDark
import com.example.ui.theme.CharcoalElevated
import com.example.ui.theme.CharcoalSurface
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.StarGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.viewmodel.FilterState
import com.example.viewmodel.SortOption
import java.text.NumberFormat
import java.util.Locale

fun formatInr(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxuryTopBar(
    cartCount: Int,
    wishlistCount: Int,
    isLoggedIn: Boolean,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onCartClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ObsidianBlack,
            titleContentColor = GoldPrimary,
            navigationIconContentColor = GoldLight,
            actionIconContentColor = GoldLight
        ),
        navigationIcon = {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.testTag("nav_drawer_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Navigation Menu",
                    tint = GoldPrimary
                )
            }
        },
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMenuClick() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TIMECRAFT",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = GoldPrimary,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "HAUTE HORLOGERIE",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    color = TextSecondary
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.testTag("top_bar_search_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Catalog",
                    tint = TextPrimary
                )
            }

            IconButton(
                onClick = onWishlistClick,
                modifier = Modifier.testTag("top_bar_wishlist_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (wishlistCount > 0) {
                            Badge(
                                containerColor = GoldPrimary,
                                contentColor = ObsidianBlack
                            ) {
                                Text("$wishlistCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (wishlistCount > 0) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (wishlistCount > 0) GoldPrimary else TextPrimary
                    )
                }
            }

            IconButton(
                onClick = onCartClick,
                modifier = Modifier.testTag("top_bar_cart_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = GoldPrimary,
                                contentColor = ObsidianBlack
                            ) {
                                Text("$cartCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Cart",
                        tint = GoldPrimary
                    )
                }
            }

            IconButton(
                onClick = onAccountClick,
                modifier = Modifier.testTag("top_bar_account_btn")
            ) {
                Icon(
                    imageVector = if (isLoggedIn) Icons.Default.AccountCircle else Icons.Outlined.Person,
                    contentDescription = "User Account",
                    tint = if (isLoggedIn) GoldPrimary else TextSecondary
                )
            }
        }
    )
}

@Composable
fun ProductCard(
    watch: WatchProduct,
    isWishlisted: Boolean,
    onCardClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CharcoalSurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CharcoalBorder),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .testTag("product_card_${watch.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image Box with badges & wishlist button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(CharcoalDark)
            ) {
                AsyncImage(
                    model = watch.imageUrl,
                    contentDescription = watch.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark subtle bottom scrim for image depth
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0x66000000))
                            )
                        )
                )

                // Discount Pill or Fictional Demo Pill
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (watch.discountPercent > 0) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AccentRed
                        ) {
                            Text(
                                text = "${watch.discountPercent}% OFF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    if (watch.isFictionalSample) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CharcoalElevated.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = "CONCEPT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Wishlist Toggle Button
                Surface(
                    shape = CircleShape,
                    color = ObsidianBlack.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .clickable(onClick = onWishlistClick)
                        .testTag("wishlist_toggle_${watch.id}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isWishlisted) "Remove from wishlist" else "Add to wishlist",
                            tint = if (isWishlisted) GoldPrimary else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Brand in uppercase
                Text(
                    text = watch.brand.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp,
                    color = GoldPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Watch Name
                Text(
                    text = watch.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating & Review Count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${watch.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${watch.reviewCount})",
                        fontSize = 11.sp,
                        color = TextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price and Strikethrough
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatInr(watch.price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    if (watch.originalPrice > watch.price) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatInr(watch.originalPrice),
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough,
                            color = TextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Add to Cart Button
                Button(
                    onClick = onAddToCartClick,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = ObsidianBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("add_to_cart_${watch.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add to Cart",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RatingStars(
    rating: Double,
    maxStars: Int = 5,
    modifier: Modifier = Modifier,
    starSize: Int = 16
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val filled = rating >= i
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (filled) StarGold else CharcoalBorder,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

@Composable
fun DrawerNavigationMenu(
    currentScreen: Screen,
    selectedCategory: WatchCategory,
    cartCount: Int,
    wishlistCount: Int,
    isLoggedIn: Boolean,
    userName: String,
    onNavigate: (Screen) -> Unit,
    onSelectCategory: (WatchCategory) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        color = ObsidianBlack,
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Close Button & Brand Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TIMECRAFT",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                        color = GoldPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "HAUTE HORLOGERIE",
                        fontSize = 8.sp,
                        letterSpacing = 2.sp,
                        color = TextSecondary
                    )
                }
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Menu",
                        tint = GoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // User Info Preview
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CharcoalSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(if (isLoggedIn) Screen.PROFILE else Screen.AUTH)
                    }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isLoggedIn) userName else "Welcome Guest",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isLoggedIn) "View Account & Orders" else "Sign In or Register",
                            fontSize = 11.sp,
                            color = GoldLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation Links
            Text(
                text = "DISCOVER",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))

            DrawerItem(
                label = "Home",
                isSelected = currentScreen == Screen.HOME,
                onClick = { onNavigate(Screen.HOME) }
            )
            DrawerItem(
                label = "Shop All Watches",
                isSelected = currentScreen == Screen.CATALOG && selectedCategory == WatchCategory.ALL,
                onClick = {
                    onSelectCategory(WatchCategory.ALL)
                    onNavigate(Screen.CATALOG)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "COLLECTIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))

            val categories = listOf(
                WatchCategory.MENS,
                WatchCategory.WOMENS,
                WatchCategory.LUXURY,
                WatchCategory.SMART,
                WatchCategory.SPORTS,
                WatchCategory.MINIMALIST
            )

            categories.forEach { cat ->
                DrawerItem(
                    label = cat.displayName,
                    isSelected = currentScreen == Screen.CATALOG && selectedCategory == cat,
                    onClick = {
                        onSelectCategory(cat)
                        onNavigate(Screen.CATALOG)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MY LUXURY SUITE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = TextTertiary
            )
            Spacer(modifier = Modifier.height(8.dp))

            DrawerItem(
                label = "Shopping Cart",
                badge = if (cartCount > 0) "$cartCount" else null,
                isSelected = currentScreen == Screen.CART,
                onClick = { onNavigate(Screen.CART) }
            )
            DrawerItem(
                label = "My Wishlist",
                badge = if (wishlistCount > 0) "$wishlistCount" else null,
                isSelected = currentScreen == Screen.WISHLIST,
                onClick = { onNavigate(Screen.WISHLIST) }
            )
            DrawerItem(
                label = "My Orders",
                isSelected = currentScreen == Screen.MY_ORDERS,
                onClick = { onNavigate(Screen.MY_ORDERS) }
            )
            DrawerItem(
                label = if (isLoggedIn) "Profile & Addresses" else "Login / Register",
                isSelected = currentScreen == Screen.PROFILE || currentScreen == Screen.AUTH,
                onClick = { onNavigate(if (isLoggedIn) Screen.PROFILE else Screen.AUTH) }
            )

            if (isLoggedIn) {
                Spacer(modifier = Modifier.height(8.dp))
                DrawerItem(
                    label = "Sign Out",
                    textColor = AccentRed,
                    onClick = onLogout
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Concierge details
            Column {
                Text(
                    text = "CONCIERGE & SUPPORT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextTertiary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Toll-Free: 1800-TIMECRAFT\nconcierge@timecraft.luxury",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    badge: String? = null,
    isSelected: Boolean = false,
    textColor: Color = if (isSelected) GoldPrimary else TextPrimary,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) CharcoalSurface else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
            )
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldPrimary
                ) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ObsidianBlack,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    filterState: FilterState,
    availableBrands: List<String>,
    availableStrapMaterials: List<String>,
    availableDialColors: List<String>,
    onApplyFilter: () -> Unit,
    onResetFilter: () -> Unit,
    onCategoryChange: (WatchCategory) -> Unit,
    onBrandToggle: (String) -> Unit,
    onPriceRangeChange: (Double, Double) -> Unit,
    onStrapChange: (String?) -> Unit,
    onDialColorChange: (String?) -> Unit,
    onRatingChange: (Double) -> Unit,
    onInStockChange: (Boolean) -> Unit,
    onSortChange: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CharcoalDark,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CharcoalBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters & Refinements",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Reset All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary,
                    modifier = Modifier.clickable(onClick = onResetFilter)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sort By
            Text(
                text = "Sort By",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortOption.values().forEach { option ->
                    FilterChip(
                        selected = filterState.sortOption == option,
                        onClick = { onSortChange(option) },
                        label = { Text(option.title, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = ObsidianBlack,
                            containerColor = CharcoalSurface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterState.sortOption == option,
                            borderColor = CharcoalBorder,
                            selectedBorderColor = GoldPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Category Filter
            Text(
                text = "Watch Category",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WatchCategory.values().forEach { cat ->
                    FilterChip(
                        selected = filterState.selectedCategory == cat,
                        onClick = { onCategoryChange(cat) },
                        label = { Text(cat.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = ObsidianBlack,
                            containerColor = CharcoalSurface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = filterState.selectedCategory == cat,
                            borderColor = CharcoalBorder,
                            selectedBorderColor = GoldPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Price Range
            Text(
                text = "Price Range: ${formatInr(filterState.minPrice)} - ${formatInr(filterState.maxPrice)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            RangeSlider(
                value = (filterState.minPrice.toFloat())..(filterState.maxPrice.toFloat()),
                onValueChange = { range ->
                    onPriceRangeChange(range.start.toDouble(), range.endInclusive.toDouble())
                },
                valueRange = 0f..200000f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = CharcoalBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Brand Filter
            Text(
                text = "Brands",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableBrands.forEach { brand ->
                    val isSelected = filterState.selectedBrands.contains(brand)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onBrandToggle(brand) },
                        label = { Text(brand, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = ObsidianBlack,
                            containerColor = CharcoalSurface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CharcoalBorder,
                            selectedBorderColor = GoldPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // Dial Color
            Text(
                text = "Dial Accent Color",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableDialColors.forEach { color ->
                    val isSelected = filterState.selectedDialColor == color
                    FilterChip(
                        selected = isSelected,
                        onClick = { onDialColorChange(if (isSelected) null else color) },
                        label = { Text(color, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GoldPrimary,
                            selectedLabelColor = ObsidianBlack,
                            containerColor = CharcoalSurface,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CharcoalBorder,
                            selectedBorderColor = GoldPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = CharcoalBorder)
            Spacer(modifier = Modifier.height(16.dp))

            // In Stock Only Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "In Stock Timepieces Only",
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Switch(
                    checked = filterState.inStockOnly,
                    onCheckedChange = onInStockChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ObsidianBlack,
                        checkedTrackColor = GoldPrimary,
                        uncheckedTrackColor = CharcoalBorder
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply Button
            Button(
                onClick = onApplyFilter,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = ObsidianBlack
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Show Filtered Results",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
