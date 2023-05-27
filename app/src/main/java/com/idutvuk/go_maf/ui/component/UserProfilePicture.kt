package com.idutvuk.go_maf.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R


@Composable
fun UserProfilePicture (
    imageRes: Int = R.drawable.ic_people,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Black
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,            // crop the image if it's not a square
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)                       // clip to the circle shape
            .border(1.dp, borderColor, CircleShape)   // add a border (optional)
    )
}

@Preview
@Composable
fun UserProfilePicturesRow(
    pictures: List<Int> = picturesList,
    backgroundColor: Color = Color.Transparent,
    modifier: Modifier = Modifier
) { //todo automatize
    Row (modifier = modifier){
        for (i in pictures.indices) {
            UserProfilePicture(
                pictures[i],
                modifier = Modifier.offset(
                    x= (-8*i).dp
                ),
                borderColor = backgroundColor
            )
        }
    }
}

