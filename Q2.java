/**
 * WIX1002 Fundamentals of Programming
 * Viva 1: Flow Control - Question 2
 * 
 * Question 2: The Campus "Kopi-Satu" Billing System
 * 
 * Description:
 * This program implements a smart digital billing system for the "Kopi-Satu" café
 * that automatically calculates taxes, applies discounts, and rewards loyal customers
 * based on various conditions including purchase amount, day of the week, and time.
 * 
 * Features:
 * - Input validation for item prices (non-negative, at least one item required)
 * - Service tax calculation based on subtotal tiers
 * - Multiple discount types: Student Saver, Happy Hour, Weekend Combo
 * - Membership loyalty cashback
 * - Detailed receipt formatting with proper alignment
 * 
 * Input Requirements:
 * 1. Item prices (enter 0 to finish)
 * 2. Day of the week
 * 3. Hour in 24-hour format
 * 4. Membership status (Y/N)
 * 
 * Output:
 * Formatted receipt showing subtotal, taxes, discounts, and final amount
 * 
 * @author Chang Zhang (Elvira) ,24083843/1
 * @version 1.0
 * @since 2025 Nov 13th
 */


import java.util.Scanner; // this program requires some data from the users

public class Q2 {


public static void main(String[] args) {

    // an object allow users to input
    Scanner data = new Scanner(System.in);

/**************************************************************************************** */

    int item = 0,time; 
    double total = 0.0, pay = 0;
    String day; 


/**************************************************************************************** */

    // 1. Item prices (enter 0 to finish)
    while(true){
        // ask for price
        System.out.print("Enter item price (0 to finish): ");
        double price = data.nextDouble();


        /* 
        check : 
        (1) at least one valid item 
        (2) no negative prices 
        (3) 0 to stop

        addition:
        (4) calculate num of item to make sure function (1) work
        (5) calculate total money needed to be paid by now for future use 
        */ 


        //(3)
        if(price == 0){ 
            // (1)
            if(item == 0){ 
                System.out.println("Please enter at least one valid item is entered before proceeding.");
                continue; 
            }
            break;
        }
        //(2)
        else if (price < 0){
            System.out.println("Invalid amount. Price cannot be negative. Please re-enter."); 
            continue; 
        }
        else{
            //(4)
            item += 1;
            //(5)
            total += price;
        }
    }


    /*
    This one is interesting: 
    if we don't use the following statement, it will show like this:

    Enter item price (0 to finish): 15
    Enter item price (0 to finish): 22
    Enter item price (0 to finish): 0
    Enter day of week: Not a valid day, please enter again: 
    Enter day of week: 

    "
    Reason: nextDouble() only reads the number but leaves the newline character (\n) 
    in the input buffer. When nextLine() runs immediately after, it reads that leftover 
    newline as an empty input, causing it to skip waiting for user input.
    
    Solution: data.nextLine() clears the buffer by consuming the leftover newline,
    allowing the next nextLine() to work properly.
    "
                                                                        —————— DeepSeek
    */
    data.nextLine();

/**************************************************************************************** */


    // 2. Day of the week
    while(true){
    //ask for day of week
    System.out.print("Enter day of week: ");
    day = data.nextLine();

    /*
    check:
    (1) if the entered day is valid
     */

    
    //(1)
    if (day.equalsIgnoreCase("Monday") ||day.equalsIgnoreCase("Tuesday")||day.equalsIgnoreCase("Wednesday")||day.equalsIgnoreCase("Thursday")||day.equalsIgnoreCase("Friday")||day.equalsIgnoreCase("Saturday")||day.equalsIgnoreCase("Sunday")){
        break;
    }
    else{
        System.out.println("Not a valid day, please enter again: ");
        continue;
    }
    
    }
    
/**************************************************************************************** */

    // 3. Hour in 24-hour format
    do{
        //ask for time 
        System.out.print("Enter hour (24-hour format): ");
        time = data.nextInt();
    } 
    while (0> time || time >23); // check: if the enyered time is valid: 0 <= time <= 23

/**************************************************************************************** */

    // 4. Membership status (Y/N)
    System.out.print("Is customer a member (Y/N)? ");
    String member = data.next();
    /*
    Noted:
    Although .next() just read one character, we can't set the variable type as 'char'
     */
/**************************************************************************************** */


/*
①
The following program use one logic repeadly:

    System.out.printf("......%__s","RM ");
    System.out.printf("%.__f\n", var);

This logic is used to make sure the outputs are represented like sample output

However, for this part '%__s', we need to count what to enter in __, which is 
a little bit inconvenience

②
Also, as % is a special symbol, when we need to print it, we have to add another % before it

 */

    System.out.println("------ Kopi-Satu Receipt ------------");


    // subtotal (total)
    System.out.printf("Subtotal:%22s","RM ");
    System.out.printf("%.2f\n",total);
    
/**************************************************************************************** */

    // sevice taxes based on subtotal


    /*
    print out taxes:

    (1) Subtotal ≤ RM 30 → 6%
    (2) RM 30 < Subtotal ≤ RM 100 → 8%
    (3) Subtotal > RM 100 → 10%

    calculate total money to pay by now (pay = subtotal + tax):
    (4) Subtotal ≤ RM 30 → 106%
    (5) RM 30 < Subtotal ≤ RM 100 → 108%
    (6) Subtotal > RM 100 → 110%

     */


    //(1)
    if (total <= 30){
        System.out.printf("Service Tax (6%%)%15s", "RM ");
        System.out.printf("%.2f\n", total*0.06);
        //(4)
        pay = total *1.06;
    }
    //(2)
    else if (30 < total && total<=100){
        System.out.printf("Service Tax (8%%)%15s", "RM ");
        System.out.printf("%.2f\n", total*0.08);
        //(5)
        pay = total *1.08;
    }
    //(3)
    else{
        System.out.printf("Service Tax (10%%)%15s", "RM ");
        System.out.printf("%.2f\n", total*0.10);
        //(6)
        pay = total *1.10;
    }

/**************************************************************************************** */

    // print out the total payment

    System.out.printf("Total before discount:%9s","RM ");
    System.out.printf("%.2f\n",pay);

/**************************************************************************************** */


    //Apply Discounts:

    /*
    print out discount(based on day & time & total & pay):

    (1)Student Saver Discount (Weekdays only):
        If total before discount > RM 25, apply 10% discount.
    (2)Happy Hour Discount (Mon–Fri, 15:00 to 16:59):
        Additional 5% discount
    (3)Weekend Combo Discount (Sat–Sun):
        If subtotal ≥ RM 50, apply 5% discount.

    calculate the money truly needed to be paid:
    (4)Student Saver Discount (Weekdays only):
        If total before discount > RM 25, apply 10% discount.
    (5)Happy Hour Discount (Mon–Fri, 15:00 to 16:59):
        Additional 5% discount
    (6)Weekend Combo Discount (Sat–Sun):
        If subtotal ≥ RM 50, apply 5% discount.

    */


    //(3)
    if (day.equalsIgnoreCase("saturday")|| day.equalsIgnoreCase("sunday")){
        if(total >= 50){
            System.out.printf("Weekend Combo Discount (5%%):RM ");
            System.out.printf("%.2f\n",pay*0.05);
            //(6)
            pay -= pay *0.05;
        }
    }
    else{
        //(1)
        if(pay > 25){
            System.out.printf("Student Discount (10%%):%8s","RM ");
            System.out.printf("%.2f\n",pay*0.1);
            //(4)
            pay -= pay *0.1;
        }
        // (2)
        if(15<=time && time < 17){
            System.out.printf("Happy Hour Discount (5%%):%6s","RM ");
            System.out.printf("%.2f\n",pay*0.05);
            //(5)
            pay -= pay * 0.05;
        }
    }
 
    System.out.println("-------------------------------------");

/**************************************************************************************** */

    // total payable
    System.out.printf("Total Payable: %16s" , "RM ");
    System.out.printf("%.2f\n" , pay);

/**************************************************************************************** */

    //Apply Membership Loyalty Cashback:

    /*
    print out the cashback(based on member):
    (1) If the customer enters Y for membership, they earn a 2% cashback on the 
    final payable amount.
     */

    if (member.equalsIgnoreCase("Y")){
        System.out.printf("Loyalty Cashback (2%%):%9s","RM ");
        System.out.printf("%.2f\n",pay*0.02);
    }

    System.out.println("-------------------------------------");

/**************************************************************************************** */

    // conclusion
    System.out.printf("Final Amount to Collect: %6s","RM ");
    System.out.printf("%.2f\n" , pay);


    //'close' the object
    data.close();

}

}
