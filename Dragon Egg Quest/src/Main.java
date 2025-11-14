import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Chest lists (3 eggs, 2 cursed chests, 5 empty chests)
        String[] chests = {"Egg", "Egg", "Egg", "Cursed Chest", "Cursed Chest", "Empty", "Empty",
        "Empty", "Empty", "Empty"};

        //Randomisation of chest contents
        List<String> chestList = Arrays.asList(chests);
        Collections.shuffle(chestList);
        chests = chestList.toArray(new String[0]);

        //this is to keep track of where chests containing eggs are located and cursed chests
        List<Integer> eggPositions = new ArrayList<>();
        for (int i = 0; i < chests.length; i++) {
            if (chests[i].equals("Egg")) {
                eggPositions.add(i + 1); // +1 to match chest numbering (1-10)
            }
        }

        //Variables
        int eggsFound = 0; // Number of eggs found so far
        int attempts = 10; // total attempts
        Scanner scanner = new Scanner(System.in);

        //Introduction lines
        System.out.println("Welcome to the Kingdom of Malaya!");
        System.out.println("There are 3 Magical Dragon Eggs hidden inside the royal vault.");
        System.out.println("There are 10 chests and you have 10 attempts to find all the eggs.");
        System.out.println("Good luck, brave adventurer! But beware of the curses!\n");

        //loop system for attempts at finding eggs
        while (attempts > 0 && eggsFound < 3) {
            System.out.println("Choose a chest to open (1-10). You have " + attempts + " attempts left.");
            int choice = scanner.nextInt();
            scanner.nextLine();

            // Input validation, user must pick number between 1-10
            if (choice < 1 || choice > 10) {
                //invalid number will not consume an attempt, user will be prompted to choose again
                System.out.println("Invalid choice. Please choose a chest between 1 and 10.\n");
                continue;
            }

            //Revealing chest contents
            String result = chests[choice - 1];
            System.out.println("You opened Chest " + choice + " and found: " + result + "!");

            //handling the results of individual chest openings
            if (result.equals("Egg")) {
                eggsFound++;
                System.out.println("Congratulations! You found a Magical Dragon Egg! Total Eggs Found: " + eggsFound);
                if (eggsFound == 3) {
                    System.out.println("You have found all 3 Magical Dragon Eggs! You win, adventurer!\n");
                }
            } 
            
            else if (result.contains("Cursed Chest")) {
                System.out.println("Oh no! You opened a Cursed Chest!");
                attempts -= 1; // Penalty for cursed chest, will lose 2 attempts in total
            } 
            
            else if (result.equals("Empty")) {
                System.out.println("This chest is empty.");

                //Finding the number of chests to the nearest one containing egg
                int nearestEggDistance = Integer.MAX_VALUE;
                int nearestEggChest = -1;
                for (int eggPos : eggPositions) { //list storing the positions of all chests containing eggs
                    int distance = Math.abs(eggPos - choice);
                    if (distance < nearestEggDistance) {
                        nearestEggDistance = distance;
                        nearestEggChest = eggPos;
                    }
                }

                //Warm/Cold hint system, if chest number picked is within 3 chests containing an egg, then "warm" hint is given
                if (nearestEggDistance <= 3) {
                    System.out.println("You're warm! You're very close to a Dragon Egg!");
                } 
                
                else {
                    System.out.println("You're cold! You're far from any Dragon Egg.");
                }

                //gives hint about trying a higher/lower chest number depeending on previous input
                if (choice < nearestEggChest) {
                    System.out.println("Try a higher number next time.");
                } 
                
                else if (choice > nearestEggChest) {
                    System.out.println("Try a lower number next time.");
                }

                System.out.println("No egg here. Keep searching!\n");
            }

            attempts--; // Decrement attempts after each try
            System.out.println("Eggs Found: " + eggsFound + ", Attempts Left: " + attempts + "\n");
        }

        //End of game
        if (eggsFound == 3) {
            System.out.println("Congratulations! All of the dragon eggs are safe!");
        } else if (attempts == 0) {
            System.out.println("Game over! Some dragon eggs remain hidden!");
            System.out.println("You found " + eggsFound + " Magical Dragon Eggs. Better luck next time!");
        }

        scanner.close();
    }
}
