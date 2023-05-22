package com.idutvuk.go_maf.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.database.MafiaGame
import com.idutvuk.go_maf.ui.ui.theme.Typography
import java.lang.StringBuilder

@Composable
fun GameItemCard(game: MafiaGame, onItemClicked: (game: MafiaGame) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(game) }),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                )
        ) {
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = StringBuilder(
                        game.date.toString()
                    ).append(
                        " | "
                    ).append(
                        game.time.toString()
                    ).toString(),
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    fontWeight = FontWeight.Bold,
                    style = Typography.titleMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.Bottom) {

                    val numPlayersIcon: Painter = painterResource(id = R.drawable.ic_people)

                    Icon(
                        painter = numPlayersIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp, 16.dp)
                    )
                    Text(
                        text = game.numPlayers.toString(),
                        modifier = Modifier
                            .padding(8.dp, 12.dp, 12.dp, 0.dp),
                        style = Typography.bodySmall
                    )
                    UserProfilePicturesRow(
//                        pictures = game.players.map { it.pictureId },
                        modifier = Modifier.weight(0.8f)
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                GenderTag(isActive = game.isCompleted)
            }
        }
    }
}

@Preview(
    name = "Light theme"
)
@Composable
fun GameItemsPreview() {
    LazyColumn {
        items(MafiaGame.games) { game ->
            GameItemCard(game = game, onItemClicked = {})
        }
    }
}

val picturesList = arrayListOf(
    R.drawable.black7,
    R.drawable.red3,
    R.drawable.black2,
    R.drawable.black3,
)
