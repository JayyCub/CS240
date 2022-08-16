package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    Set<String> wordSet;
    SortedSet<Character> usedLetters;
    String word;
    Map<String, TreeSet<String>> doubleMap;

    public EvilHangmanGame(){
        wordSet = new HashSet<>();
        usedLetters = new TreeSet<>();
        word = "";
        doubleMap = new HashMap<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        scanner.useDelimiter("(#[^\\n]*\\n)|(\\s+)+");
        if (!scanner.hasNext()){
            throw new EmptyDictionaryException();
        }
        wordSet.clear();
        while (scanner.hasNext()){
            String string = scanner.next();
            if (string.length() == wordLength){
                wordSet.add(string);
            }
        }

        if (wordSet.isEmpty()){
            throw new EmptyDictionaryException();
        }
        word = "-".repeat(wordLength);
        System.out.println(word);
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (guess >= 'A' && guess <= 'Z'){
            guess += 32;
        }
        if (usedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }
        usedLetters.add(guess);
        String token = findBigSubset(guess);
        int numHits = updateWordSoFar(guess, token);
        respondToUser(guess, numHits);
        return wordSet;
    }

    private void respondToUser(char guess, int numHits){
        StringBuilder builder = new StringBuilder();
        if (numHits > 0){
            builder.append("Yes, there is ").append(numHits).append(" ").append(guess).append("\n\n");
        } else {
            builder.append("Sorry, there are no ").append(guess).append("'s\n\n");
        } System.out.println(builder);
    }

    private int updateWordSoFar(char guess, String token){
        int numHits = 0;
        for (int i = 0; i < token.length(); i++){
            if (token.charAt(i) == guess){
                word = word.substring(0, i) + guess + word.substring(i  + 1);
                numHits++;
            }
        }
        return numHits;
    }

    private String findBigSubset(char guess){
        StringBuilder builder = new StringBuilder();
        TreeSet<String> temporary;
        int max = 0;
        doubleMap.clear();

        for (String string : wordSet){
            builder.setLength(0);
            for (int i = 0; i < string.length(); i++){
                if (string.charAt(i) == guess){
                    builder.append(guess);
                } else {
                    builder.append("-");
                }
            }
            temporary = doubleMap.getOrDefault(builder.toString(), new TreeSet<>());
            temporary.add(string);
            doubleMap.put(builder.toString(), temporary);
            if (temporary.size() > max){
                max = temporary.size();
            }
        }
        TreeSet<String> sortedPatterns = new TreeSet<>();
        for (String string : doubleMap.keySet()){
            if (doubleMap.get(string).size() == max){
                sortedPatterns.add(string);
            }
        }
        wordSet = doubleMap.get(sortedPatterns.first());
        return sortedPatterns.first();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return usedLetters;
    }

    public String getWord() {
        return word;
    }
}
