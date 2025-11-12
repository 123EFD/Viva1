package security.um;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //1.read username and password from user
        
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        
        boolean isUserNameValid = true; // placeholder for actual username validation logic

        //2.USERNAME VALIDATION
        //FIRST CONDITION 5-15 CHARACTERS LONG
        if (username.length() < 5 || username.length() > 15) {
            isUserNameValid = false;    
        }

        //checking number one
        //PROCEED WHEN ISUSERNAMEVALID = TRUE, WHEN ITS BETWEEN 5 - 15 CHARACTERS
        // must be a lowercase letter at the start
        if (isUserNameValid) {
            // rule 1: must start with a letter (A-Z or a-z) and not a number or special character
            //rule 4: must NOT contain uppercase letters

            //check if its the length of username is greater than 0 which meets rule 1 or not
            if (username.length() > 0) {
                char firstChar = username.charAt(0);
                //check the first character index 0 if its a letter lowercase
                if (!Character.isLowerCase(firstChar)) {
                    isUserNameValid = false;
                }
            
            // the first condition will not happen if rule 2(min 5 -15 chars) is met & isUserNameValid = false
            } else {
                isUserNameValid = false;
            }
        }

        //checking number two
        //rule 3 (can contain letters, digits, and underscores only) + rule 4 only lowercase no uppercase

        if(isUserNameValid){
            for (int i = 0; i < username.length(); i++) { //loops through each single character inside username
                char ch = username.charAt(i);
                if (!Character.isLowerCase(ch) && !Character.isDigit(ch) && ch != '_') { //if any one of them returns true, isUsernameValid = false, bcs its an and operator
                    isUserNameValid = false;
                    break; //exit the loop if invalid character found
                }
            }
        }

        //checking number three
        //3.when username is valid, proceed to password validation
        //if is username is invalid, print invalid username message to user
        if(isUserNameValid==false){
            System.out.println("Invalid Username"); // if any of these 3 checkings fail, username is invalid
        } else { //if its valid, now we do the password checking
            //password validation

            int rulesMet = 0; //total rules met
            String specialCharacters = "!@#$%^&*";

            //placeholders for each rule to keep track, false bcs we havent checked yet
            boolean haveUppercase = false;
            boolean haveLowercase = false;
            boolean haveDigit = false;
            boolean haveSpecialChar = false;

            for (int i = 0; i<password.length();i++){ //loop through each character in password
                char ch = password.charAt(i);

                if (Character.isUpperCase(ch)){
                    haveUppercase = true;
                } else if (Character.isLowerCase(ch)){
                    haveLowercase = true;
                } else if (Character.isDigit(ch)){
                    haveDigit = true;
                } else {                          //@
                    if (specialCharacters.indexOf(ch) != -1){ //indexOf looks for the position of the character in the string specialCharacter, for example @, if found in string based on the index, haveSpecialChar = true
                        haveSpecialChar = true;
                    }
                }

            }

            if(haveUppercase) rulesMet++; //if its true(rules met), add 1 to rulesMet
            if(haveLowercase) rulesMet++;
            if(haveDigit) rulesMet++;
            if(haveSpecialChar) rulesMet++;

            //check remaining rules: length at least 8 characters
            if (password.length()>=8){
                rulesMet++;
            }

            //if the password have no spaces, increment rulesMet by 1
            if(!password.contains(" ")){
                rulesMet++;
            }

            //check if whether username appears in password
            if(!password.toLowerCase().contains(username.toLowerCase())){ //converts both to lowwercase, ! not contains
                rulesMet++;
            }

            //password strength
            String strength;
            if(rulesMet == 7){
                strength = "Very Strong";
            } else if (rulesMet == 6){
                strength = "Strong";
            } else if (rulesMet >= 4){ //4-5
                strength = "Moderate";
            } else {//smaller than 4 (less than or equal to 3)
                strength = "Weak";
            }

            System.out.println("Password Strength:"+strength); //print inside the else loop when username is valid
        }
    }
}
