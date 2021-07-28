package idv.bruce.code1922.structure

import androidx.room.Entity
import androidx.room.PrimaryKey
import idv.bruce.code1922.structure.SmsData.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class SmsData {
    companion object {
        const val TABLE_NAME = "t_sms"
    }

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

    var time : Long = -1

    var code : String = ""
        set(value) {
            field = value.chunked(4).joinToString(separator = " ")
        }

    var content : String = ""
        set(value) {
            field = value
            code = field.filter { it.isDigit() }
        }
}