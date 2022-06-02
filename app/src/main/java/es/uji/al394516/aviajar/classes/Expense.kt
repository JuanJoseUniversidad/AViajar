package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.ExpenseEntity
import es.uji.al394516.aviajar.database.PersonEntity

class Expense(val id:Int, val name:String, val tavelID:Int, val price:Double, val person_money: MutableMap<Personid,Double>) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readHashMap(MutableMap::class.java.classLoader) as MutableMap<Personid,Double>
    ) {
    }

    constructor(expenseEntity: ExpenseEntity) : this(expenseEntity.id,expenseEntity.name,expenseEntity.tavelID,expenseEntity.price, mutableMapOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(tavelID)
        parcel.writeDouble(price)
        parcel.writeMap(person_money)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }

}