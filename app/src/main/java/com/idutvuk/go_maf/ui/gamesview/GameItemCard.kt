package com.idutvuk.go_maf.ui.gamesview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.model.database.entities.MafiaGame
import com.idutvuk.go_maf.ui.theme.Typography
import java.text.SimpleDateFormat

@Composable
fun GameItemCard(
    game: MafiaGame,
    onItemClicked: (gameId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(game.id) }),
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
                Row {
                    Text(
                        text = SimpleDateFormat("dd/M/yyyy hh:mm").format(game.startTime),
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        fontWeight = FontWeight.Bold,
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = SimpleDateFormat("hh:mm:ss").format(game.duration),
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        style = Typography.titleSmall
                    )
                }
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
                GenderTag(isActive = game.isOver)
            }
        }
    }
}

val picturesList = arrayListOf(
    R.drawable.black7,
    R.drawable.red3,
    R.drawable.black2,
    R.drawable.black3,
)
