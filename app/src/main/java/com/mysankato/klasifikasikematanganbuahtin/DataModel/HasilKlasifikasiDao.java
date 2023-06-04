package com.mysankato.klasifikasikematanganbuahtin.DataModel;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HasilKlasifikasiDao {
    //Get all data
    @Query("SELECT * FROM hasilKlasifikasi")
    List<HasilKlasifikasi> getAll();

    @Insert
    void insertHasilKlasifikasi(HasilKlasifikasi hasilKlasifikasi);

    @Delete
    void deleteHasilKlasifikasi(HasilKlasifikasi hasilKlasifikasi);

}
