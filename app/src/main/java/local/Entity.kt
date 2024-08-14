package local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "simple_entity")
data class SimpleEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String
)
