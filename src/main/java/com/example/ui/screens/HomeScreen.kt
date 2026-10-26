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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.WatchCatalogData
import com.example.model.CustomerReview
import com.example.model.Screen
import com.example.model.WatchCategory
import com.example.model.WatchProduct
import com.example.ui.components.ProductCard
import com.example.ui.components.RatingStars
import com.example.ui.theme.AccentGreen
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

@Composable
fun HomeScreen(
    watches: List<WatchProduct>,
    wishlistIds: Set<String>,
    onSelectWatch: (WatchProduct) -> Unit,
    onToggleWishlist: (WatchProduct) -> Unit,
    onAddToCart: (WatchProduct) -> Unit,
    onNavigateToCategory: (WatchCategory) -> Unit,
    onNavigateToCatalog: () -> Unit,
    onSubscribeNewsletter: (String) -> Unit
) {
    var newsletterEmail by remember { mutableStateOf("") }
    var newsletterSubscribed by remember { mutableStateOf(false) }

    val bestSellers = remember(watches) { watches.filter { it.isBestSeller } }
    val newArrivals = remember(watches) { watches.filter { it.isNewArrival } }
    val luxuryWatches = remember(watches) { watches.filter { it.category == WatchCategory.LUXURY } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .verticalScroll(rememberScrollState())
    ) {
        // A. HERO SECTION
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
        ) {
            // Hero background image
            AsyncImage(
                model = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=1200&q=85",
                contentDescription = "TIMECRAFT Hero Luxury Watch",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Deep gradient overlay for cinematic contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ObsidianBlack.copy(alpha = 0.5f),
                                ObsidianBlack.copy(alpha = 0.75f),
                                ObsidianBlack
                            )
                        )
                    )
            )

            // Hero Text & Call to Actions
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GoldPrimary.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "NEW 2026 HOROLOGY COLLECTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = GoldLight,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "TIME THAT DEFINES YOU",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover timeless craftsmanship, Swiss precision, and modern elegance for every moment.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onNavigateToCatalog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("hero_shop_now_btn")
                    ) {
                        Text("Shop Now", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    OutlinedButton(
                        onClick = { onNavigateToCategory(WatchCategory.LUXURY) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GoldLight
                        ),
                        border = BorderStroke(1.dp, GoldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("hero_explore_collection_btn")
                    ) {
                        Text("Explore Luxury", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // B. FEATURED CATEGORIES
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Curated Collections",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Explore by horological style & lifestyle",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldPrimary,
                    modifier = Modifier.clickable { onNavigateToCatalog() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val categoryCards = listOf(
                Pair(WatchCategory.MENS, "https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=400&q=80"),
                Pair(WatchCategory.WOMENS, "https://images.unsplash.com/photo-1542496658-e33a6d0d50f6?auto=format&fit=crop&w=400&q=80"),
                Pair(WatchCategory.LUXURY, "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=400&q=80"),
                Pair(WatchCategory.SMART, "https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=400&q=80"),
                Pair(WatchCategory.SPORTS, "https://images.unsplash.com/photo-1518131672697-613becd4fab5?auto=format&fit=crop&w=400&q=80"),
                Pair(WatchCategory.MINIMALIST, "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=400&q=80")
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(categoryCards) { (cat, img) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
                        border = BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier
                            .width(150.dp)
                            .height(180.dp)
                            .clickable { onNavigateToCategory(cat) }
                            .testTag("category_card_${cat.name}")
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = img,
                                contentDescription = cat.displayName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, ObsidianBlack.copy(alpha = 0.85f))
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = cat.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Explore →",
                                    fontSize = 11.sp,
                                    color = GoldPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // C. BEST SELLERS SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Best Sellers",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Most coveted timepieces of the season",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "See All",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldPrimary,
                    modifier = Modifier.clickable { onNavigateToCatalog() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2-column layout for top 4 best sellers
            val displayBest = bestSellers.take(4)
            for (i in displayBest.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val item1 = displayBest[i]
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(
                            watch = item1,
                            isWishlisted = wishlistIds.contains(item1.id),
                            onCardClick = { onSelectWatch(item1) },
                            onWishlistClick = { onToggleWishlist(item1) },
                            onAddToCartClick = { onAddToCart(item1) }
                        )
                    }

                    if (i + 1 < displayBest.size) {
                        val item2 = displayBest[i + 1]
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                watch = item2,
                                isWishlisted = wishlistIds.contains(item2.id),
                                onCardClick = { onSelectWatch(item2) },
                                onWishlistClick = { onToggleWishlist(item2) },
                                onAddToCartClick = { onAddToCart(item2) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // F. PROMOTIONAL FESTIVE BANNER
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CharcoalElevated),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF2B2313),
                                CharcoalSurface,
                                Color(0xFF1E1E26)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = GoldPrimary
                        ) {
                            Text(
                                text = "EXCLUSIVE OFFER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ObsidianBlack,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FESTIVE HOROLOGY PRIVILEGE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = GoldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Save up to ₹2,500 on Heritage & Automatic Timepieces",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Use code TIMECRAFT10 for 10% off or LUXURY25 for ₹2,500 off during checkout. Free insured international shipping on all orders above ₹5,000.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onNavigateToCatalog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("Explore Eligible Watches", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // D. NEW ARRIVALS SHELF
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "New Arrivals",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Freshly released horological innovations",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "Browse",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldPrimary,
                    modifier = Modifier.clickable { onNavigateToCatalog() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(newArrivals) { watch ->
                    Box(modifier = Modifier.width(220.dp)) {
                        ProductCard(
                            watch = watch,
                            isWishlisted = wishlistIds.contains(watch.id),
                            onCardClick = { onSelectWatch(watch) },
                            onWishlistClick = { onToggleWishlist(watch) },
                            onAddToCartClick = { onAddToCart(watch) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // E. EDITORIAL LUXURY SPOTLIGHT
        Surface(
            color = CharcoalDark,
            border = BorderStroke(1.dp, CharcoalBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "THE ART OF TIMEKEEPING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    color = GoldPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Swiss Automatic Calibres & Hand-Finished Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Every watch at TIMECRAFT is authenticated by our master horologists. Enjoy a 5-year global concierge warranty, complimentary bracelet sizing, and insured courier delivery directly to your door.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LuxuryPillar(icon = Icons.Default.Security, title = "100% Authentic", desc = "Certified by makers")
                    LuxuryPillar(icon = Icons.Default.LocalShipping, title = "Free Express", desc = "Fully insured transit")
                    LuxuryPillar(icon = Icons.Default.Verified, title = "5-Yr Warranty", desc = "Concierge care")
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // G. CUSTOMER REVIEWS (DEMO CONTENT)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Patron Experiences",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Verified reviews from luxury watch connoisseurs",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(WatchCatalogData.sampleReviews) { review ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
                        border = BorderStroke(1.dp, CharcoalBorder),
                        modifier = Modifier
                            .width(280.dp)
                            .height(190.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RatingStars(rating = review.rating.toDouble(), starSize = 14)
                                    Text(review.date, fontSize = 11.sp, color = TextTertiary)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "\"${review.comment}\"",
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp,
                                    maxLines = 4
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = GoldPrimary.copy(alpha = 0.2f),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = review.authorName.take(1),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldLight
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = review.authorName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = AccentGreen,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "Verified Purchaser",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // H. NEWSLETTER SUBSCRIPTION
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CharcoalSurface),
            border = BorderStroke(1.dp, CharcoalBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "THE TIMECRAFT GAZETTE",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Subscribe to receive VIP access to limited-edition allocations, horology deep-dives, and seasonal private sales.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newsletterEmail,
                    onValueChange = { newsletterEmail = it },
                    placeholder = { Text("Enter your email address", fontSize = 13.sp, color = TextTertiary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = CharcoalBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = CharcoalDark,
                        unfocusedContainerColor = CharcoalDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("newsletter_email_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (newsletterEmail.isNotBlank()) {
                            onSubscribeNewsletter(newsletterEmail)
                            newsletterSubscribed = true
                            newsletterEmail = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("newsletter_subscribe_btn")
                ) {
                    Text(
                        text = if (newsletterSubscribed) "Subscribed ✓" else "Request VIP Invitation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // I. LUXURY FOOTER
        Surface(
            color = CharcoalDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "TIMECRAFT",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    color = GoldPrimary,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "PURVEYORS OF LUXURY & HERITAGE HOROLOGY",
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Headquarters: Nariman Point, Mumbai 400021, India\nCustomer Concierge: concierge@timecraft.luxury | +91 (022) 8000-TIME",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = CharcoalBorder)
                Spacer(modifier = Modifier.height(16.dp))

                // Footer links
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("About TIMECRAFT", fontSize = 11.sp, color = TextPrimary)
                        Text("Shipping & Returns", fontSize = 11.sp, color = TextPrimary)
                        Text("Authenticity Guarantee", fontSize = 11.sp, color = TextPrimary)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("5-Year Warranty", fontSize = 11.sp, color = TextPrimary)
                        Text("Privacy Policy", fontSize = 11.sp, color = TextPrimary)
                        Text("Terms & Conditions", fontSize = 11.sp, color = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(14.dp))
                    Text(
                        text = "100% Encrypted 256-Bit SSL Checkout • Visa • Mastercard • UPI • RuPay",
                        fontSize = 10.sp,
                        color = TextTertiary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "© 2026 TIMECRAFT Luxury Horology Pvt. Ltd. All rights reserved.",
                    fontSize = 10.sp,
                    color = TextTertiary
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun LuxuryPillar(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = CharcoalElevated,
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(desc, fontSize = 9.sp, color = TextSecondary)
    }
}
