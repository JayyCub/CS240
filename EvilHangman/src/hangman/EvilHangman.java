package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        try {
            String inputDictionary = args[0];
            int wordlength = Integer.parseInt(args[1]);
            int gameCounts = Integer.parseInt(args[2]);
            if (gameCounts < 1) {
                throw new ArrayIndexOutOfBoundsException();
            }
            String inputGuess;
            char charGuess;
            Set<String> possibleWords = new HashSet<>();
            EvilHangmanGame game = new EvilHangmanGame();
            File file = new File(inputDictionary);
            Scanner scanner = new Scanner(System.in);

            game.startGame(file, wordlength);

            while(gameCounts > 0){
                try {
                    String word = game.getWord();
                    promptUser(gameCounts, game.usedLetters, word);
                    inputGuess = scanner.next();
                    if (inputGuess.isBlank()){
                        throw new Exception();
                    }
                    charGuess = inputGuess.charAt(0);
                    if (inputGuess.length() > 1){
                        throw new Exception();
                    }
                    if ((charGuess < 'a' || charGuess > 'z') && (charGuess < 'A' || charGuess > 'Z')){
                        throw new Exception();
                    }
                    possibleWords = game.makeGuess(charGuess);

                    CharSequence dash = "-";
                    if (!game.getWord().contains(dash)){
                        gameCounts = -1;
                    }
                    if (word.equals(game.getWord())){
                        gameCounts--;
                    }


                } catch (GuessAlreadyMadeException e){
                    System.out.println("\nThat guess was already made\n");
                } catch (Exception e) {
                    System.out.println("\nInvalid guess\n");
                }
            }
            if (gameCounts < 0){
                System.out.println("You Win!\nThe word is: ");
                System.out.println(possibleWords.toArray()[0]);
            } else {
                System.out.println("You lose!\n The word is: ");
                System.out.println(possibleWords.toArray()[0]);
            }

        } catch (ArrayIndexOutOfBoundsException exception){
            System.out.print("Number of guesses must be 1 or more");
        }
    }

    public static void promptUser(int gameCounts, SortedSet<Character> usedLetters, String word){
        StringBuilder builder = new StringBuilder();
        builder.append("You have " ).append(gameCounts).append(" guesses left\n").append("Used letters: ");
        for (Character character: usedLetters){
            builder.append(character).append(" ");
        }
        builder.replace(builder.length() - 1, builder.length(), "\n");
        builder.append("Word: ").append(word).append("\n").append("Enter guess: ");

        System.out.print(builder);
    }
}