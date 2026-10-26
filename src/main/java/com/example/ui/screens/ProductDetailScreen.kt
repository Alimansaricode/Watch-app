package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CustomerReview
import com.example.model.WatchProduct
import com.example.ui.components.RatingStars
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.CharcoalBorder
import com.example.ui.theme.CharcoalDark
import com.example.ui.theme.CharcoalElevated
import com.example.ui.theme.CharcoalSurface
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.StarGold
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    watch: WatchProduct,
    isWishlisted: Boolean,
    reviewsFlow: StateFlow<List<CustomerReview>>,
    relatedWatches: List<WatchProduct>,
    onBackClick: () -> Unit,
    onToggleWishlist: (WatchProduct) -> Unit,
    onAddToCart: (watch: WatchProduct, color: String, strap: String, quantity: Int) -> Unit,
    onBuyNow: (watch: WatchProduct, color: String, strap: String, quantity: Int) -> Unit,
    onSelectRelatedWatch: (WatchProduct) -> Unit,
    onSubmitReview: (author: String, rating: Int, comment: String) -> Unit
) {
    val reviews by reviewsFlow.collectAsState()

    var selectedImageIndex by remember { mutableIntStateOf(0) }
    val allImages = remember(watch) {
        listOf(watch.imageUrl) + watch.additionalImages
    }

    var selectedColor by remember { mutableStateOf(watch.colors.firstOrNull() ?: "Standard") }
    var selectedStrap by remember { mutableStateOf(watch.strapMaterials.firstOrNull() ?: "Stainless Steel") }
    var quantity by remember { mutableIntStateOf(1) }

    var showReviewDialog by remember { mutableStateOf(false) }
    var newReviewAuthor by remember { mutableStateOf("") }
    var newReviewRating by remember { mutableIntStateOf(5) }
    var newReviewComment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Top App Bar
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ObsidianBlack,
                navigationIconContentColor = GoldPrimary,
                actionIconContentColor = GoldPrimary
            ),
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = {
                Text(
                    text = watch.brand.uppercase(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary,
                    fontFamily = FontFamily.Serif
                )
            },
            actions = {
                IconButton(
                    onClick = { onToggleWishlist(watch) },
                    modifier = Modifier.testTag("detail_wishlist_btn")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) GoldPrimary else TextPrimary
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Main Watch Image Gallery
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(CharcoalDark)
            ) {
                AsyncImage(
                    model = allImages.getOrElse(selectedImageIndex) { watch.imageUrl },
                    contentDescription = watch.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (watch.discountPercent > 0) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = AccentRed,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${watch.discountPercent}% OFF",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Thumbnail Selector
            if (allImages.size > 1) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allImages.indices.toList()) { index ->
                        val isSelected = selectedImageIndex == index
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(2.dp, if (isSelected) GoldPrimary else CharcoalBorder),
                            modifier = Modifier
                                .size(60.dp)
                                .clickable { selectedImageIndex = index }
                        ) {
                            AsyncImage(
                                model = allImages[index],
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            // Product Details Information
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = watch.brand.uppercase(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = watch.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rating & Reviews Count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(rating = watch.rating, starSize = 16)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${watch.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${watch.reviewCount} Connoisseur Reviews",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pricing
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatInr(watch.price),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    if (watch.originalPrice > watch.price) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = formatInr(watch.originalPrice),
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.LineThrough,
                            color = TextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stock status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = watch.stockStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AccentGreen
                    )
                    if (watch.isFictionalSample) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "• Concept Demo Timepiece",
                            fontSize = 11.sp,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CharcoalBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Description",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = watch.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color Selection
                Text(
                    text = "Dial & Case Color: $selectedColor",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(watch.colors) { color ->
                        val isSelected = selectedColor == color
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GoldPrimary else CharcoalSurface,
                            border = BorderStroke(1.dp, if (isSelected) GoldLight else CharcoalBorder),
                            modifier = Modifier
                                .clickable { selectedColor = color }
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = color,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ObsidianBlack else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Strap Material Selection
                Text(
                    text = "Strap Material: $selectedStrap",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(watch.strapMaterials) { strap ->
                        val isSelected = selectedStrap == strap
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GoldPrimary else CharcoalSurface,
                            border = BorderStroke(1.dp, if (isSelected) GoldLight else CharcoalBorder),
                            modifier = Modifier
                                .clickable { selectedStrap = strap }
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = strap,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ObsidianBlack else TextPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CharcoalSurface,
                        border = BorderStroke(1.dp, CharcoalBorder)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", tint = TextPrimary)
                            }

                            Text(
                                text = "$quantity",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )

                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = TextPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons: Add to Cart and Buy Now
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onAddToCart(watch, selectedColor, selectedStrap, quantity) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CharcoalElevated,
                            contentColor = GoldPrimary
                        ),
                        border = BorderStroke(1.dp, GoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_add_to_cart_btn")
                    ) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onBuyNow(watch, selectedColor, selectedStrap, quantity) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_buy_now_btn")
                    ) {
                        Text("Buy Now", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                HorizontalDivider(color = CharcoalBorder)
                Spacer(modifier = Modifier.height(20.dp))

                // Technical Specifications Table
                Text(
                    text = "Technical Specifications",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
                    border = BorderStroke(1.dp, CharcoalBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SpecRow("Movement Calibre", watch.movement)
                        SpecRow("Case Diameter", watch.caseSize)
                        SpecRow("Water Resistance", watch.waterResistance)
                        SpecRow("Strap Compatibility", selectedStrap)
                        SpecRow("Crystal / Glass", "Anti-Reflective Sapphire Crystal")
                        SpecRow("International Warranty", watch.warranty)
                        SpecRow("Origin", if (watch.brand.contains("Tissot") || watch.brand.contains("Longines")) "Swiss Made" else "Japan / Domestic Origin")
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Reviews Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Customer Reviews",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "${reviews.size} verified buyer impressions",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    OutlinedButton(
                        onClick = { showReviewDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                        border = BorderStroke(1.dp, GoldPrimary),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("write_review_btn")
                    ) {
                        Icon(imageVector = Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Write Review", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (reviews.isEmpty()) {
                    Text(
                        text = "Be the first patron to review this exquisite timepiece.",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        reviews.forEach { rev ->
                            ReviewItem(review = rev)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Related Products Carousel
                if (relatedWatches.isNotEmpty()) {
                    Text(
                        text = "You May Also Admire",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(relatedWatches.take(6)) { rel ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
                                border = BorderStroke(1.dp, CharcoalBorder),
                                modifier = Modifier
                                    .width(160.dp)
                                    .clickable { onSelectRelatedWatch(rel) }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    AsyncImage(
                                        model = rel.imageUrl,
                                        contentDescription = rel.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = rel.brand.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                    Text(
                                        text = rel.name,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = formatInr(rel.price),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldLight
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Write Review Dialog
    if (showReviewDialog) {
        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            containerColor = CharcoalElevated,
            title = {
                Text(
                    text = "Review this Timepiece",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    fontFamily = FontFamily.Serif
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newReviewAuthor,
                        onValueChange = { newReviewAuthor = it },
                        label = { Text("Your Name", color = TextSecondary) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = CharcoalBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        Text("Rating:", fontSize = 13.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (star in 1..5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star stars",
                                    tint = if (star <= newReviewRating) StarGold else CharcoalBorder,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { newReviewRating = star }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newReviewComment,
                        onValueChange = { newReviewComment = it },
                        label = { Text("Your Experience & Craftsmanship Thoughts", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldPrimary,
                            unfocusedBorderColor = CharcoalBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newReviewAuthor.isNotBlank() && newReviewComment.isNotBlank()) {
                            onSubmitReview(newReviewAuthor, newReviewRating, newReviewComment)
                            showReviewDialog = false
                            newReviewAuthor = ""
                            newReviewComment = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = ObsidianBlack
                    )
                ) {
                    Text("Submit Review", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showReviewDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
        HorizontalDivider(color = CharcoalBorder.copy(alpha = 0.5f))
    }
}

@Composable
fun ReviewItem(review: CustomerReview) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
        border = BorderStroke(1.dp, CharcoalBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = review.authorName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (review.verifiedPurchase) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = AccentGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Verified Buyer",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AccentGreen,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(review.date, fontSize = 11.sp, color = TextTertiary)
            }

            Spacer(modifier = Modifier.height(4.dp))
            RatingStars(rating = review.rating.toDouble(), starSize = 13)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )
        }
    }
}
