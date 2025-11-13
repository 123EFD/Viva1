# Library Fine System guide

Good — below I’ll break this down into (A) the feature list, (B) a step-by-step implementation workflow, (C) the variables + imports you’ll need, and (D) important segmented Java code snippets (not a full program) that show the key algorithms (base fine calculation, penalties, discounts, I/O loop, formatting). I’ll also include notes about validation, testing and maintainability.

A — Feature list (requirements / features to implement)

* Input handling
  * Read number of test cases
  * For each case read: daysOverdue (int), bookType (char/string: R/G/M/C/T), borrowerCategory (char/string: S/T), previousLateReturns (int)
* Base fine calculator per book type
  * Reference (R): flat RM 100.00 if borrowed and overdue (>0 days)
  * General (G): tiered rates (1–7, 8–30, >30)
  * Magazine (M): flat RM 0.20/day
  * Multimedia (C): RM 2/day for first 10 days, RM 5/day for subsequent days
  * Thesis (T): RM 10/day; extra RM 200 if overdue > 15 days
* Additional fixed penalties
  * +RM 25 if overdue > 60 days
  * +RM 10 if previous late returns >= 3 (habitual)
* Discounts / rewards
  * Staff (borrowerCategory == 'T') gets 20% discount on final fine (checked before Good Borrower)
  * Good Borrower: if previousLateReturns == 0 && daysOverdue <= 3 then 50% reduction
  * Discounts mutually exclusive; Staff takes precedence over Good Borrower
* Output formatting
  * Print per-case header and Total Fine with exactly 2 decimal places (RM x.xx)
* Edge cases & validation
  * days = 0 → no fine
  * invalid book codes or borrower codes → handle gracefully (error or default)
  * negative days or negative previousLateReturns → input validation
* Test cases & examples
  * Include unit tests for each book type, boundaries for tiers (7, 8, 30, 31, 10, 11, 15, 16, 60, 61), discounts, and penalty combos

B — Workflow / implementation plan (step-by-step)

1. Setup project and imports
   * Create a main class (e.g., LibraryFineCalculator) and import Scanner and formatting / BigDecimal utilities.
2. Global constants
   * Define constants for rates, penalties, thresholds (e.g., RATE\_G\_TIER1, RATE\_C\_FIRST\_10, PENALTY\_THESIS\_15\_DAYS, etc.)
   * Consider using enum BookType { R, G, M, C, T } and enum Borrower { STUDENT, STAFF } for clarity.
3. Input reading loop
   * Read testCases
   * For i from 1..testCases:
     * Read tokens: days, bookCode, borrowerCode, prevLateReturns
     * Validate inputs (non-negative days, valid codes)
     * Compute fine by calling helper methods
     * Print formatted output for Case i
4. Fine calculation (separate method)
   * calculateBaseFine(int days, BookType bookType) → BigDecimal
   * applyAdditionalPenalties(BigDecimal fine, int days, int prevLate) → BigDecimal
   * applyDiscounts(BigDecimal fine, Borrower borrower, int days, int prevLate) → BigDecimal
5. Formatting & output
   * Use DecimalFormat or BigDecimal#setScale(2, RoundingMode.HALF\_UP) to print RM x.xx
6. Testing
   * Write unit tests for every book type and boundary values; manually check sample input/output.
7. Error handling & maintainability
   * If invalid input, either skip case with a message or throw a parse error.
   * Keep constants together so business rules are easy to update.

C — Variables and imports (what to declare at top) Suggested imports:

```java
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat; // optional
```

Suggested constants (as static final fields):

