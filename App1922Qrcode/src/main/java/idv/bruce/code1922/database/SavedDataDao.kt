package idv.bruce.code1922.database

import androidx.room.*
import idv.bruce.code1922.structure.SavedData

@Dao
interface SavedDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data:SavedData):Long

    @Delete
    fun delete(data : SavedData)

    @Query("select * from t_saved")
    fun getAll():List<SavedData>

    @Query("select * from t_saved where code like :code")
    fun find(code:String):List<SavedData>
}