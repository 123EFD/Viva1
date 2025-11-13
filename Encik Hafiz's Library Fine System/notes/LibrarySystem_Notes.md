# Library Fine System — Code Walkthrough (Presentation)

This document explains the Java implementation of the Library Fine System (LibrarySystem.java) for a short presentation. It covers the program purpose, high-level flow, key classes/enums, important methods, examples, edge cases, and testing notes.

---

## 1. Purpose (one-sentence)
Compute library fines using multi-condition rules (book type, days overdue, borrower type, previous late returns), apply penalties and mutually-exclusive discounts, and print a formatted result for each test case.

---

## 2. Quick usage (compile & run)
- Save the source as `LibrarySystem.java`.
- Compile:
```bash
javac LibrarySystem.java
```
- Run with redirected input (recommended to avoid typed input echo):
```bash
java LibrarySystem < input.txt
```
- `input.txt` example:
```
5
10 G S 1
5 M S 0
65 C S 4
12 T T 2
2 G T 0
```

---

## 3. Expected output format
For each case:
```
--- Case N ---
Total Fine: RM x.xx
```
Example (for the sample input above):
```
--- Case 1 ---
Total Fine: RM 6.50
--- Case 2 ---
Total Fine: RM 1.00
--- Case 3 ---
Total Fine: RM 330.00
--- Case 4 ---
Total Fine: RM 96.00
--- Case 5 ---
Total Fine: RM 0.80
```

---

## 4. High-level program structure
- `public class LibrarySystem` — main entry and orchestration
- `public static class LibraryFineCalculator` — constants and calculation logic
  - monetary constants (BigDecimal)
  - `calculateBaseFine(BookType, int days)` — per-type base fine
  - `applyPenalties(BigDecimal, int days, int prevLate)` — >60-day & habitual penalties
  - `applyDiscounts(BigDecimal, Borrower, int days, int prevLate)` — staff or good-borrower discount (mutually exclusive)
- `public enum BookType` — R/G/M/C/T with `fromCode(char)` lookup
- `public enum Borrower` — S/T with `fromCode(char)` lookup

All enums and helper methods are declared at class scope so they are available to `main`.

---

## 5. Per-book fine rules (summary)
- Reference (R): flat RM100 if overdue (>0).
- General (G): tiered
  - Days 1–7: RM0.50/day
  - Days 8–30: RM1.00/day
  - Over 30: RM2.00/day
- Magazine (M): RM0.20/day
- Multimedia (C): RM2/day for first 10 days, RM5/day after
- Thesis (T): RM10/day; if overdue > 15 days add RM200

Additional rules:
- +RM25 if overdue > 60 days
- +RM10 if previousLate >= 3
- Staff (T) gets 20% discount (checked before good-borrower)
- Good Borrower: prevLate == 0 and days <= 3 => 50% discount (applies only if not staff)

---

## 6. Input parsing & validation
- `main` reads: testCases, then for each case: days (int), bookCode (token), borrowerCode (token), prevLate (int)
- Validation:
  - tokens are non-empty
  - numeric values >= 0
  - codes validated via `BookType.fromCode` and `Borrower.fromCode` (returns `Optional` — used to print error & skip invalid)
- Program avoids printing any prompt lines so automated grading sees only expected output.

---

## 7. Important implementation details
- Use `BigDecimal` (constructed from string) for all monetary computations to avoid floating-point rounding.
- Compute base fine first, then add penalties, then apply discount (discounts mutually exclusive; staff first).
- Format final output with `setScale(2, RoundingMode.HALF_UP)` and `toPlainString()` to display RM x.xx.

---

## 8. Edge cases & design choices
- days == 0 → no base fine and therefore no penalties or discounts.
- Habitual penalty only applies when there is an overdue (days > 0).
- Invalid codes cause the case to be skipped with a short error message (suitable for development); for strict grading you can change behavior to throw or halt.
- The enums use static maps for O(1) lookup; they return `Optional` to encourage explicit validation.

---

## 9. Testing checklist (recommended)
- Boundary days: 0, 1, 3, 7, 8, 10, 11, 15, 16, 30, 31, 60, 61
- Each book type individually (R,G,M,C,T)
- Discounts:
  - Staff with various fines (ensure 20% off)
  - Good borrower (prevLate=0, days<=3) ensure 50% off
  - Confirm staff takes precedence over good-borrower
- Habitual: prevLate = 2 (no penalty) vs 3 (penalty)
- Compound cases: thesis >15 and >60 days, combined penalties

---

## 10. Slide-ready talking points
- One-liner: "Type-safe enums + central calculator keep business logic easy to follow and test."
- Why BigDecimal: "monetary accuracy"
- Flow: "parse → validate → base fine → penalties → discounts → format"
- Extensibility: "add new book types by adding enum constant and updating calculator (or add method to enum to encapsulate behavior)"

---





