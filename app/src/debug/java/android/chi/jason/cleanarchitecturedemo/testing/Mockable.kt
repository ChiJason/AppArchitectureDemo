/*
 * make kotlin final class mockable by allopen plugin
 * @see <a href="https://kotlinlang.org/docs/reference/compiler-plugins.html">allopen</a>
*/

package android.chi.jason.cleanarchitecturedemo.testing

/**
 * This annotation allows us to open some classes for mocking purposes while they are final in
 * release builds.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class OpenClass

/**
 * Annotate a class with [Mockable] if you want it to be extendable in debug builds.
 */
@OpenClass
@Target(AnnotationTarget.CLASS)
annotation class Mockable