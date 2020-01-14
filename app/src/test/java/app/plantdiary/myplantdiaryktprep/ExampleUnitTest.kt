package app.plantdiary.myplantdiaryktprep

import app.plantdiary.myplantdiaryktprep.ui.main.MainViewModel
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
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


}
