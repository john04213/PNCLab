package com.example.jetpackcomposedemos.testutil.builders

import com.example.jetpackcomposedemos.features.orders.data.MyOrderDto


import com.example.jetpackcomposedemos.features.orders.data.MyOrderResponseDto

fun myOrdersDto(
   customerId: Int = 11000,
   vararg orders: MyOrderDto
) = MyOrderResponseDto(
    customerId = customerId,
    page = 1,
    pageSize = 1,
    totalOrders = orders.size,
    orders = orders.toList()

)
