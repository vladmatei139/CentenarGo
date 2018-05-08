package echipa_8.centenargo_app.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ImageDAO {
    @Query("SELECT * FROM Image")
    List<Image> getAll();

    @Query("SELECT * FROM Image WHERE id IN (:imageIds)")
    List<Image> loadAllByIds(int[] imageIds);

    @Query("SELECT * FROM Image WHERE id = :imageId")
    Image findById(int imageId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Image... images);

    @Delete
    void delete(Image... image);
}
