package com.example.jetpackcomposedemos.features.orders.presentation

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.example.jetpackcomposedemos.core.LoadableState
import com.example.jetpackcomposedemos.features.orders.data.FakeOrderRepository
import com.example.jetpackcomposedemos.features.orders.domain.GetOrdersUseCase
import com.example.jetpackcomposedemos.features.orders.domain.OrderRepository
import com.example.jetpackcomposedemos.testutil.builders.anOrder
import com.example.jetpackcomposedemos.testutil.user.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import com.example.jetpackcomposedemos.features.orders.domain.Order
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle

class OrdersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModelWidth(repo: OrderRepository): OrdersViewModel {
        return OrdersViewModel(GetOrdersUseCase(repo))
    }

    @Test
    fun `starts in loading state before anything is requested`() = runTest{

        val viewModel = viewModelWidth(FakeOrderRepository())

        viewModel.uiState.test{
            assertEquals(LoadableState.Loading, awaitItem())
        }
    }

    @Test
    fun `given orders are available, when loading, then moves from Loading to Success`() = runTest{
        val orders = listOf(anOrder{id = 1})
        val viewModel = viewModelWidth(FakeOrderRepository(orders))

        viewModel.uiState.test {
            assertEquals(LoadableState.Loading, awaitItem())

            viewModel.loadData()

            assertEquals(LoadableState.Success(orders), awaitItem())
        }
    }

    @Test
    fun `given the repository fails, when loading, then moves from Loading to Error with its message`()
    = runTest {

        val repo = FakeOrderRepository(failure = IOException("network down"))

        val viewModel = viewModelWidth(repo)

        viewModel.uiState.test {
            assertEquals(LoadableState.Loading, awaitItem())

            viewModel.loadData()

            assertEquals(LoadableState.Error("network down"), awaitItem())
        }
    }

    @Test
    fun `given an earlier failure, when trying, then shows Loading again before Success`() = runTest{
        val orders = listOf(anOrder{ id = 1 })
        var shouldFail = true
        val repo = object: OrderRepository {
            override suspend fun getOrders(): List<Order> {
                if(shouldFail) throw IOException("network down")
                return orders
            }
        }

        val viewModel = viewModelWidth(repo)

        viewModel.uiState.test {
            assertEquals(LoadableState.Loading, awaitItem())

            viewModel.loadData()
            assertEquals(LoadableState.Error("network down"), awaitItem())

            shouldFail = false
            viewModel.loadData()
            assertEquals(LoadableState.Loading, awaitItem())
            assertEquals(LoadableState.Success(orders), awaitItem())
        }

    }

    @Test
    fun `given the load is cancelled mid-flight, when the scope is cancelled, then does not report an error`() = runTest {
        val repo = object : OrderRepository {
            override suspend fun getOrders(): List<Order> {
                delay(5_000)
                return emptyList()
            }
        }
        val viewModel = viewModelWidth(repo)

        viewModel.loadData()
        advanceTimeBy(1_000)
        viewModel.viewModelScope.cancel()
        advanceUntilIdle()
        assertEquals(LoadableState.Loading, viewModel.uiState.value)
    }

}