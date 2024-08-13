package local


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.xapp.quickbit.SampleEntity

@Dao
interface SampleDao {

    // Insert a single entity into the database
    @Insert
    suspend fun insert(sampleEntity: SampleEntity)

    // Insert multiple entities into the database
    @Insert
    suspend fun insertAll(vararg sampleEntities: SampleEntity)

    // Update an existing entity in the database
    @Update
    suspend fun update(sampleEntity: SampleEntity)

    // Delete an entity from the database
    @Delete
    suspend fun delete(sampleEntity: SampleEntity)

    // Query to get all entities from the table
    @Query("SELECT * FROM sample_table")
    suspend fun getAll(): List<SampleEntity>

    // Query to get an entity by its ID
    @Query("SELECT * FROM sample_table WHERE id = :id")
    suspend fun getById(id: Int): SampleEntity?
}
