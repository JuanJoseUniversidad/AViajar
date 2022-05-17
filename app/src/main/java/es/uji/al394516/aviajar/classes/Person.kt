package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.PersonEntity
import es.uji.al394516.aviajar.database.TravelEntity

class Person(val personEntity: PersonEntity):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(PersonEntity::class.java.classLoader)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(personEntity, flags)
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