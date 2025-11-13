# compareTo() method

#### compareTo(...) compares two BigDecimal values

Short answer: compareTo(...) compares two BigDecimal values numerically and returns an int (<0, 0, >0)

* **Return Value:** It returns an integer value indicating the relative order of the current object (`this`) compared to the argument `o`:
  * **Negative integer:** If `this` object is less than `o`.
  * **Zero:** If `this` object is equal to `o`.
  * **Positive integer:** If `this` object is greater than `o`.
* Important differences vs equals(...)
  * equals(...) checks value and scale. Example:
    * new BigDecimal("0.0").<mark style="color:$warning;">equals</mark>(new BigDecimal("0.00")) → false
    * new BigDecimal("0.0").<mark style="color:$info;">compareTo</mark>(new BigDecimal("0.00")) → 0

\
