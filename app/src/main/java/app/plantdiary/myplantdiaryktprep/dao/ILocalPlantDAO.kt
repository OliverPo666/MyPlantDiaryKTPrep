package app.plantdiary.myplantdiaryktprep.dao

import androidx.room.*
import app.plantdiary.myplantdiaryktprep.dto.Plant

@Dao
interface ILocalPlantDAO {

    @Query("SELECT * FROM plant")
    fun getAllPlants(): List<Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: ArrayList<Plant>)

    @Delete
    fun delete(plant: Plant)
}