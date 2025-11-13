import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class librarySystem {

    // Helper calculator encapsulating constants and calculation logic
    public static class LibraryFineCalculator {
        // BigDecimal constants
        private static final BigDecimal ZERO = BigDecimal.ZERO;
        private static final BigDecimal MAGAZINES_FINE = new BigDecimal("0.20");
        private static final BigDecimal GENERAL_BOOKS_TIER1 = new BigDecimal("0.50");
        private static final BigDecimal GENERAL_BOOKS_TIER2 = new BigDecimal("1.00");
        private static final BigDecimal GENERAL_BOOKS_TIER3 = new BigDecimal("2.00");
        private static final BigDecimal THESIS_FINE = new BigDecimal("10.00");
        private static final BigDecimal REF_BOOKS_FINE = new BigDecimal("100.00");
        private static final BigDecimal THESIS_OVERDUE = new BigDecimal("200.00");
        private static final BigDecimal CD_FIRST_10_DAYS = new BigDecimal("2.00");
        private static final BigDecimal CD_OVERDUE = new BigDecimal("5.00");

        private static final BigDecimal PENALTY_OVER_60 = new BigDecimal("25.00");
        private static final BigDecimal PENALTY_HABITUAL = new BigDecimal("10.00");
        private static final BigDecimal STAFF_DISCOUNT = new BigDecimal("0.20"); // 20%
        private static final BigDecimal GOODBORROWER_DISCOUNT = new BigDecimal("0.50"); // 50%

        // Compute base fine according to book type rules (before penalties/discounts)
        public static BigDecimal calculateBaseFine(BookType bookType, int days) {
            if (days <= 0) return ZERO;

            switch (bookType) {
                case REFERENCE:
                    return REF_BOOKS_FINE;
                case GENERAL: {
                    BigDecimal fine = ZERO;
                    int tier1Days = Math.min(days, 7);
                    if (tier1Days > 0) {
                        fine = fine.add(GENERAL_BOOKS_TIER1.multiply(new BigDecimal(tier1Days)));
                    }
                    if (days >= 8) {
                        int tier2Days = Math.min(days, 30) - 7; // days 8..30
                        if (tier2Days > 0) {
                            fine = fine.add(GENERAL_BOOKS_TIER2.multiply(new BigDecimal(tier2Days)));
                        }
                    }
                    if (days > 30) {
                        int tier3Days = days - 30;
                        fine = fine.add(GENERAL_BOOKS_TIER3.multiply(new BigDecimal(tier3Days)));
                    }
                    return fine;
                }
                case MAGAZINE:
                    return MAGAZINES_FINE.multiply(new BigDecimal(days));
                case MULTIMEDIA: {
                    int firstDays = Math.min(days, 10);
                    BigDecimal fine = CD_FIRST_10_DAYS.multiply(new BigDecimal(firstDays));
                    if (days > 10) {
                        fine = fine.add(CD_OVERDUE.multiply(new BigDecimal(days - 10)));
                    }
                    return fine;
                }
                case THESIS: {
                    BigDecimal fine = THESIS_FINE.multiply(new BigDecimal(days));
                    if (days > 15) {
                        fine = fine.add(THESIS_OVERDUE);
                    }
                    return fine;
                }
                default:
                    return ZERO;
            }
        }

        // Apply additional fixed penalties (over 60 days and habitual offender)
        // Penalties apply only if there is an overdue (days > 0)
        public static BigDecimal applyPenalties(BigDecimal currentFine, int days, int prevLate) {
            BigDecimal result = currentFine;
            if (days <= 0) return result;

            if (days > 60) {
                result = result.add(PENALTY_OVER_60);
            }
            if (prevLate >= 3) { // 3 or more previous late returns
                result = result.add(PENALTY_HABITUAL);
            }
            return result;
        }

        // Apply discounts (mutually exclusive). Staff has precedence over Good Borrower.
        public static BigDecimal applyDiscounts(BigDecimal fineBeforeDiscounts, Borrower borrower, int days, int prevLate) {
            BigDecimal result = fineBeforeDiscounts;
            if (result.compareTo(BigDecimal.ZERO) == 0) return result; // nothing to discount

            if (borrower == Borrower.STAFF) {
                BigDecimal discount = result.multiply(STAFF_DISCOUNT);
                result = result.subtract(discount);
                return result;
            }

            // Good borrower reward: 0 previous late returns, returned within 3 days late or less
            if (prevLate == 0 && days > 0 && days <= 3) {
                BigDecimal discount = result.multiply(GOODBORROWER_DISCOUNT);
                result = result.subtract(discount);
            }
            return result;
        }
    }

    // Enum for BookType with helper lookup
    public enum BookType {
        REFERENCE('R'),
        GENERAL('G'),
        MAGAZINE('M'),
        MULTIMEDIA('C'),
        THESIS('T');

        private final char code;
        BookType(char code) { this.code = code; }
        public char getCode() { return code; }

        private static final Map<Character, BookType> CODE_MAP = new HashMap<>();
        static {
            for (BookType bt : values()) CODE_MAP.put(bt.code, bt);
        }

        public static Optional<BookType> fromCode(char c) {
            return Optional.ofNullable(CODE_MAP.get(Character.toUpperCase(c)));
        }
    }

    // Enum for Borrower with helper lookup
    public enum Borrower {
        STUDENT('S'),
        STAFF('T');

        private final char code;
        Borrower(char code) { this.code = code; }
        public char getCode() { return code; }

        private static final Map<Character, Borrower> CODE_MAP = new HashMap<>();
        static {
            for (Borrower b : values()) CODE_MAP.put(b.code, b);
        }

        public static Optional<Borrower> fromCode(char c) {
            return Optional.ofNullable(CODE_MAP.get(Character.toUpperCase(c)));
        }
    }

    // Main: read input, validate, compute and print fines
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) {
            System.out.println("Missing number of test cases.");
            scanner.close();
            return;
        }
        int testCases = scanner.nextInt();

        for (int caseNum = 1; caseNum <= testCases; caseNum++) {
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid or incomplete test case input.");
                break;
            }
            int days = scanner.nextInt();

            if (!scanner.hasNext()) {
                System.out.println("Invalid or incomplete test case input.");
                break;
            }
            String bookToken = scanner.next();

            if (!scanner.hasNext()) {
                System.out.println("Invalid or incomplete test case input.");
                break;
            }
            String borrowerToken = scanner.next();

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid or incomplete test case input.");
                break;
            }
            int prevLate = scanner.nextInt();

            // Basic validation
            if (bookToken.trim().isEmpty() || borrowerToken.trim().isEmpty()) {
                System.out.println("Invalid input");
                continue;
            }
            if (days < 0 || prevLate < 0) {
                System.out.println("Invalid numeric input");
                continue;
            }

            char bookChar = bookToken.trim().toUpperCase().charAt(0);
            char borrowerChar = borrowerToken.trim().toUpperCase().charAt(0);

            Optional<BookType> maybeBook = BookType.fromCode(bookChar);
            Optional<Borrower> maybeBorrower = Borrower.fromCode(borrowerChar);

            if (maybeBook.isEmpty()) {
                System.out.println("Invalid book code: " + bookToken);
                continue;
            }
            if (maybeBorrower.isEmpty()) {
                System.out.println("Invalid borrower code: " + borrowerToken);
                continue;
            }

            BookType bookType = maybeBook.get();
            Borrower borrower = maybeBorrower.get();

            BigDecimal base = LibraryFineCalculator.calculateBaseFine(bookType, days);
            BigDecimal withPenalties = LibraryFineCalculator.applyPenalties(base, days, prevLate);
            BigDecimal finalFine = LibraryFineCalculator.applyDiscounts(withPenalties, borrower, days, prevLate);

            // Format to 2 decimal places
            finalFine = finalFine.setScale(2, RoundingMode.HALF_UP);

            System.out.println("--- Case " + caseNum + " ---");
            System.out.println("Total Fine: RM " + finalFine.toPlainString());
        }

        scanner.close();
    }
}