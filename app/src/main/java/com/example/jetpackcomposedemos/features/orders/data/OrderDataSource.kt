package com.example.jetpackcomposedemos.features.orders.data

interface OrderDataSource {

    suspend fun getOrders(): List<OrderLineItemDto>

}


