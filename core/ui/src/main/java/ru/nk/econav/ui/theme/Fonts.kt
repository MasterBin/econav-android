package ru.nk.econav.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.nk.econav.android.core.resources.R

private val light = Font(R.font.proximanova_light, FontWeight.Light)
private val regular = Font(R.font.proximanova_regular, FontWeight.Normal)
private val semibold = Font(R.font.proximanova_semibold, FontWeight.SemiBold)
private val bold = Font(R.font.proximanova_bold, FontWeight.Bold)
private val extraBld = Font(R.font.proximanova_extrabld, FontWeight.ExtraBold)
private val thin = Font(R.font.proximanova_thin, FontWeight.Thin)
private val black = Font(R.font.proximanova_black, FontWeight.Black)

private val lightIt = Font(R.font.proximanova_lightit, FontWeight.Light, FontStyle.Italic)
private val regulatIt = Font(R.font.proximanova_regularlt, FontWeight.Normal, FontStyle.Italic)
private val semiboldIt = Font(R.font.proximanova_semiboldit, FontWeight.SemiBold, FontStyle.Italic)
private val boldltIt = Font(R.font.proximanova_boldit, FontWeight.Bold, FontStyle.Italic)
private val extraBldIt = Font(R.font.proximanova_extrabldlt, FontWeight.ExtraBold, FontStyle.Italic)
private val thinIt = Font(R.font.proximanova_thinit, FontWeight.Thin, FontStyle.Italic)
private val blackIt = Font(R.font.proximanova_blackit, FontWeight.Black, FontStyle.Italic)


internal val fontFamily = FontFamily(
    light, regular, semibold, bold, extraBld, thin, black,
    lightIt, regulatIt, semiboldIt, boldltIt, extraBldIt, thinIt, blackIt
)

internal val typography = Typography(
    defaultFontFamily = fontFamily
)




