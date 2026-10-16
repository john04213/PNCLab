package com.example.jetpackcomposedemos.features.artists.domain

import com.example.jetpackcomposedemos.features.artists.data.FakeArtistRepository
import com.example.jetpackcomposedemos.testutil.builders.anArtist
import kotlinx.coroutines.test.runTest
import org.junit.Test
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.io.IOException

class GetAvailableArtistUseCaseTest {

//    @Test
//    fun `never finishes on a load that never completes`() = runTest(timeout = 2.seconds) {
//        val repo = FakeArtistRepository(listOf(anArtist()))
//    }

    @Test
    fun `emits cached artists filtered to available and sorted by name`() = runTest {
        val repo = FakeArtistRepository(
            listOf(
                anArtist(id = 2, name = "Zed"),
                anArtist(id = 1, name = "Abe"),
                anArtist(id = 3, name = "Hidden", isAvailable = false)

            )
        )

        GetAvailableArtistsUseCase(repo)().test {
            assertEquals(listOf("Abe", "Zed"), awaitItem().map { it.name })
        }   // turbine stops collecting the flow of data
        // and also asserts that nothing is left unconsumed in the flow
    }

    @Test
    fun `emits again when the underlying data changed`() = runTest {
        val repo = FakeArtistRepository(listOf(anArtist(id = 1, name = "Abe")))

        GetAvailableArtistsUseCase(repo)().test {
            assertEquals(listOf("Abe"), awaitItem().map { it.name })

            // change the repo data
            repo.artists.value = listOf(
                anArtist(id = 1, name = "Abe"),
                anArtist(id = 2, name = "Bea")
            )
            assertEquals(listOf("Abe", "Bea"), awaitItem().map { it.name })

            repo.artists.value = listOf(
                anArtist(id = 1, name = "Abe", isAvailable = false)
            )
            assertEquals(emptyList<Artist>(), awaitItem())
        }
    }

    @Test
    fun `does not emit again when the same artists are pushed`() = runTest {
        val repo = FakeArtistRepository(listOf(anArtist(id = 1)))

        GetAvailableArtistsUseCase(repo)().test {
            skipItems(1) // skip the initial emission// we don't care about the initial emission

            repo.artists.value = listOf(anArtist(id = 1))     // equal list, new instance

            expectNoEvents()
        }
    }

    @Test
    fun `propegates a failure from the repository flow`() = runTest {
        // new (to us) syntax to create a fake on-on-the-fly, inside our test
        val failing = object : ArtistRepository by FakeArtistRepository() {
            override fun observeArtists(): Flow<List<Artist>> = flow {
                emit(listOf(anArtist(name = "Abe")))
                throw IOException("db closed")
            }
        }
        // object: ArtistRepository nu FakeArtistRepository()
        // this is called interface delegation

        GetAvailableArtistsUseCase(failing)().test {

            assertEquals(listOf("Abe"), awaitItem().map { it.name })
            assertEquals("db closed", awaitError().message)

        }

    }

    @Test
    fun `completes when the repository flow completes`() = runTest {
        val finite = object : ArtistRepository by FakeArtistRepository() {
            override fun observeArtists(): Flow<List<Artist>> = flowOf(listOf(anArtist()))
        }

        GetAvailableArtistsUseCase(finite)().test {
            skipItems(1)
            awaitComplete()
        }
    }

}