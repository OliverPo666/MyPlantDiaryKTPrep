package app.plantdiary.myplantdiaryktprep

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.plantdiary.myplantdiaryktprep.ui.main.MainViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fetchMaple_ReturnsMaple () {
        var mvm:MainViewModel = MainViewModel()
        mvm.fetchPlants("Maple")
        assertNotNull("BRandan")
        assertTrue(true)
        assertNotNull(mvm.plantsArray)
        assertTrue(mvm.plantsArray!!.size > 0)

    }

    @Test
    fun fetchMaple_returnsObservableMaple() {
        var mvm:MainViewModel = MainViewModel()
        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size >0)

        }
        mvm.fetchPlants("Maple")

    }


}
