package com.idutvuk.go_maf.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.ui.ui.theme.Typography
import java.util.Random

@Composable
fun GameItemCard(dog: Dog, onItemClicked: (dog: Dog) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onItemClicked(dog) }),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val random = Random()
            val id = if(random.nextBoolean()) R.drawable.black3 else R.drawable.red2
            val image: Painter = painterResource(id = id)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = dog.name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    fontWeight = FontWeight.Bold,
                    style = Typography.bodySmall
                )

                Row(verticalAlignment = Alignment.Bottom) {

                    val numPlayersIcon: Painter = painterResource(id = R.drawable.ic_people)

                    Icon(
                        painter = numPlayersIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp, 16.dp)
                    )

                    Text(
                        text = dog.location, //TODO: replace with playercount
                        modifier = Modifier.padding(8.dp, 12.dp, 12.dp, 0.dp),
                        style = Typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                UserProfilePicturesRow(
                    backgroundColor = MaterialTheme.colorScheme.surfaceTint
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                GenderTag(isActive = true) //TODO replace with isGameActive
            }
        }
    }
}

@Preview(
    name = "Light theme"
)
@Composable
fun ItemPreview() {
    Column {
        GameItemCard(dog = fakeDatasetOfDogs[0], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[1], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[2], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[0], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[1], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[2], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[0], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[1], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[2], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[0], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[1], onItemClicked = {})
        GameItemCard(dog = fakeDatasetOfDogs[2], onItemClicked = {})
    }

}

data class Dog(
    val id: Int,
    val name: String,
    val age: Double,
    val gender: String,
    val color: String,
    val weight: Double,
    val location: String,
    val image: Int,
    val about: String,
    val owner: Owner,
)

data class Owner(val name: String, val bio: String)


val fakeDatasetOfDogs = arrayListOf(
    Dog(
        id = 1,
        name = "23/05/2023 | 17:03",
        age = 3.5,
        gender = "Male",
        color = "Golden",
        weight = 25.5,
        location = "10", //players count
        image = R.drawable.ic_people,
        about = "Buddy is a playful and energetic dog who loves to go on walks and play fetch. He gets along well with other dogs and people.",
        owner = Owner(
            name = "Jessica",
            bio = "I've been a dog lover my whole life and I'm so excited to have Buddy as my furry companion.",
        )
    ),

    Dog(
        id = 2,
        name = "Lola",
        age = 2.0,
        gender = "Female",
        color = "Black and White",
        weight = 12.0,
        location = "New York, NY",
        image = R.drawable.ic_people,
        about = "Lola is a sweet and gentle dog who loves to cuddle. She's a bit shy at first but warms up quickly to new people.",
        owner = Owner(
            name = "David",
            bio = "I adopted Lola from a shelter and she's been the best thing to ever happen to me. She's my constant companion and I love her to bits.",
        )
    ),

    Dog(
        id = 3,
        name = "Max",
        age = 5.0,
        gender = "Male",
        color = "Brown",
        weight = 30.0,
        location = "Chicago, IL",
        image = R.drawable.ic_people,
        about = "Max is a loyal and protective dog who loves to play and go on adventures. He can be a bit stubborn at times but is always eager to please.",
        owner = Owner(
            name = "Sophie",
            bio = "Max is my best friend and I couldn't imagine my life without him. He's always by my side and I wouldn't have it any other way.",
        )
    )
)

val picturesList = arrayListOf(
    R.drawable.black7,
    R.drawable.red3,
    R.drawable.black2,
    R.drawable.black3,
)
