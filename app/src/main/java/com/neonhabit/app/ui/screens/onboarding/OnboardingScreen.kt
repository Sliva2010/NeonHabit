package com.neonhabit.app.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neonhabit.app.ui.components.NeonButton
import com.neonhabit.app.ui.theme.NeonBlack
import com.neonhabit.app.ui.theme.NeonPink
import com.neonhabit.app.ui.theme.NeonPurple
import com.neonhabit.app.ui.theme.NeonWhite
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    
    val pages = listOf(
        OnboardingPage(
            icon = "🚀",
            title = "Добро пожаловать в NeonHabit",
            description = "Твой персональный трекер привычек с геймификацией"
        ),
        OnboardingPage(
            icon = "📋",
            title = "Создавай привычки",
            description = "Создавай ежедневные задачи и повторяющиеся привычки"
        ),
        OnboardingPage(
            icon = "🏆",
            title = "Получай награды",
            description = "Выполняй задачи, получай опыт и открывай награды"
        )
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(
                        NeonPink.copy(alpha = 0.2f),
                        NeonPurple.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            // Pager с экранами
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(2f)
            ) { page ->
                OnboardingPageContent(pages[page])
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Индикаторы страниц
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (pagerState.currentPage == index) 12.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (pagerState.currentPage == index) NeonPink else NeonPink.copy(alpha = 0.3f)
                            )
                    )
                }
            }
            
            // Кнопки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (pagerState.currentPage < 2) {
                    NeonButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        text = "Далее",
                        modifier = Modifier.weight(1f),
                        colors = listOf(NeonPink, NeonPurple)
                    )
                } else {
                    NeonButton(
                        onClick = onFinished,
                        text = "Начать",
                        modifier = Modifier.weight(1f),
                        colors = listOf(NeonPink, NeonPurple)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = page.icon,
            fontSize = 120.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = page.title,
            color = NeonWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = page.description,
            color = NeonWhite.copy(alpha = 0.7f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

data class OnboardingPage(
    val icon: String,
    val title: String,
    val description: String
)
