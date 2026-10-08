package com.example.jetpackcomposedemos.features.orders.domain

interface OrderRespository {

    suspend fun getOrders(): List<Order>

}