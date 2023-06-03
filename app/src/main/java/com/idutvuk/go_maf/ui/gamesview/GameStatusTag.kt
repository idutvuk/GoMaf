package com.idutvuk.go_maf.ui.gamesview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.idutvuk.go_maf.R
import com.idutvuk.go_maf.ui.theme.Typography

@Composable
fun GameStatusTag(isActive: Boolean) { //TODO rename to proper name
    if (isActive) {
        ChipView(gender = "Active", colorResource = colorResource(id = R.color.light_green))
    } else {
        ChipView(gender = "Finished", colorResource = colorResource(id = R.color.blue))
    }
}

@Composable
fun ChipView(gender: String, colorResource: Color) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(colorResource.copy(.08f))
    ) {
        Text(
            text = gender, modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
            style = Typography.bodySmall,
            color = colorResource
        )
    }
}
