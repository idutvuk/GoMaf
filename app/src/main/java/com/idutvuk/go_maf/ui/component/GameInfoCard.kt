package com.idutvuk.go_maf.ui.component


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R

@Composable
fun GameInfoCard(date: String, time:String, isOver: String, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(
                text = date,
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {

                val locationIcon: Painter = painterResource(id = R.drawable.ic_people)

                Icon(
                    painter = locationIcon,
                    contentDescription = "Users count",
                    modifier = Modifier.size(16.dp, 16.dp)
                )

                Text(
                    text = duration,
                    modifier = Modifier.padding(8.dp, 12.dp, 12.dp, 0.dp),
                )
            }
            

            Text(
                text = "12 mins ago",
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
            )
        }
    }
}



@Preview(
    showBackground = true
)
@Composable
fun CardPreview() {
//    GameInfoCard(name = "das", gender = "hz", location = "my haus")
    GameInfoCard(
        date = "02/09/23",
        time = "19:26",
        isOver = "gender",
        duration = "43:55")
}