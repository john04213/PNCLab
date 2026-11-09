package com.example.jetpackcomposedemos.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposedemos.R
import com.example.jetpackcomposedemos.features.orders.domain.Order
import java.time.format.DateTimeFormatter

@Composable
fun OrderSummary(
    order: Order
) {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(stringResource(R.string.order_number, order.id))
        Text("Customer: ${order.firstName} ${order.lastName}")
        Text("Order Date: ${order.orderDate.format(formatter)}")
        Text("Ship Date: ${order.shipDate.format(formatter)}")
    }
}


