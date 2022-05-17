package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.TravelEntity

class Travel(val travelEntity: TravelEntity, val people:List<Person>, val expenses: List<Expense>) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(TravelEntity::class.java.classLoader)!!,
        parcel.createTypedArrayList(Person)!!,
        parcel.createTypedArrayList(Expense)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(travelEntity, flags)
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