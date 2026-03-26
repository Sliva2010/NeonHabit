package com.neonhabit.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonhabit.app.ui.theme.*

/**
 * Анимированный неоновый прогресс-бар
 */
@Composable
fun NeonProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    colors: List<Color> = listOf(NeonPink, NeonPurple, NeonCyan),
    backgroundColor: Color = NeonLightGray,
    showGlow: Boolean = true,
    rounded: Boolean = true
) {
    // Анимация прогресса
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )
    
    var isHovered by remember { mutableStateOf(false) }
    
    val glowAlpha by animateFloatAsState(
        targetValue = if (isHovered) 0.8f else 0.4f,
        animationSpec = tween(200)
    )
    
    Column(modifier = modifier) {
        // Слой свечения
        if (showGlow) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(RoundedCornerShape(height))
                    .background(
                        Brush.horizontalGradient(
                            colors = colors.map { it.copy(alpha = glowAlpha) }
                        )
                    )
            )
        }
        
        // Основной прогресс-бар
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(height))
                    .background(
                        Brush.horizontalGradient(colors = colors)
                    )
            )
        }
    }
}

/**
 * Круговой неоновый прогресс-бар (для таймера и уровней)
 */
@Composable
fun NeonCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 8.dp,
    colors: List<Color> = listOf(NeonPink, NeonPurple),
    backgroundColor: Color = NeonLightGray,
    showGlow: Boolean = true,
    cap: StrokeCap = StrokeCap.Round,
    content: @Composable () -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val sweepAngle by remember(animatedProgress) {
        derivedStateOf { animatedProgress * 360f }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = size.toPx()
            val stroke = strokeWidth.toPx()
            val radius = (canvasSize - stroke) / 2
            val center = Offset(canvasSize / 2, canvasSize / 2)
            
            // Фоновый круг
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(backgroundColor, backgroundColor.copy(alpha = 0.1f))
                ),
                radius = radius,
                center = center,
                style = Stroke(width = stroke)
            )
            
            // Прогресс круг
            if (showGlow) {
                // Слой свечения
                drawArc(
                    brush = Brush.sweepGradient(colors.map { it.copy(alpha = 0.3f) }),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    size = Size(canvasSize, canvasSize),
                    topLeft = Offset.Zero,
                    style = Stroke(width = stroke + 4.dp.toPx(), cap = cap)
                )
            }
            
            // Основной прогресс
            drawArc(
                brush = Brush.sweepGradient(colors),
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                size = Size(canvasSize, canvasSize),
                topLeft = Offset.Zero,
                style = Stroke(width = stroke, cap = cap)
            )
        }
        
        // Контент в центре
        content()
    }
}

/**
 * Прогресс-бар уровня пользователя
 */
@Composable
fun LevelProgressBar(
    currentLevel: Int,
    currentExp: Int,
    requiredExp: Int,
    modifier: Modifier = Modifier
) {
    val progress = currentExp.toFloat() / requiredExp.toFloat()
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Уровень $currentLevel",
                color = NeonWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "$currentExp / $requiredExp EXP",
                color = NeonWhite50,
                fontSize = 12.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        NeonProgressBar(
            progress = progress,
            colors = listOf(NeonCyan, NeonBlue, NeonPurple),
            height = 10.dp
        )
    }
}

/**
 * Индикатор жизней
 */
@Composable
fun LivesIndicator(
    lives: Int,
    maxLives: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(maxLives) { index ->
            val isFilled = index < lives
            Text(
                text = "♥",
                color = if (isFilled) NeonRed else NeonLightGray,
                fontSize = 20.sp,
                modifier = Modifier
                    .background(
                        color = if (isFilled) NeonRedGlow else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(2.dp)
            )
        }
    }
}
