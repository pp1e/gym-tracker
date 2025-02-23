import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import common

fun MainViewController(root: TodoRoot): UIViewController {
    return ComposeUIViewController {
        GymTracker {
            val model by router.model.subscribeAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold (
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBar(
                        screenTitle = model.screenTitle,
                        isScheduleScreenActive = model.isScheduleScreenActive,
                        isHistoryScreenActive = model.isHistoryScreenActive,
                        isEditTrainingScreenActive = model.isEditTrainingScreenActive,
                        onBackClicked = router::onBackClicked,
                    )
                },
                bottomBar = {
                    BottomMenu(
                        onTrainingClicked = router::onTrainingScreenMenuButtonClicked,
                        onScheduleClicked = router::onScheduleScreenMenuButtonClicked,
                        onHistoryClicked = router::onHistoryScreenMenuButtonClicked,
                        isTrainingScreenActive = model.isCurrentTrainingScreenActive,
                        isScheduleScreenActive = model.isScheduleScreenActive,
                        isHistoryScreenActive = model.isHistoryScreenActive,
                    )
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { paddingValues ->
                RootContent(
                    router = router,
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}