package com.example.jetpackcomposedemos.features.orders.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.jetpackcomposedemos.features.auth.domain.SessionManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import com.example.jetpackcomposedemos.features.orders.domain.Order

@Singleton
class PagedOrdersRepository @Inject constructor(
    private val dataSource: MyOrdersDataSource,
    private val session: SessionManager
) {

    companion object {
        const val PAGE_SIZE = 5
    }

    fun pagedOrders(pageSize: Int = PAGE_SIZE): Flow<PagingData<Order>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { OrdersPagingSource(dataSource, session, pageSize) }
        ).flow
}