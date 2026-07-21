package io.github.jean.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.jean.core.common.time.now
import io.github.jean.core.designsystem.R
import io.github.jean.core.designsystem.modifier.border
import io.github.jean.core.designsystem.modifier.clip
import io.github.jean.core.designsystem.modifier.pressScale
import io.github.jean.core.designsystem.theme.LeafTheme
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus

private const val START_YEAR = 1900
private const val END_YEAR = 2100
private const val TOTAL_MONTH_PAGES = (END_YEAR - START_YEAR + 1) * 12

private val WEEKDAYS = listOf("일", "월", "화", "수", "목", "금", "토")

private fun pageOf(
    year: Int,
    month: Int,
): Int = (year - START_YEAR) * 12 + (month - 1)

@Composable
fun LeafDatePickerDialog(
    initialDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    maxDate: LocalDate? = null,
) {
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(initialDate) }
    var yearPickerVisible by remember { mutableStateOf(false) }

    val pagerState =
        rememberPagerState(
            initialPage = pageOf(initialDate.year, initialDate.monthNumber),
            pageCount = { TOTAL_MONTH_PAGES },
        )
    val visibleYear = START_YEAR + pagerState.currentPage / 12
    val visibleMonth = pagerState.currentPage % 12 + 1
    val scope = rememberCoroutineScope()
    val colors = LeafTheme.colors

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(20.dp)
                    .background(colors.card)
                    .padding(top = 24.dp, bottom = 12.dp)
                    .animateContentSize(),
        ) {
            // 선택된 날짜 헤더 — 세리프
            Text(
                text = "${selectedDate.year}년 ${"%02d".format(selectedDate.month.number)}월 ${"%02d".format(selectedDate.day)}일",
                style = LeafTheme.typography.bookTitle,
                color = colors.textPrimary,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = colors.border,
                modifier = Modifier.padding(top = 16.dp),
            )

            // 컨트롤 행: 년월 (연도 그리드 토글) + 월 이동 화살표
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 4.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MonthLabelButton(
                    text = "${visibleYear}년 ${visibleMonth}월",
                    expanded = yearPickerVisible,
                    onClick = { yearPickerVisible = !yearPickerVisible },
                )
                Spacer(modifier = Modifier.weight(1f))
                if (!yearPickerVisible) {
                    LeafIconButton(
                        icon = LeafTheme.res.chevronLeft,
                        iconSize = 16.dp,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                    )
                    LeafIconButton(
                        icon = LeafTheme.res.chevronRight,
                        iconSize = 16.dp,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                    )
                }
            }

            if (yearPickerVisible) {
                YearGrid(
                    visibleYear = visibleYear,
                    todayYear = today.year,
                    onYearSelected = { year ->
                        scope.launch { pagerState.scrollToPage(pageOf(year, visibleMonth)) }
                        yearPickerVisible = false
                    },
                )
            } else {
                WeekdayHeader()
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                ) { page ->
                    MonthGrid(
                        year = START_YEAR + page / 12,
                        month = page % 12 + 1,
                        selectedDate = selectedDate,
                        today = today,
                        maxDate = maxDate,
                        onSelect = { selectedDate = it },
                    )
                }
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, top = 4.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                LeafTextButton(
                    text = stringResource(R.string.common_cancel),
                    onClick = onDismiss,
                    color = colors.textSecondary,
                )
                LeafTextButton(
                    text = stringResource(R.string.common_confirm),
                    onClick = { onConfirm(selectedDate) },
                )
            }
        }
    }
}

@Composable
private fun MonthLabelButton(
    text: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "yearToggleChevron",
    )

    Row(
        modifier =
            modifier
                .pressScale(interactionSource = interactionSource, pressedScale = 0.95f)
                .clip(10.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = LeafTheme.typography.label,
            color = LeafTheme.colors.textPrimary,
        )
        Icon(
            painter = painterResource(LeafTheme.res.chevronDown),
            contentDescription = null,
            tint = LeafTheme.colors.textSecondary,
            modifier =
                Modifier
                    .size(11.dp)
                    .rotate(chevronRotation),
        )
    }
}

@Composable
private fun WeekdayHeader(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        WEEKDAYS.forEach { day ->
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = day,
                    style = LeafTheme.typography.meta,
                    color = LeafTheme.colors.textMuted,
                )
            }
        }
    }
}

@Composable
private fun MonthGrid(
    year: Int,
    month: Int,
    selectedDate: LocalDate,
    today: LocalDate,
    maxDate: LocalDate?,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val firstDay = LocalDate(year, month, 1)
    val leadingBlanks = firstDay.dayOfWeek.isoDayNumber % 7
    val daysInMonth = firstDay.plus(1, DateTimeUnit.MONTH).toEpochDays() - firstDay.toEpochDays()

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
    ) {
        repeat(6) { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { weekday ->
                    val dayNumber = week * 7 + weekday - leadingBlanks + 1
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(42.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (dayNumber in 1..daysInMonth) {
                            val date = LocalDate(year, month, dayNumber)
                            DayCell(
                                date = date,
                                selected = date == selectedDate,
                                isToday = date == today,
                                enabled = maxDate == null || date <= maxDate,
                                onSelect = onSelect,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    selected: Boolean,
    isToday: Boolean,
    enabled: Boolean,
    onSelect: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LeafTheme.colors
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier =
            modifier
                .size(38.dp)
                .pressScale(interactionSource = interactionSource, enabled = enabled, pressedScale = 0.88f)
                .clip(19.dp)
                .then(
                    when {
                        selected -> Modifier.background(colors.primary)
                        isToday -> Modifier.border(width = 1.dp, color = colors.primaryMuted, radius = 19.dp)
                        else -> Modifier
                    },
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = { onSelect(date) },
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = LeafTheme.typography.body,
            color =
                when {
                    selected -> colors.onPrimary
                    !enabled -> colors.textMuted.copy(alpha = 0.45f)
                    else -> colors.textPrimary
                },
        )
    }
}

@Composable
private fun YearGrid(
    visibleYear: Int,
    todayYear: Int,
    onYearSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val years = remember { (START_YEAR..END_YEAR).toList() }
    val gridState =
        rememberLazyGridState(
            // 현재 연도가 두 번째 행쯤 오도록
            initialFirstVisibleItemIndex = ((visibleYear - START_YEAR) / 3 * 3 - 3).coerceAtLeast(0),
        )
    val colors = LeafTheme.colors

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        modifier =
            modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(items = years, key = { it }) { year ->
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier =
                    Modifier
                        .height(36.dp)
                        .pressScale(interactionSource = interactionSource, pressedScale = 0.94f)
                        .clip(18.dp)
                        .then(
                            when {
                                year == visibleYear -> Modifier.background(colors.primary)
                                year == todayYear -> Modifier.border(width = 1.dp, color = colors.primaryMuted, radius = 18.dp)
                                else -> Modifier
                            },
                        ).clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = { onYearSelected(year) },
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = year.toString(),
                    style = LeafTheme.typography.body,
                    color = if (year == visibleYear) colors.onPrimary else colors.textPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LeafDatePickerDialogPreview() {
    LeafTheme {
        LeafDatePickerDialog(
            initialDate = LocalDate(2026, 7, 9),
            maxDate = LocalDate(2026, 7, 13),
            onConfirm = {},
            onDismiss = {},
        )
    }
}
