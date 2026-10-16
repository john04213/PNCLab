package com.example.jetpackcomposedemos.features.artists.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.jetpackcomposedemos.core.room.AppDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import app.cash.turbine.test
import com.example.jetpackcomposedemos.testutil.builders.anArtistEntity
import kotlinx.coroutines.flow.first

class ArtistDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ArtistDao

    @Before
    fun createDb(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.artistDao()
    }

    @After
    fun closeDb() {
        db.close()
    }


    @Test
    fun givenEmptyTable_whenObserved_thenEmitsAnEmptyList() = runTest {
        dao.observeArtists().test {
            assertEquals(emptyList<ArtistEntity>(), awaitItem())
        }
    }

    @Test
    fun givenObservedList_whenArtistsAreInserted_thenNewListEmited() = runTest {
        dao.observeArtists().test {
            assertEquals(emptyList<ArtistEntity>(), awaitItem())
            dao.upsertArtist(listOf(anArtistEntity(id = 1), anArtistEntity(id = 2)))

            val ids = awaitItem().map {it.id }.sorted()
            assertEquals(listOf(1,2), ids)
        }
    }


    @Test
    fun givenExistingArtist_whenUpsertedWithTheSameId_thenRowIsReplacedNotDuplicated() = runTest {
        //should insert
        dao.upsertArtist(listOf(anArtistEntity(id = 1, name = "Old Name")))
        // should update, not append
        dao.upsertArtist(listOf(anArtistEntity(id = 1, name = "New Name")))

        val artists = dao.observeArtists().first()

        assertEquals(listOf("New Name"), artists.map {it.name})
    }

    @Test
    fun givenExistingArtist_whenReplaceAll_thenInsertUpdatesAndDeleteInOneStep() = runTest {
        dao.upsertArtist(listOf(
            anArtistEntity(id = 1, name = "Abe"),
            anArtistEntity(id = 2, name = "Bob"),
            anArtistEntity(id = 3, name = "Carol")
        ))

        dao.replaceAll(listOf(
            anArtistEntity(id = 1, name = "Abe Updated"),
            anArtistEntity(id = 4, name = "Dee")
        ))

        val artists = dao.observeArtists().first().sortedBy { it.id }
        // test whether inserts and deletes happened properly
        assertEquals(listOf(1,4), artists.map{ it.id })

        // tests whether the updates happened properly
        assertEquals(listOf("Abe Updated", "Dee"), artists.map { it.name})
    }

    @Test
    fun givenNoSuchArtist_whenObservedById_thenEmitsNull() = runTest {
        dao.observeArtist(42).test {
            assertEquals(null, awaitItem())
        }
    }
    @Test
    fun givenExistingArtist_whenObservedById_thenEmitsThatArtist() = runTest {

        val artist = anArtistEntity(id = 1, name = "Abe")
        dao.upsertArtist(listOf(artist))
//        dao.observeArtist(1).test {
//            assertEquals(anArtistEntity(id = 1, name = "Abe"), awaitItem())
//        }
        val result = dao.observeArtist(1).first()

        assertEquals(artist, result)
    }

    @Test
    fun givenObservedArtist_whenADifferentArtistIsInserteD_thenTheSameValueIsEmittedAgain() = runTest {
        dao.upsertArtist(listOf(anArtistEntity(id = 1, name = "Abe")))

        dao.observeArtist(1).test {
            val first = awaitItem()
            dao.upsertArtist(listOf(anArtistEntity(id = 2, name = "Bob")))
            assertEquals(first, awaitItem())
        }
    }
}