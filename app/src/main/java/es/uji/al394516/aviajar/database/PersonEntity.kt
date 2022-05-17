package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(

)
data class PersonEntity(
    @PrimaryKey val id: Int,
    val name:String,
    val travelID:Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(travelID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonEntity> {
        override fun createFromParcel(parcel: Parcel): PersonEntity {
            return PersonEntity(parcel)
        }

        override fun newArray(size: Int): Array<PersonEntity?> {
            return arrayOfNulls(size)
        }
    }
}
