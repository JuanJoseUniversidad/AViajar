package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.PersonEntity
import es.uji.al394516.aviajar.database.TravelEntity

class Travel(val id: Int, val name:String, val place:String, val people:List<Person>, val expenses: List<Expense>) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Person)!!,
        parcel.createTypedArrayList(Expense)!!
    ) {
    }

    constructor(travelEntity: TravelEntity) : this(travelEntity.id,travelEntity.name,travelEntity.place,
        listOf(), listOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(place)
        parcel.writeTypedList(people)
        parcel.writeTypedList(expenses)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Travel> {
        override fun createFromParcel(parcel: Parcel): Travel {
            return Travel(parcel)
        }

        override fun newArray(size: Int): Array<Travel?> {
            return arrayOfNulls(size)
        }
    }

}