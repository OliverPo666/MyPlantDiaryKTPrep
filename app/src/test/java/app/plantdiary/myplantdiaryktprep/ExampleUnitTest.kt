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
 *
 * Added some more tests.
 * 
 */
class ExampleUnitTest {

    lateinit var mvm:MainViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun fetchMaple_returnsObservableMaple() {
        mvm = MainViewModel()
        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size >0)

        }
        mvm.fetchPlants("Maple")

    }

    @Test
    fun searchForRedbud_returnsRedbud() {
        givenAFeedOfPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainsEasternRedbud()
    }

    private fun givenAFeedOfPlantDataAreAvailable() {
        mvm = MainViewModel()
    }

    private fun whenSearchForRedbud() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenResultContainsEasternRedbud() {
        var redbudFound = false;
        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size >0)
            it.forEach {
                if (it.genus == "Cercis" && it.species == "canadensis" && it.common.contains("Eastern Redbud")) {
                    redbudFound = true;
                }
            }
            assertTrue(redbudFound)
        }
    }

    @Test
    fun searchForQuercus_returnsMultipleOaks() {
        givenAFeedOfPlantDataAreAvailable()
        whenSearchForQuercus()
        thenReturnTwoOaks()
    }

    private fun whenSearchForQuercus() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenReturnTwoOaks() {
        var oakCount = 0;
        mvm.plants.observeForever {
            it.forEach {
                if (it.genus == "Quercus") {
                    if (it.species == "alba" || it.species == "robur") {
                        oakCount++
                    }
                }
            }
            assertTrue(oakCount >= 2)
        }
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenAFeedOfPlantDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResults()
    }

    private fun whenISearchForGarbage() {
        mvm.fetchPlants("sklujapouetllkjsda;u")
    }

    private fun thenIGetZeroResults() {
        mvm.plants.observeForever {
            assertEquals(0, it.size)
        }
    }
}
