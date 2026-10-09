package com.example.jetpackcomposedemos.features.orders.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class OrderWithLineItemsEntity (
    @Embedded
    var order: OrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    var lineItems: List<LineItemEntity>

)