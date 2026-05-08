package pt.isel.reversi.app.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.app.state.PagesState
import pt.isel.reversi.app.pages.Page

/**
 * Central component managing transitions between pages in the application.
 * Slides right (forward) when navigating to higher-level pages, left (backward) when returning.
 *
 * @param theme The current app theme for styling.
 * @param contentAlignment How to align overlapping content during transition.
 * @param switchAction Lambda defining content for each page within the animation container.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
@Suppress("unused")
fun AppScreenSwitcher(
    pagesState: PagesState,
    theme: AppTheme,
    contentAlignment: Alignment = Alignment.TopStart,
    switchAction: @Composable (BoxScope.(page: Page) -> Unit)
) {
    val duration = 500
    val targetPage = pagesState.page

    AnimatedContent(
        targetState = targetPage,
        transitionSpec = {
            val forward = targetPage.level > initialState.level
            val iOSEasing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
            if (forward) reversiGoInAnimation(duration, iOSEasing)
            else reversiGoOutAnimation(duration, iOSEasing)
        },
        modifier = Modifier.fillMaxSize().background(theme.backgroundColor),
        label = "PageTransition",
        contentAlignment = contentAlignment,
        contentKey = { it }
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (page == targetPage || page == Page.MAIN_MENU) {
                switchAction(page)
            }
        }
    }
}

/**
 * Creates an animation for forward page transitions (sliding right with fade in).
 *
 * @param duration Animation duration in milliseconds.
 * @param animation Easing function to apply to the animation.
 * @return A ContentTransform combining slide and fade animations.
 */
fun reversiGoInAnimation(
    duration: Int = 500,
    animation: Easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f),
): ContentTransform =
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(duration, easing = animation)
    ) + fadeIn(tween(duration, easing = animation)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(duration, easing = animation)
            ) + fadeOut(tween(duration, easing = animation))

/**
 * Creates an animation for backward page transitions (sliding left with fade in).
 *
 * @param duration Animation duration in milliseconds.
 * @param animation Easing function to apply to the animation.
 * @return A ContentTransform combining slide and fade animations.
 */
fun reversiGoOutAnimation(
    duration: Int = 500,
    animation: Easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
): ContentTransform =
    slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(duration, easing = animation)
    ) + fadeIn(tween(duration, easing = animation)) togetherWith
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(duration, easing = animation)
            ) + fadeOut(tween(duration, easing = animation))

/**
 * Creates a fade-only animation without slide transition.
 *
 * @param duration Animation duration in milliseconds.
 * @param animation Easing function to apply to the animation.
 * @return A ContentTransform combining only fade in/out animations.
 */
fun reversiFadeAnimation(
    duration: Int = 500,
    animation: Easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
): ContentTransform =
    fadeIn(tween(duration, easing = animation)) togetherWith
            fadeOut(tween(duration, easing = animation))
