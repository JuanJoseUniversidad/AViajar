package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO Hacer relaciones

@Entity(

)
data class TravelEntity(
    @PrimaryKey val id: Int,
    val name:String,
    val place:String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(place)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelEntity> {
        override fun createFromParcel(parcel: Parcel): TravelEntity {
            return TravelEntity(parcel)
        }

        override fun newArray(size: Int): Array<TravelEntity?> {
            return arrayOfNulls(size)
        }
    }
}
