package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = PersonEntity::class,
        parentColumns = ["id"],
        childColumns = ["personID"]
    ),
    ForeignKey(
        entity = ExpenseEntity::class,
        parentColumns = ["name"],
        childColumns = ["expeseName"]
    )],
    primaryKeys = ["personID","expeseName"]
)
data class PersonExpenseEntity(
    val personID: Int,
    val expeseName:String,
    val price:Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(personID)
        parcel.writeString(expeseName)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PersonExpenseEntity> {
        override fun createFromParcel(parcel: Parcel): PersonExpenseEntity {
            return PersonExpenseEntity(parcel)
        }

        override fun newArray(size: Int): Array<PersonExpenseEntity?> {
            return arrayOfNulls(size)
        }
    }

}
