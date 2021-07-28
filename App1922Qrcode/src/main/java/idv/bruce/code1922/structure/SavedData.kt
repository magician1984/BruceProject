package idv.bruce.code1922.structure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import idv.bruce.code1922.structure.SavedData.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class SavedData {
    companion object {
        const val TABLE_NAME = "t_saved"
    }

    @PrimaryKey
    var code : String = ""

    @ColumnInfo(name = "update_time")
    var updateTime : Long = -1

    var tag : String = ""
}