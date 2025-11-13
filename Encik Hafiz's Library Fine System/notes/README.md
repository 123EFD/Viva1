# Why static final cannot put in public static void main(String\[] args) \*\*this is a method

### Summary:

You declared variables with the modifier static (e.g., static final BigDecimal ...) in a context where static is not allowed — most commonly inside a method or (less commonly) inside a method parameter list. Java permits static only for class-level fields, not for local variables or parameters.

#### Reference: [https://share.google/aimode/3broCDPVM5TL5OAhh](https://share.google/aimode/3broCDPVM5TL5OAhh)

The compiler error you saw — "Illegal modifier for parameter general\_books\_tier2; only final is permitted" — means you used a modifier that Java doesn't allow in that context. The usual causes are:

* You put `static` (or `private`, `public`) on a local variable inside a method. Local variables cannot be `static` (only class-level fields can). You may use `final` for local variables, but not `static`.
* Or, less commonly, you accidentally inserted the declaration where the compiler thinks it's a method parameter (for example, inside parentheses) — method parameters can only be `final` (not `static`/`private`/`public`).

Why the first snippet “worked” and the second didn’t

* The first snippet is valid if it was declared at class scope (as class fields). `static final` is legal for fields defined directly in a class.
* The second snippet produced the error because those `static final` declarations were placed where `static` isn't allowed — most likely inside a method (e.g., inside main) or inadvertently inside a parameter list.

How to fix it You have two options depending on where you want the constants to live.

1. Put them at class level (recommended for constants used across methods)

* Use `static final` at the top of your class (outside any method).

Example (class-level constants):

```java
import java.math.BigDecimal;

public class LibraryFineCalculator {
    // class-level constants: OK to use static final
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal MAGAZINES_FINE = new BigDecimal("0.20");
    private static final BigDecimal GENERAL_BOOKS_TIER1 = new BigDecimal("0.50");
    private static final BigDecimal GENERAL_BOOKS_TIER2 = new BigDecimal("1.00");
    private static final BigDecimal GENERAL_BOOKS_TIER3 = new BigDecimal("2.00");
    private static final BigDecimal THESIS_FINE = new BigDecimal("10.00");
    private static final BigDecimal REF_BOOKS_FINE = new BigDecimal("100.00");
    private static final BigDecimal THESIS_OVERDUE = new BigDecimal("200.00");

    private static final BigDecimal PENALTY_OVER_60 = new BigDecimal("25.00");
    private static final BigDecimal PENALTY_HABITUAL = new BigDecimal("10.00");
    private static final BigDecimal STAFF_DISCOUNT = new BigDecimal("0.20");
    private static final BigDecimal GOODBORROWER_DISCOUNT = new BigDecimal("0.50");

    public static void main(String[] args) {
        // ...
    }
}
```

2. If you must declare them inside a method (not recommended for widely used constants), drop `static` and make them `final` or plain local variables:

* Local variables cannot be `static`. Use `final` if you want them immutable.

Example (method-local constants):

```java
public static void main(String[] args) {
    final BigDecimal ZERO = BigDecimal.ZERO;
    final BigDecimal magazines_fine = new BigDecimal("0.20");
    final BigDecimal general_books_tier1 = new BigDecimal("0.50");
    final BigDecimal general_books_tier2 = new BigDecimal("1.00");
    // ...
}
```

Extra recommendations

* Naming: by convention, constants use ALL\_CAPS\_WITH\_UNDERSCORES (e.g., GENERAL\_BOOKS\_TIER1) — this makes intent clearer.
* BigDecimal: instantiate with string literals (`new BigDecimal("0.20")`) — avoids floating-point precision issues like new BigDecimal(0.2).
* Scope: prefer class-level `private static final` for constants that are reused across methods. Use `final` local vars only for one-off values inside a single method.
* If you still see an error, check you didn't accidentally paste these declarations inside a method parameter list (inside parentheses) or inside another method signature.

