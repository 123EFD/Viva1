# Enums for BookCodes and BookType + Basic Concept

Short answer first: use enums to represent book types and borrower categories. Give each enum a code (char or String), provide a static lookup (Map) and two helper methods: one that returns Optional (safe, for validation) and one that throws IllegalArgumentException (convenient when input is already validated). Convert user input to uppercase/trim before lookup. Then use the enum values in switches or method calls instead of raw chars/strings.

Why enums?

* Type-safe named constants (no magic chars scattered around).
* Can attach data (code, display name), behavior (methods) and lookup logic in one place.
* Cleaner switch statements and less runtime errors from invalid codes.
* Easy to test and extend.

Example BookType enum (recommended pattern)

```java
public enum BookType {
    REFERENCE('R'),
    GENERAL('G'),
    MAGAZINE('M'),
    MULTIMEDIA('C'),
    THESIS('T');

    private final char code;
    private static final Map<Character, BookType> BY_CODE = new HashMap<>();

    static {
        for (BookType bt : values()) {
            BY_CODE.put(bt.code, bt);
        }
    }

    BookType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    // Safe lookup: returns Optional (no exception)
    public static Optional<BookType> fromCode(char c) {
        return Optional.ofNullable(BY_CODE.get(Character.toUpperCase(c)));
    }

    // Strict parse: throws IllegalArgumentException for invalid code
    public static BookType parse(char c) {
        BookType bt = BY_CODE.get(Character.toUpperCase(c));
        if (bt == null) {
            throw new IllegalArgumentException("Invalid book code: " + c);
        }
        return bt;
    }
}
```

Example Borrower enum

```java
public enum Borrower {
    STUDENT('S'),
    STAFF('T');

    private final char code;
    private static final Map<Character, Borrower> BY_CODE = new HashMap<>();

    static {
        for (Borrower b : values()) BY_CODE.put(b.code, b);
    }

    Borrower(char code) { this.code = code; }

    public static Optional<Borrower> fromCode(char c) {
        return Optional.ofNullable(BY_CODE.get(Character.toUpperCase(c)));
    }

    public static Borrower parse(char c) {
        Borrower b = BY_CODE.get(Character.toUpperCase(c));
        if (b == null) throw new IllegalArgumentException("Invalid borrower code: " + c);
        return b;
    }
}
```

How to use these enums when reading input

* Read tokens as String, trim and check length, then get first char and uppercase it.
* Use fromCode(...) to validate without exceptions, or parse(...) inside try/catch.

Example usage (validation with Optional):

```java
String bookToken = sc.next();              // user input like "g" or "G"
char bookChar = bookToken.trim().toUpperCase().charAt(0);

Optional<BookType> maybeBook = BookType.fromCode(bookChar);
if (maybeBook.isEmpty()) {
    System.out.println("Invalid book code: " + bookToken);
    // handle error: skip/ask again/exit
    continue;
}
BookType bookType = maybeBook.get();

String borrowerToken = sc.next();
char borrowerChar = borrowerToken.trim().toUpperCase().charAt(0);
Borrower borrower = Borrower.parse(borrowerChar); // can wrap in try/catch
```

Using enums in logic

* Switch on enum (clear and readable):

```java
switch (bookType) {
    case REFERENCE:
        // apply flat RM100
        break;
    case GENERAL:
        // tiered calculation
        break;
    // ...
}
```

* Check borrower:

```java
if (borrower == Borrower.STAFF) { /* apply staff discount */ }
```

Validation tips

* Validate numeric inputs (days, prevLate): ensure >= 0.
* If you read tokens, always check token != null and token.trim().length() > 0 before charAt(0).
* Provide clear error messages like "Expected book code R/G/M/C/T but got: 'x'".
* Decide policy: either reject a test case with an error, or skip it and continue.

Design choices: Optional vs exceptions

* Use Optional fromCode when you want to handle invalid input gracefully (print message, continue).
* Use parse when invalid codes are programming errors or you prefer try/catch.
* Both can coexist in the enum class.

Performance & thread-safety

* The static Map approach is fast (O(1) lookup) and thread-safe for read-only access after static initialization.
* No synchronization needed because the map is fully initialized in static block before use.

Extensibility

* Add fields like displayName or fine rules to the enum if you want book-specific defaults or behavior encapsulated in the enum (e.g., a method calculateBaseFine(int days) implemented per enum constant).

Quick checklist for robust input mapping

* Trim input
* Convert to uppercase
* Ensure token length > 0 before charAt(0)
* Use BookType.fromCode(...) or parse(...)
* Validate numerical ranges (days >= 0, prevLate >= 0)
* Use enums in calculation code instead of raw chars

I
