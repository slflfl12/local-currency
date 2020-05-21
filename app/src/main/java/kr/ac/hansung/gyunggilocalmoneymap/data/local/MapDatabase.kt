package kr.ac.hansung.gyunggilocalmoneymap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.ac.hansung.gyunggilocalmoneymap.data.local.dao.MapDao
import kr.ac.hansung.gyunggilocalmoneymap.data.local.model.MapEntity


@Database(version = 1, entities = [MapEntity::class], exportSchema = false)
abstract class MapDatabase : RoomDatabase() {

    abstract fun mapDao(): MapDao

}