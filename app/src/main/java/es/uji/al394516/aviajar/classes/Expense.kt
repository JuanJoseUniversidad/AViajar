package es.uji.al394516.aviajar.classes

import android.os.Parcel
import android.os.Parcelable
import es.uji.al394516.aviajar.database.ExpenseEntity

class Expense(val expenseEntity: ExpenseEntity, val person_money: MutableMap<Int,Double>):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ExpenseEntity::class.java.classLoader)!!,
        TODO("person_money")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(expenseEntity, flags)
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