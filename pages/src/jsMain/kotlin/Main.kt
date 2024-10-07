import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        // todo: Style()

        Div({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
            }
        }) {
            H1 { Text("Privacy Policy") }
            P {
                Text(
                    "We are not interested in collecting any personal information. We believe " +
                            "such information is yours and yours alone. We do not store or transmit " +
                            "your personal details, nor do we include any advertising or analytics " +
                            "or analytics software that talks to third parties.")
            }
            H2 { Text("What Information Do We Collect?") }
            P {
                Text(
                    "We do not collect, store, use or share any information, personal or otherwise."
                )
            }
            H2 { Text("Contact") }
            P {
                Text("If you have any questions or concerns, please feel free to ")
                A(href = "mailto:nathanielreline@gmail.com") {
                    Text("contact us")
                }
                Text(".")
            }
        }
    }
}
