package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.WatchCatalogData
import com.example.model.WatchCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TIMECRAFT", appName)
  }

  @Test
  fun `verify watch catalog integrity`() {
    val watches = WatchCatalogData.sampleWatches
    assertTrue("Catalog should contain at least 24 timepieces", watches.size >= 24)
    watches.forEach { watch ->
      assertNotNull(watch.id)
      assertNotNull(watch.name)
      assertNotNull(watch.brand)
      assertTrue(watch.price > 0)
      assertTrue(watch.colors.isNotEmpty())
      assertTrue(watch.strapMaterials.isNotEmpty())
    }
  }
}
