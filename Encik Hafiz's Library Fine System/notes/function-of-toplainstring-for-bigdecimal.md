# Function of toPlainString() for BigDecimal

1. <mark style="color:$info;">toPlainString()</mark> gives a stable, exact decimal representation of a BigDecimal without any exponential (scientific) notation. Combined with setScale(2, RoundingMode.HALF\_UP) it guarantees the printed text is the human‑readable currency form you expect (e.g. "96.00" or "0.80"), not something like "9.6E+1" or "8E-1".
2. Why combine setScale(...) and toPlainString()

* setScale(2, RoundingMode.HALF\_UP) forces exactly two decimal places (adds trailing zeros when required). Example:
  * finalFine = finalFine.setScale(2, RoundingMode.HALF\_UP);
  * finalFine.toPlainString() -> "6.50" (not "6.5").
* Without setScale you might get "6.5" or "6.50000" depending on how the BigDecimal was produced. Combined, you get a consistent currency format.

3. Why not use String.format("%.2f", bigDecimal) or printf directly?

* String.format with %f accepts a primitive double, so you must convert BigDecimal to double first (bigDecimal.doubleValue()) which can <mark style="color:$info;">**lose precision for large or high-precision values**</mark>. Avoid that for monetary values.
* You can use String.format("%s", bigDecimal.setScale(2)) which ends up calling BigDecimal.toString() — better to use toPlainString to avoid exponent notation.





