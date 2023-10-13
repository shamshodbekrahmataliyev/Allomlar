package com.mac.allomalar.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mac.allomalar.models.*


@Dao
interface SubjectInfoDao {

    @Query("Select*from subject_info where subject =:subjectId")
    suspend fun getSubjects(subjectId: Int): List<SubjectInfo>

    @Insert
    suspend fun insertSubjectInfo(subject: Subject)

    @Insert(onConflict = REPLACE)
    suspend fun insertSubjectsAll(list: List<SubjectInfo?>?)
}