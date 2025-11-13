# add(), multiply(): BigDecimal(Immutable)

* multiply(...) multiplies two BigDecimals and returns a new BigDecimal; add(...) adds two BigDecimals and returns a new BigDecimal.
*

    * GENERAL\_BOOKS\_TIER1.multiply(new BigDecimal(tier1Days))
      * <mark style="color:$info;">multiply is executed first</mark>. It multiplies the BigDecimal rate (e.g., 0.50) by the BigDecimal representation of the integer days (e.g., 7). The result is a new BigDecimal (e.g., 3.50).
      * fine.add(...)
        * <mark style="color:$info;">**add takes the current fine BigDecimal and the multiplication result**</mark> and returns a new BigDecimal equal to their sum. You must assign this new value back if you want to keep it (fine = fine.add(...)).



## What is meant by <mark style="color:$warning;">**Immutabilty**</mark>

***

They return new BigDecimal instances. Example:

* BigDecimal original = new BigDecimal("1.00");
* original.add(new BigDecimal("0.50")); // original still "1.00"
* original = original.add(new BigDecimal("0.50")); // now original is "1.50"

```java
BigDecimal rate = new BigDecimal("0.50"); // scale 2 (floating point)
BigDecimal days = BigDecimal.valueOf(7); // scale 0
BigDecimal part = rate.multiply(days); // 0.50 * 7 = 3.50 (new BigDecimal)
BigDecimal fine = BigDecimal.ZERO;
fine = fine.add(part); // 0.00 + 3.50 = 3.50 (new BigDecimal)
System.out.println(part.toPlainString()); // prints "3.50"
System.out.println(fine.toPlainString()); // prints "3.50"

```

