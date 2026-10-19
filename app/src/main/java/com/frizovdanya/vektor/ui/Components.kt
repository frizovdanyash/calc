package com.frizovdanya.vektor.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SurfaceCard(
    palette: AppPalette,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = palette.surface,
        border = BorderStroke(1.dp, palette.outline.copy(alpha = if (palette.isDark) 0.55f else 0.72f)),
        content = content
    )
}

@Composable
fun BrandHeader(
    palette: AppPalette,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(palette.display, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("v.", color = Color(0xFF8CE6C6), fontWeight = FontWeight.Black, fontSize = 23.sp)
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                "VEKTOR",
                color = palette.text,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.8.sp,
                fontSize = 16.sp
            )
            Text("Считай. Понимай. Решай.", color = palette.muted, fontSize = 12.sp)
        }
        trailing?.invoke()
    }
}

@Composable
fun ScreenHeader(
    eyebrow: String,
    title: String,
    subtitle: String,
    palette: AppPalette
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            eyebrow.uppercase(),
            color = palette.accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.45.sp
        )
        Text(
            title,
            color = palette.text,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(subtitle, color = palette.muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SectionLabel(text: String, palette: AppPalette, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        color = palette.muted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.05.sp
    )
}

@Composable
fun OptionSelector(
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit,
    palette: AppPalette,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.surfaceAlt.copy(alpha = if (palette.isDark) 0.62f else 0.74f), RoundedCornerShape(19.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { (id, label) ->
            val isSelected = selected == id
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clickable { onSelect(id) },
                shape = RoundedCornerShape(15.dp),
                color = if (isSelected) palette.surface else Color.Transparent,
                shadowElevation = if (isSelected) 1.dp else 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        label,
                        color = if (isSelected) palette.text else palette.muted,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun NumericField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    palette: AppPalette,
    modifier: Modifier = Modifier,
    allowNegative: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = { text ->
            onValueChange(
                text.filter { character ->
                    character.isDigit() || character == '.' || character == ',' || (allowNegative && character == '-')
                }
            )
        },
        modifier = modifier,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = palette.accent,
            unfocusedBorderColor = palette.outline,
            focusedLabelColor = palette.accent,
            cursorColor = palette.accent
        )
    )
}

@Composable
fun SmallTag(text: String, palette: AppPalette, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = palette.accentTint
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
            color = palette.accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CardHeading(title: String, note: String, palette: AppPalette) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, color = palette.text, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium)
        if (note.isNotBlank()) Text(note, color = palette.muted, style = MaterialTheme.typography.bodySmall)
    }
}
