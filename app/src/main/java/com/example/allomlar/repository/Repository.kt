package com.mac.allomalar.repository

import com.mac.allomalar.db.daos.CenturyDao
import com.mac.allomalar.db.database.AppDatabase
import com.mac.allomalar.internet.ApiService
import com.mac.allomalar.models.*
import javax.inject.Inject


class Repository @Inject constructor(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) {
    private val centuryDao = appDatabase.centuryDao()
    private val allomaAndSubjectsDao = appDatabase.allomaAndSubjectDao()
    private val allomasDao = appDatabase.allomaDao()
    private val madrasaAndAllomasDao = appDatabase.madrasaAndAllomasDao()
    private val madrasaDao = appDatabase.madrasaDao()
    private val subjectDao = appDatabase.subjectDao()
    private val bookDao = appDatabase.bookDao()
    private val scienceDao = appDatabase.scienceDao()
    private val madrasaAndYears = appDatabase.madrasaAndYearsDao()
    private val imageDao = appDatabase.imageDao()
    private val subjectInfoDao = appDatabase.subjectInfoDao()

    //Read Room
    suspend fun getAllCenturiesFromRoom() = centuryDao.getAllCenturies()
    suspend fun insertAllData(list: List<Century?>?) = centuryDao.insertAll(list)

    suspend fun getAllAllomaAndSubjectsFromRoom() = allomaAndSubjectsDao.getAllomaAndSubjects()
    suspend fun insertAllomaAndSubjectsAll(list: List<AllomaAndSubjects>) =
        allomaAndSubjectsDao.insertAllomaAndSubjectsAll(list)

    suspend fun insertAllomaAndSubjects(allomaAndSubject: AllomaAndSubjects) =
        allomaAndSubjectsDao.insertAllomaAndSubjects(allomaAndSubject)

    suspend fun getAllAllomasFromRoom() = allomasDao.getAllAllomas()
    suspend fun getAllomaFromRoom(id: Int) = allomasDao.getAlloma(id)
    suspend fun insertAlloma(alloma: Alloma) = allomasDao.insertAlloma(alloma)
    suspend fun insertAllomas(list: List<Alloma?>?) = allomasDao.insertAllomas(list)

    suspend fun getAllMadrasaAndAllomasFromRoom() = madrasaAndAllomasDao.getAllMadrasaAndAllomas()
    suspend fun insertMadrasaAndAllomas(madrasaAndAllomas: MadrasaAndAllomas) =
        madrasaAndAllomasDao.insertMadrasaAndAllomas(madrasaAndAllomas)

    suspend fun insertMadrasaAndAllomasAll(list: List<MadrasaAndAllomas?>?) =
        madrasaAndAllomasDao.insertMadrasaAndAllomasAll(list)

    suspend fun getAllMadrasasFromRoom() = madrasaDao.getAllMadrasas()
    suspend fun insertMadrasas(list: List<Madrasa?>?) = madrasaDao.insertMadrasas(list)

    suspend fun getAllSubjectsFromRoom(allomaId: Int) = subjectDao.getSubjects(allomaId)
    suspend fun insertSubject(subject: Subject) = subjectDao.insertSubject(subject)
    suspend fun insertSubjectsAll(list: List<Subject?>?) = subjectDao.insertSubjectsAll(list)

    suspend fun getAllBooksOfAllomaFromRoom(allomaId: Int) = bookDao.getAllBookOfAlloma(allomaId)
    suspend fun insertBooks(list: List<Book?>?) = bookDao.insertAllBooks(list)

    suspend fun getAllScienceOfAllomaFromRoom(allomaId: Int) =
        scienceDao.getAllScienceOfAlloma(allomaId)

    suspend fun insertSciences(list: List<Science?>?) = scienceDao.insertAllScience(list)


    suspend fun getImageFromRoomById(imageId: String) = imageDao.getImageById(imageId)
    suspend fun insertImage(image: Image) = imageDao.insertImage(image)

    suspend fun getAllSubjectInfoFromRoom(subjectId: Int) =
        subjectInfoDao.getSubjects(subjectId)

    suspend fun insertSubjectInfoAll(list: List<SubjectInfo?>?) =
        subjectInfoDao.insertSubjectsAll(list)


    //ApiService
    suspend fun getEachAlloma(id: Int) = apiService.getAlloma(id)
    suspend fun getAllCenturies() = apiService.getCenturies()
    suspend fun getAllMadrasas() = apiService.getMadrasas()
    suspend fun getMadrasaAndAllomas() = apiService.getMadrasaAndAllomas()
    suspend fun getAllSubjectsOfAlloma(allomaId: Int) = apiService.geAllSubjectsOfAlloma(allomaId)
    suspend fun getAllBooks() = apiService.getAllBooks()
    suspend fun getAllScience() = apiService.getScience()
    suspend fun getAllSubjectsInfo(allomaId: Int) = apiService.getSubjectInfo(allomaId)
}