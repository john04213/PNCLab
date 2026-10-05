package com.example.jetpackcomposedemos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun BoardMemberDetails(boardMember: BoardMember) {

    Column() {

        Text(
            text = "${boardMember.firstName} ${boardMember.lastName}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(boardMember.title)

        AsyncImage(
            model = "https://kazoopromotions.com${boardMember.imageUrl}",
            contentDescription = "picture of ${boardMember.firstName} ${boardMember.lastName}",
            modifier = Modifier.size(250.dp)
        )

        // bio content is HTML
        Text(AnnotatedString.fromHtml(boardMember.bio))
    }
}
