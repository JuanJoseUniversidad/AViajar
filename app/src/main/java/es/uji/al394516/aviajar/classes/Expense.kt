package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.ExpenseEntity
import es.uji.al394516.aviajar.database.PersonEntity

class Expense(val name:String, val tavelID:Int, val price:Float, val person_money: MutableMap<Personid,Double>) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readFloat(),
        TODO("person_money")
    ) {
    }

    constructor(expenseEntity: ExpenseEntity) : this(expenseEntity.name,expenseEntity.tavelID,expenseEntity.price, mutableMapOf()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(tavelID)
        parcel.writeFloat(price)
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