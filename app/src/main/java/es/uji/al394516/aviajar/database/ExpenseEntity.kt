package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = CASCADE,
        entity = TravelEntity::class,
        parentColumns = ["id"],
        childColumns = ["tavelID"]
    )]
)
data class ExpenseEntity(
    @PrimaryKey val name:String,
    val tavelID:Int,
    val price:Double
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(tavelID)
        parcel.writeDouble(price)
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
