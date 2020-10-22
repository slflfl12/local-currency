package kr.ac.hansung.localcurrency.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map")
data class MapEntity(

        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        val id: Long,

        @ColumnInfo(name = "title")
        val title: String?,

        @ColumnInfo(name = "telePhone")
        val telePhone: String?,

        @ColumnInfo(name = "roadAddress")
        val roadAddress: String?,

        @ColumnInfo(name = "latitude")
        val latitude: String?,

        @ColumnInfo(name = "latitude_double")
        val latitudeDouble: Double?,

        @ColumnInfo(name = "longitude")
        val longitude: String?,

        @ColumnInfo(name = "longitude_double")
        val longitudeDouble: Double?,


        @ColumnInfo(name = "sigun")
        val sigun: String?,

        @ColumnInfo(name = "category")
        val category: String?


)