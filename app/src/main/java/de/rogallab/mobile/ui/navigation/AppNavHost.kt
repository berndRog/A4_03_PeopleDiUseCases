package de.rogallab.mobile.ui.navigation
import NavScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.rogallab.mobile.ui.people.PeopleSwipeListScreen
import de.rogallab.mobile.ui.people.PeopleViewModel
import de.rogallab.mobile.ui.people.PersonDetailScreen
import de.rogallab.mobile.ui.people.PersonInputScreen
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.UUID

@Composable
fun AppNavHost(
   peopleViewModel: PeopleViewModel = hiltViewModel()
) {
// val tag ="ok>AppNavHost         ."

   val navHostController: NavHostController = rememberNavController()
   val duration = 800  // in ms

   NavHost(
      navController = navHostController,
      startDestination = NavScreen.PeopleList.route,
      enterTransition = { enterTransition(duration) },
      exitTransition  = { exitTransition(duration)  },
      popEnterTransition = { popEnterTransition(duration)},
      popExitTransition = { popExitTransition(duration) }
   ) {

      composable(NavScreen.PeopleList.route) {
         PeopleSwipeListScreen(
            navController = navHostController,
            viewModel = peopleViewModel
         )
      }

      composable(NavScreen.PersonInput.route) {
         PersonInputScreen(
            navController = navHostController,
            viewModel = peopleViewModel
         )
      }

      composable(
         route = NavScreen.PersonDetail.route + "/{personId}",
         arguments = listOf(navArgument("personId") { type = NavType.StringType})
      ) { backStackEntry ->
         val id = backStackEntry.arguments?.getString("personId")?.let{
            UUID.fromString(it)
         }
         PersonDetailScreen(
            id = id,
            navController = navHostController,
            viewModel = peopleViewModel
         )
      }
   }
}


private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
   duration: Int
) = fadeIn(animationSpec = tween(duration)) + slideIntoContainer(
   animationSpec = tween(duration),
   towards = AnimatedContentTransitionScope.SlideDirection.Left
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
   duration: Int
) = fadeOut(animationSpec = tween(duration)) + slideOutOfContainer(
   animationSpec = tween(duration),
   towards = AnimatedContentTransitionScope.SlideDirection.Left
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(
   duration: Int
) = fadeIn(animationSpec = tween(duration)) + slideIntoContainer(
   animationSpec = tween(duration),
   towards = AnimatedContentTransitionScope.SlideDirection.Up
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(
   duration: Int
) = fadeOut(animationSpec = tween(duration))