```java
// General constants for money using BigDecimal
static final BigDecimal ZERO = BigDecimal.ZERO;
static final BigDecimal BD_POINT_TWO = new BigDecimal("0.20");
static final BigDecimal BD_POINT_FIVE = new BigDecimal("0.50");
static final BigDecimal BD_ONE = new BigDecimal("1.00");
static final BigDecimal BD_TWO = new BigDecimal("2.00");
static final BigDecimal BD_TEN = new BigDecimal("10.00");
static final BigDecimal BD_HUNDRED = new BigDecimal("100.00");
static final BigDecimal BD_TWO_HUNDRED = new BigDecimal("200.00");

static final BigDecimal PENALTY_OVER_60 = new BigDecimal("25.00");
static final BigDecimal PENALTY_HABITUAL = new BigDecimal("10.00");
static final BigDecimal STAFF_DISCOUNT = new BigDecimal("0.20"); // 20%
static final BigDecimal GOODBORROWER_DISCOUNT = new BigDecimal("0.50"); // 50%
```

Per-test-case local variables:

* int daysOverdue
* char bookTypeCode or String bookTypeStr
* char borrowerCategory
* int prevLateReturns
* BigDecimal totalFine

D — Important segmented code (key algorithm pieces — not a full program)

1. Parsing input and main loop (sketch)

```java
Scanner sc = new Scanner(System.in);
int testCases = sc.nextInt();
for (int caseNum = 1; caseNum <= testCases; caseNum++) {
    int days = sc.nextInt();
    String bookCode = sc.next(); // e.g., "G"
    String borrowerCode = sc.next(); // "S" or "T"
    int prevLate = sc.nextInt();

    // map codes to enums or just use chars after uppercasing
    // validate inputs...

    BigDecimal base = calculateBaseFine(days, bookCode.charAt(0));
    BigDecimal afterPenalties = applyPenalties(base, days, prevLate, bookCode.charAt(0));
    BigDecimal finalFine = applyDiscounts(afterPenalties, borrowerCode.charAt(0), days, prevLate);

    // ensure scale and rounding
    finalFine = finalFine.setScale(2, RoundingMode.HALF_UP);

    System.out.println("--- Case " + caseNum + " ---");
    System.out.println("Total Fine: RM " + finalFine.toPlainString());
}
sc.close();
```

2. Base fine calculator (full logic for each book type)

```java
// Returns BigDecimal representing base fine before extra penalties/discounts
static BigDecimal calculateBaseFine(int days, char bookCode) {
    if (days <= 0) return ZERO;

    switch (Character.toUpperCase(bookCode)) {
        case 'R':
            // Reference: in-library use only — if borrowed and overdue, flat RM100
            return BD_HUNDRED;
        case 'G': {
            // General Books: tiered
            BigDecimal fine = ZERO;
            if (days >= 1) {
                int tier1Days = Math.min(days, 7);
                fine = fine.add(BD_POINT_FIVE.multiply(new BigDecimal(tier1Days)));
            }
            if (days >= 8) {
                int tier2Days = Math.min(days, 30) - 7; // days 8..30 inclusive
                fine = fine.add(BD_ONE.multiply(new BigDecimal(tier2Days)));
            }
            if (days > 30) {
                int tier3Days = days - 30;
                fine = fine.add(BD_TWO.multiply(new BigDecimal(tier3Days)));
            }
            return fine;
        }
        case 'M':
            // Magazines: flat 0.20 per day
            return BD_POINT_TWO.multiply(new BigDecimal(days));
        case 'C': {
            // Multimedia CDs/DVDs: RM2 for first 10 days, RM5 afterward
            int firstDays = Math.min(days, 10);
            BigDecimal fine = BD_TWO.multiply(new BigDecimal(firstDays));
            if (days > 10) {
                fine = fine.add(new BigDecimal("5.00").multiply(new BigDecimal(days - 10)));
            }
            return fine;
        }
        case 'T': {
            // Thesis: RM10/day, plus RM200 if overdue > 15 days
            BigDecimal fine = BD_TEN.multiply(new BigDecimal(days));
            if (days > 15) {
                fine = fine.add(BD_TWO_HUNDRED);
            }
            return fine;
        }
        default:
            // unknown code -> treat as zero or throw; choose to return ZERO
            return ZERO;
    }
}
```

