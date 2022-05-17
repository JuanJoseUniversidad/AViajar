package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import es.uji.al394516.aviajar.database.PersonEntity
import es.uji.al394516.aviajar.database.TravelEntity

typealias Personid = Int
typealias Travelid = Int

class Person(val id: Personid, val name:String ,val travelID:Travelid) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    constructor(personEntity: PersonEntity) : this(personEntity.id,personEntity.name,personEntity.travelID) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(travelID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

}