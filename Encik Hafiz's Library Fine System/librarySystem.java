import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class librarySystem {

    public class LibraryFineCalculator {
        //putting into main class(method) with static final will cause illegal modifier error
        /*local variables and method parameters exist only within the scope of that method and 
        cannot be accessed from outside*/

        //General constants for money using BigDeicmal to avoid floating point rounding errors.
        static final BigDecimal ZERO = BigDecimal.ZERO;
        static final BigDecimal MAGAZINES_FINE = new BigDecimal("0.20");
        static final BigDecimal GENERAL_BOOKS_TIER1 = new BigDecimal("0.50");
        static final BigDecimal GENERAL_BOOKS_TIER2 = new BigDecimal("1.00");
        static final BigDecimal GENERAL_BOOKS_TIER3 = new BigDecimal("2.00");
        static final BigDecimal THESIS_FINE = new BigDecimal("10.00");
        static final BigDecimal REF_BOOKS_FINE = new BigDecimal("100.00");
        static final BigDecimal THESIS_OVERDUE = new BigDecimal("200.00");
        static final BigDecimal CD_FIRST_10_DAYS = new BigDecimal("2.00");
        static final BigDecimal CD_OVERDUE = new BigDecimal("5.00");

        static final BigDecimal PENALTY_OVER_60 = new BigDecimal("25.00");
        static final BigDecimal PENALTY_HABITUAL = new BigDecimal("10.00");
        static final BigDecimal STAFF_DISCOUNT = new BigDecimal("0.20"); // 20%
        static final BigDecimal GOODBORROWER_DISCOUNT = new BigDecimal("0.50"); // 50%

        //calculateBaseFine(int days, BookType bookType) 
        // Returns BigDecimal representing base fine before extra penalties/discounts
        static BigDecimal calculateBaseFine(int days, char bookCode) {
            if (days <= 0) return ZERO;

            switch (Character.toUpperCase(bookCode)) {
                case 'R':
                    // Reference: in-library use only — if borrowed and overdue, flat RM100
                    return REF_BOOKS_FINE;
                case 'G': {
                    // General Books: tiered
                    BigDecimal fine = ZERO;
                    if (days >= 1) {
                        int tier1Days = Math.min(days, 7);
                        fine = fine.add(GENERAL_BOOKS_TIER1.multiply(new BigDecimal(tier1Days)));
                    }
                    if (days >= 8) {
                        int tier2Days = Math.min(days, 30) - 7; // days 8..30 inclusive
                        fine = fine.add(GENERAL_BOOKS_TIER2.multiply(new BigDecimal(tier2Days)));
                    }
                    if (days > 30) {
                        int tier3Days = days - 30;
                        fine = fine.add(GENERAL_BOOKS_TIER3.multiply(new BigDecimal(tier3Days)));
                    }
                    return fine;
                }
                case 'M':
                    // Magazines: flat 0.20 per day
                    return MAGAZINES_FINE.multiply(new BigDecimal(days));
                case 'C': {
                    // Multimedia CDs/DVDs: RM2 for first 10 days, RM5 afterward
                    int firstDays = Math.min(days, 10);
                    BigDecimal fine = CD_FIRST_10_DAYS.multiply(new BigDecimal(firstDays));
                    if (days > 10) {
                        fine = CD_OVERDUE.multiply(new BigDecimal(days - 10)));
                    }
                    return fine;
                }
                case 'T': {
                    // Thesis: RM10/day, plus RM200 if overdue > 15 days
                    BigDecimal fine = THESIS_FINE.multiply(new BigDecimal(days));
                    if (days > 15) {
                        fine = fine.add(THESIS_OVERDUE);
                    }
                    return fine;
                }
                default:
                    // unknown code -> treat as zero or throw; choose to return ZERO
                    return ZERO;
            }
        }

        //applyDiscounts(BigDecimal fine, Borrower borrower, int days, int prevLate)
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
    }

    public static void main(String[] args) {
        
        int daysOverdue;
        char bookTypeCode;
        char borrowerCtegory;
        int prevLateReturns;
        BigDecimal totalFine;

        Scanner scanner =new Scanner(System.in);
        int testCases = scanner.nextInt();
        for (int caseNum = 0; caseNum < testCases; caseNum++) {
            int days = scanner.nextInt();
            String bookCode = scanner.next();
            int prevLate = scanner.nextInt();

            BigDecimal base = calculateBaseFine(days, bookCode.charAt(0));
            BigDecimal afterPenalties = applyPenalties(base, days, prevLate);
            BigDecimal finalFine = applyDiscounts(afterPenalties, bookCode.charAt(1), days, prevLate);

            finalFine = finalFine.setScale(2, RoundingMode.HALF_UP);
            System.out.println("--- Case " + caseNum + " ---");
            System.out.println("Total Fine: RM " + finalFine.toPlainString());
        }
        scanner.close();

        
        
        //enum BookType {R ,G, M ,C, T} and Borrower {Student, Staff }

        //Read tokens: days, bookCode, borrowerCode, prevLateReturns

        //Helper methods for computing fine

        

        //applyAdditionalPenalties(BigDecimal fine, int days, int prevLate) 
        static BigDecimal applyPenalties(BigDecimal fine, int days, int prevLate) {
            if (days > 60) {
                fine = fine.add(PENALTY_OVER_60);
            }
            if (prevLate > 3) {
                fine = fine.add(PENALTY_HABITUAL);
            }
            return fine;
        }

        
    }
}


