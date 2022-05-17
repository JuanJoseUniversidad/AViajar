package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(

)
data class ExpenseEntity(
    @PrimaryKey val nombre:String,
    val tavelID:Int,
    val price:Float
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeInt(tavelID)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpenseEntity> {
        override fun createFromParcel(parcel: Parcel): ExpenseEntity {
            return ExpenseEntity(parcel)
        }

        override fun newArray(size: Int): Array<ExpenseEntity?> {
            return arrayOfNulls(size)
        }
    }
}
