package idv.bruce.radio.structure

import android.os.Parcel
import android.os.Parcelable

class RadioEvent() : Parcelable {
    enum class State {
        IDLE, PTT, LISTEN
    }

    var channelId : Int = -1

    var state : State = State.IDLE

    var speaker : String = ""

    constructor(parcel : Parcel) : this() {
        channelId = parcel.readInt()
        state = State.values()[parcel.readInt()]
        speaker = parcel.readString()!!
    }

    override fun writeToParcel(parcel : Parcel, flags : Int) {
        parcel.writeInt(channelId)
        parcel.writeInt(state.ordinal)
        parcel.writeString(speaker)
    }

    override fun describeContents() : Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RadioEvent> {
        override fun createFromParcel(parcel : Parcel) : RadioEvent {
            return RadioEvent(parcel)
        }

        override fun newArray(size : Int) : Array<RadioEvent?> {
            return arrayOfNulls(size)
        }
    }
}