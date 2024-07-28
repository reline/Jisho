import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import jisho.composeapp.generated.resources.Res
import jisho.composeapp.generated.resources.banner

@Composable
fun MainContent(
    results: List<Result>,
    query: String?,
    showProgressBar: Boolean,
    showLogo: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            showLogo -> Logo()
            showProgressBar -> ProgressBar()
            results.isEmpty() -> EmptyResultsText(query)
            else -> DictionaryEntries(results)
        }
    }
}

@Composable
fun Logo() {
    Image(painter = painterResource(Res.drawable.banner), contentDescription = null)
}

@Composable
fun ProgressBar() {
    CircularProgressIndicator(
        color = Color(0xC70025),
        modifier = Modifier.size(76.dp),
        strokeWidth = 6.dp,
    )
}

@Composable
fun EmptyResultsText(query: String?) {
    Text(text = "No match for $query")
}
