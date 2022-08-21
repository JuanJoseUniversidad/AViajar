package es.uji.al394516.aviajar.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(

)
data class CountryEntity(
    @PrimaryKey val id:String   //esto debería hacerse con un int y columnas según el idioma
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CountryEntity> {
        override fun createFromParcel(parcel: Parcel): CountryEntity {
            return CountryEntity(parcel)
        }

        override fun newArray(size: Int): Array<CountryEntity?> {
            return arrayOfNulls(size)
        }
    }

}
