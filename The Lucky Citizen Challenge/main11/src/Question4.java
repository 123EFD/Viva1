import java.util.Scanner;

public class Question4 {
        public static void main(String[] args) {
          Scanner sc = new Scanner(System.in);
        
        //ask user for enter IC
        System.out.print("Enter IC number (YYMMDD-##-####): ");
        String ic = sc.nextLine();    //since there is - inside ic so we can't use int/long
        
        //to remove'-' symbol
        String icDigits = "";   //create an empty string, use it to store the IC num without '-'
        int index = 0;   //counter variable,keep track of which character we are currently looking, e.g index = 0 means start from first char
        while(index < ic.length()){    //keep looping if less than,if more then stop 
            char c = ic.charAt(index);  //takes one char from ic string at the position index,eg.when index = 3, c ='0',when index = 6, c='-'
            if (c != '-'){     //if char not '-',then add it to icDigits,so 0,1,2,..will be added,' - 'will skipped
                icDigits += c;
            }
            index++;  //to move to next char
    }
        //get birthday from first 6 num ; turn a digit character into an integer
        int yy = (icDigits.charAt(0)- '0')* 10 + (icDigits.charAt(1) - '0');   //from .charAt()we get is char,not num, we need to convert it to num by - '0',in ASCII,'0'=48, charAt(0)-0 == 48-48 = 0(then we get real 0), 1 is 49(49-48=1)
        int mm = (icDigits.charAt(2)- '0')* 10 + (icDigits.charAt(3) - '0');   //'2' is 50(50-48=2) ,...
        int dd = (icDigits.charAt(4)- '0')* 10 + (icDigits.charAt(5) - '0');   // *10 is to get the tens places, if we didn't*10, 4+5 become 9 since'4'=0,'5'=3 so(0*10)+3 =3 (for the case(010203 if u didn't *10 u still can get cuz 0+2=0, but if 050916?, if 1+6 become 7 so we must (1*10)+6
        
        // year
        int year;
        if (yy >= 0 && yy <= 25){   //yy is char('0','1'), check if the num is between 0 and 25, if yes, then its born in 2000-2025
            year = 2000+ yy;
        }else{
            year = 1900 + yy;       //else, its between 26 and 99, then its born in 1926-1999
        }
        
        //gender
        int lastDigit = icDigits.charAt(icDigits.length() - 1) - '0';   //.length is to find the total num of the str, eg.Hai the length is 3 but i is 2 so when we get length then we -1, then we get the last str which (3-1=2), - '0' is to convert to integer  
        String gender;
        if (lastDigit % 2 == 0){
            gender = "Female";
        }else {
            gender = "Male";
        }
        
        //monthType Long or Short
        String monthType;
        if ( mm == 1 || mm == 3 || mm == 5 || mm == 7 || mm == 8 || mm == 10 || mm == 12 ){   //31 days
            monthType = "Long";  
        } else{
            monthType = "Short";  //28-30 days
        }
        
        //sum of IC digits
        int sum = 0;    //initialize sum = 0 
        for( int i = 0; i < icDigits.length(); i++){   //loop until last digit(.length())
            sum += icDigits.charAt(i) - '0';           // convert the i char in to integer and total it
        }
        
        //Determine Lucky Winner
        String lucky;
        if (gender.equals("Male")){
            if (sum % 5 == 0 && monthType.equals("Short")){   //male
                lucky = "Yes" ;
            }else {
                lucky = "No";
            }
        }else{
            if (sum % 7 == 0 && monthType.equals("Long")){   //female
                lucky = "Yes";
            }else {
                lucky = "No";
            }
        }
            
            System.out.println("Birth Date: " + dd + "/" + mm + "/" + year);
            System.out.println("Gender: " + gender);
            System.out.println("Sum of Digits: " + sum);
            System.out.println("Lucky Winner: " + lucky );
            
            sc.close();
        
}
}


