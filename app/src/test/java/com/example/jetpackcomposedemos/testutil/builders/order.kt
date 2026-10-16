package com.example.jetpackcomposedemos.testutil.builders

import java.time.LocalDateTime
import com.example.jetpackcomposedemos.features.orders.domain.LineItem
import com.example.jetpackcomposedemos.features.orders.domain.Order

class OrderBuilder {
    var id: Int = 1
    var customerId: Int = 1
    var firstName: String = "ada"
    var lastName: String = "Lovelace"
    var orderDate: LocalDateTime = LocalDateTime.of(2024, 1, 1,0, 0)
    var shipDate: LocalDateTime? = null // delay setting the default value

    private val items = mutableListOf<LineItem>()

    fun item(
        productName: String = "Widget",
        qty: Int = 1,
        unitPrice: Double = 10.0,
    ) {
        items += aLineItem(
            id = items.size + 1,
            productName = productName,
            orderQty = qty,
            unitPrice = unitPrice
        )
    }

    fun build() = Order(
        id = id,
        customerId = customerId,
        firstName = firstName,
        lastName = lastName,
        orderDate = orderDate,
        shipDate = shipDate ?: orderDate.plusDays(2), // computer default
        items = items.toList()

        )
}

fun anOrder(block: OrderBuilder.() -> Unit = {}): Order  =
    OrderBuilder().apply(block).build()