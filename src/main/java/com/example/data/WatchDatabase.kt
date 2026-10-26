package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val id: String,
    val watchId: String,
    val selectedColor: String,
    val selectedStrap: String,
    val quantity: Int
)

@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey val watchId: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val subtotal: Double,
    val discount: Double,
    val shipping: Double,
    val tax: Double,
    val total: Double,
    val itemsSummary: String,
    val customerName: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val orderDate: String,
    val status: String
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val watchId: String,
    val authorName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val verifiedPurchase: Boolean
)

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartEntity)

    @Update
    suspend fun update(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items")
    fun getAllWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistEntity)

    @Query("DELETE FROM wishlist_items WHERE watchId = :watchId")
    suspend fun deleteByWatchId(watchId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE watchId = :watchId)")
    suspend fun isInWishlist(watchId: String): Boolean
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE watchId = :watchId")
    fun getReviewsForWatch(watchId: String): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)
}

@Database(
    entities = [CartEntity::class, WishlistEntity::class, OrderEntity::class, ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WatchDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: WatchDatabase? = null

        fun getDatabase(context: Context): WatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchDatabase::class.java,
                    "timecraft_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
