package idv.bruce.code1922.database

import androidx.room.*
import idv.bruce.code1922.structure.SmsData

@Dao
interface SmsDataDao {

    @Insert
    fun insert(data:SmsData):Long

    @Delete
    fun delete(data:SmsData)

    @Update
    fun update(data : SmsData)

    @Query("select * from t_sms")
    fun getAll():List<SmsData>

    @Query("select * from t_sms where code like :code")
    fun find(code:String):List<SmsData>
}