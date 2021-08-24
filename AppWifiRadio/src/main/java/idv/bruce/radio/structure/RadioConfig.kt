package idv.bruce.radio.structure

import android.os.Parcel
import android.os.Parcelable

class RadioConfig() : Parcelable {
    var displayName:String = ""

    var isEncryption:Boolean = false

    var key:String = ""

    constructor(parcel : Parcel) : this() {
        displayName = parcel.readString()!!
        isEncryption = parcel.readByte() != 0.toByte()
        key = parcel.readString()!!
    }

    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeString(displayName)
        parcel.writeByte(if (isEncryption) 1 else 0)
        parcel.writeString(key)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RadioConfig> {
        override fun createFromParcel(parcel : Parcel) : RadioConfig {
            return RadioConfig(parcel)
        }

        override fun newArray(size : Int) : Array<RadioConfig?> {
            return arrayOfNulls(size)
        }
    }


}