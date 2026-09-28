package com.example.jetpackcomposedemos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun BoardMemberCard(boardMember: BoardMember) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(){
            Text(
                text = boardMember.firstName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = boardMember.lastName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp)
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun BoardMemberCardPreview() {
    JetpackComposeDemosTheme {
        val boardMember = BoardMember(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            imageUrl = "Image URL",
            title = "Software Engineer",
            gender = "male",
            bio = "Junior Software Engineer at PNC banking"
        )
        BoardMemberCard(
            boardMember = boardMember
        )
    }
}