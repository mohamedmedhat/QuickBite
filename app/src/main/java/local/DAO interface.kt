package local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface SimpleEntityDao {

    // Insert a new SimpleEntity into the database
    @Insert
    suspend fun insert(entity: SimpleEntity)

    // Update an existing SimpleEntity in the database
    @Update
    suspend fun update(entity: SimpleEntity)

    // Delete a SimpleEntity from the database
    @Delete
    suspend fun delete(entity: SimpleEntity)

    // Query to get a SimpleEntity by its ID
    @Query("SELECT * FROM simple_entity WHERE id = :id")
    suspend fun getById(id: Int): SimpleEntity?

    // Query to get all SimpleEntities
    @Query("SELECT * FROM simple_entity")
    suspend fun getAll(): List<SimpleEntity>
}
