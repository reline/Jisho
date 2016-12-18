package xyz.projectplay.jisho.ui.controllers.base;

import android.support.annotation.LayoutRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class that designates a controller and specifies its layout.
 *
 * <p>For example, <pre><code>
 * {@literal@}Layout(R.layout.my_controller_layout)
 * public class MyController { ... }
 * </code></pre>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Layout {
    @LayoutRes int value();
}