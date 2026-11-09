package com.example.jetpackcomposedemos.features.orders.data

import com.example.jetpackcomposedemos.features.auth.data.storage.FakeTokenStore
import com.example.jetpackcomposedemos.features.auth.domain.SessionManager
import com.example.jetpackcomposedemos.features.auth.domain.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MyOrdersRepositoryTest {

    private class RecordingDataSource : MyOrdersDataSource {
        var requestCustomerId: Int? = null
        override suspend fun getOrders(
            customerId: Int,
            page: Int,
            pageSize: Int
        ): MyOrderResponseDto {
            requestCustomerId = customerId
            return MyOrderResponseDto(
                customerId = customerId,
                page = page,
                pageSize = pageSize,
                totalOrders = 0,
                orders = emptyList()
            )
        }
    }

    private val dataSource = RecordingDataSource()
    private val session = SessionManager(
        FakeTokenStore(),
        CoroutineScope(Dispatchers.Unconfined)
    )
    private val repository = MyOrdersRepository(dataSource, session)

    @Test
    fun `asks for the signed-in customer's orders`() = runTest {

        session.signIn(User(id = "11000", username = "Cara Cole", token = ""))

        repository.getOrders()

        assertEquals(11000, dataSource.requestCustomerId)

    }

    @Test
    fun `refuses to load orders when nobody is signed in`() = runTest {

        val result = runCatching { repository.getOrders() }

        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals(null, dataSource.requestCustomerId)
    }
}