package spell;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector {
    Trie mainTrie;
    private TreeSet<String> options;
    private TreeSet<String> goodOptions;

    public SpellCorrector(){
        mainTrie = new Trie();
        options = new TreeSet<String>();
        goodOptions = new TreeSet<String>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner myScanner = new Scanner (new BufferedInputStream(new FileInputStream(dictionaryFileName)));

        String newWord;
        while(myScanner.hasNext()){
            newWord = myScanner.next();
            //System.out.println(newWord);
            mainTrie.add(newWord);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (inputWord.equals("")) { return null; }

        inputWord = inputWord.toLowerCase();
        if (mainTrie.find(inputWord) != null) { return inputWord; }
        options.clear();
        goodOptions.clear();

        delete(inputWord);
        transpose(inputWord);
        alter(inputWord);
        insert(inputWord);

        String mostCommon;
        mostCommon = checkOptions();

        if (goodOptions.isEmpty()) {
            TreeSet<String> optionsDuplicate = (TreeSet)options.clone();
            for (String str : optionsDuplicate) {
                delete(str);
                transpose(str);
                alter(str);
                insert(str);
            }
            mostCommon = checkOptions();
            if (goodOptions.isEmpty()) { return null; }
        }

        if (goodOptions.size() == 1) { return goodOptions.first(); }

        return mostCommon;
    }

    public void delete(String inputWord){
        if (inputWord.length() < 2){ return; }
        StringBuilder newWord = new StringBuilder();
        for (int i = 0; i < inputWord.length(); i++){
            newWord.append(inputWord);
            newWord.deleteCharAt(i);
            options.add(newWord.toString());
            newWord.setLength(0);
        }
    }

    public void transpose(String inputWord){
        if (inputWord.length() < 2) return;
        StringBuilder newWord = new StringBuilder();
        StringBuilder subString = new StringBuilder();

        for (int i = 0; i < inputWord.length() - 1; i++) {
            newWord.append(inputWord);
            subString.append(newWord.substring(i, i + 2));
            subString.reverse();
            newWord.replace(i,i + 2, subString.toString());
            options.add(newWord.toString());
            newWord.setLength(0);
            subString.setLength(0);
        }
    }

    public void alter(String inputWord){
        StringBuilder newWord = new StringBuilder();
        char letter;

        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                letter = 'a';
                letter += j;
                newWord.append(inputWord);
                newWord.setCharAt(i, letter);
                options.add(newWord.toString());
                newWord.setLength(0);
            }
        }
    }

    public void insert(String inputWord) {
        StringBuilder newWord = new StringBuilder();
        char letter;

        for (int i = 0; i < inputWord.length() + 1; i++) {
            for (int j = 0; j < 26; j++) {
                letter = 'a';
                letter += j;
                newWord.append(inputWord);
                newWord.insert(i, letter);
                options.add(newWord.toString());
                newWord.setLength(0);
            }
        }
    }

    public String checkOptions(){
        int appearances = 0;
        INode node;
        String mostCommon = null;
        for (String option: options){
            node = mainTrie.find(option);
            if (node != null){
                goodOptions.add(option);
                if (node.getValue() > appearances){
                    appearances = node.getValue();
                    mostCommon = option;
                }
            }
        }
        return mostCommon;
    }

}