3. Apply extra penalties (over 60 days and habitual offender)

```java
static BigDecimal applyPenalties(BigDecimal currentFine, int days, int prevLate, char bookCode) {
    BigDecimal result = currentFine;

    // Additional RM25 if overdue > 60 days
    if (days > 60) {
        result = result.add(PENALTY_OVER_60);
    }

    // Habitual offender penalty if previousLate >= 3
    if (prevLate >= 3) {
        result = result.add(PENALTY_HABITUAL);
    }

    return result;
}
```

4. Apply discounts (mutually exclusive; staff before good borrower)

```java
static BigDecimal applyDiscounts(BigDecimal fineBeforeDiscounts, char borrowerCode, int days, int prevLate) {
    BigDecimal result = fineBeforeDiscounts;

    // Staff ('T') gets 20% discount on final fine
    if (Character.toUpperCase(borrowerCode) == 'T') {
        BigDecimal discount = result.multiply(STAFF_DISCOUNT);
        result = result.subtract(discount);
        return result;
    }

    // Good borrower: previousLate == 0 and days <= 3 => 50% reduction
    if (prevLate == 0 && days <= 3) {
        BigDecimal discount = result.multiply(GOODBORROWER_DISCOUNT);
        result = result.subtract(discount);
    }

    return result;
}
```

5. Formatting the output

* Use BigDecimal#setScale(2, RoundingMode.HALF\_UP) and toPlainString()
* Or use DecimalFormat with locale-safe separators if needed. Example:

```java
BigDecimal displayed = finalFine.setScale(2, RoundingMode.HALF_UP);
System.out.println("Total Fine: RM " + displayed.toPlainString());
```

E — Important rules, order of operations, corner cases and reasoning

* Order matters:
  1. Compute base fine using the book type rules.
  2. Add additional fixed penalties (over 60 days, habitual offender).
  3. Apply discounts — staff discount has precedence over good-borrower per spec.
* Discounts are mutually exclusive — do not stack staff + good-borrower.
* If days == 0 → base = 0 → penalties should not be applied (penalties depend on overdue days, but habitual offender penalty is based on prevLate only — spec says habituall offender receives additional RM10 if prevLate >=3; it doesn't require days>0 explicitly. However semantically it should apply only when there is a fine. Decide policy: apply habitual penalty only if days > 0. I recommend checking days > 0 before applying penalties).
* Reference books: if days > 0 → flat RM 100. If borrowed in-library only but the input indicates it was borrowed and overdue, apply RM100.
* Thesis extra RM200 applies only if days > 15 (it stacks on top of the day-rate).
* Example verification: (From spec) 65 C S 4:
  * Base C: (10 \* 2.00) + (55 \* 5.00) = 295.00
  * > 60 days penalty +25
  * Habitual prevLate>=3 +10
  * Total = 330.00 — matches example.

F — Validation & Testing checklist

* Test edges:
  * G: days = 1,7,8,30,31
  * C: days = 10,11
  * T: days = 15,16
  * Over 60: 60 vs 61
  * Discounts: staff with any prevLate; good borrower (prevLate=0 and days<=3) vs not (days=4)
  * days = 0 should give RM 0.00
* Test invalid codes behavior (either handle with exception or print message and skip)
* Add unit tests for calculateBaseFine, applyPenalties, applyDiscounts, and integration tests for whole workflow.

G — Suggestions for maintainability

* Use enums for book types and borrowers with parsing helper methods (BookType.fromCode(char)).
* Keep business-rule constants in a single class or top of file.
* Encapsulate calculation into a FineCalculator class with methods to make it testable.
* Use BigDecimal for money to avoid floating point rounding errors.

If you want, I can:

* Provide a UML-like method diagram or class skeleton (without full code),
* Provide short unit-test examples (JUnit) for key methods,
* Or convert the snippets into a single compiled program (but you said you don't want full sample code — I’ll wait for your go-ahead).

Which of these would help you next?
