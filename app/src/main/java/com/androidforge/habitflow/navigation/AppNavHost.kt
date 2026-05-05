package com.androidforge.habitflow.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidforge.habitflow.core.AdManager
import com.androidforge.habitflow.presentation.about.AboutScreen
import com.androidforge.habitflow.presentation.add_edit_habit.AddEditHabitScreen
import com.androidforge.habitflow.presentation.habit_detail.HabitDetailScreen
import com.androidforge.habitflow.presentation.habit_list.HabitListScreen
import com.androidforge.habitflow.presentation.settings.SettingsScreen
import com.androidforge.habitflow.presentation.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    adManager: AdManager,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestinations.Splash.route,
        modifier = modifier,
        enterTransition = { // Page transition: Subtle fade combined with a gentle horizontal slide from right
            fadeIn(animationSpec = tween(300)) +
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
        },
        exitTransition = { // Page transition: Subtle fade combined with a gentle horizontal slide to left
            fadeOut(animationSpec = tween(300)) +
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
        },
        popEnterTransition = { // Page transition: Subtle fade combined with a gentle horizontal slide from left
            fadeIn(animationSpec = tween(300)) +
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
        },
        popExitTransition = { // Page transition: Subtle fade combined with a gentle horizontal slide to right
            fadeOut(animationSpec = tween(300)) +
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
        }
    ) {
        composable(AppDestinations.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(AppDestinations.HabitList.route) {
            HabitListScreen(navController = navController, adManager = adManager)
        }
        composable(
            route = AppDestinations.AddEditHabit.route,
            arguments = listOf(
                navArgument("habitId") { type = NavType.StringType; nullable = true; defaultValue = null }
            )
        ) {
            AddEditHabitScreen(navController = navController)
        }
        composable(
            route = AppDestinations.HabitDetail.route,
            arguments = listOf(
                navArgument("habitId") { type = NavType.StringType; nullable = false }
            )
        ) {
            val habitId = it.arguments?.getString("habitId")
            requireNotNull(habitId) { "Habit ID required for HabitDetailScreen" }
            HabitDetailScreen(navController = navController, habitId = habitId)
        }
        composable(AppDestinations.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(AppDestinations.About.route) {
            AboutScreen(navController = navController)
        }
    }
}