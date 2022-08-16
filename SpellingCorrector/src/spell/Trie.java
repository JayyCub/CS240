package spell;

public class Trie implements ITrie {
    private final Node root;
    private int wordCount;
    private int nodeCount;
    private int hashNum;

    public Trie() {
        root = new Node();
        wordCount = 0;
        nodeCount = 1;
        hashNum = 0;
    }


    @Override
    public String toString(){
        StringBuilder currWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        return toString_Helper(root, currWord, output);
    }

    public String toString_Helper(INode currNode, StringBuilder word, StringBuilder wordList) {
        char letter;

        for (int i = 0; i < currNode.getChildren().length; i++) {
            INode child = currNode.getChildren()[i];
            if (child != null) {
                letter = 'a';
                letter += i;
                word.append(letter);
                if (currNode.getChildren()[i].getValue() > 0) {
                    wordList.append(word);
                    wordList.append("\n");
                }
                toString_Helper(child, word, wordList);
            }
        }
        if (word.length() > 1) {
            word.deleteCharAt(word.length() - 1);
        } else {
            word.setLength(0);
        }

        return wordList.toString();
    }

    @Override
    public boolean equals(Object o){

        // is o null? if yes, return false
        // is o equal to this? if yes, return true;
        // do this an o have the same class? no -> return false

        Trie d = (Trie) o;
        // do this and d have the same word and node count?

        if (!(o instanceof Trie)){ return false; }
        Trie trie = (Trie) o;
        if (d.wordCount != this.wordCount) { return false; }
        if (d.nodeCount != this.nodeCount) { return false; }
        if (d.hashNum != this.hashNum) { return false; }

        return equals_Helper(this.root, d.root);
    }

    private boolean equals_Helper(Node n1, Node n2){
        /* Compare n1 and n2 to see if they are the same
            // Do n1 and n2 have the same count?
            // Do n1 and n2 have non null children in exactly the same indexes

        // Recurse on the children and compare the child sub trees*/

        if (n1.getValue() != n2.getValue()) return false;

        for (int i = 0; i < n1.getChildren().length; i++) {
            Node child1 = n1.getChildren()[i];
            Node child2 = n2.getChildren()[i];

            if (child1 != null && child2 != null) {
                equals_Helper(child1, child2);
            }
            else if (child1 != null || child2 != null) return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
/*
        // combine the following values:
        // 1. wordcount
        // 2. nodeCount
        // 3. The index of each of the root node's non null children*/

        for (int i = 0; i < 26; i++){
            if (root.getChildren()[i] != null){
                return i * nodeCount * wordCount;
            }
        }

        return nodeCount * wordCount * 50;
    }

    @Override
    public void add(String dictWord) {
        int hashRand = 32;
        char wordCharacter;
        int index;
        Node currNode = root;

        dictWord = dictWord.toLowerCase();
        hashNum += hashRand * dictWord.hashCode();

        for (int i = 0; i < dictWord.length(); i++){
            wordCharacter = dictWord.charAt(i);
            index = wordCharacter - 'a';
            if (currNode.getChildren()[index] == null){
                currNode.getChildren()[index] = new Node();
                nodeCount++;
            }
            if (i == dictWord.length() - 1){
                if (currNode.getChildren()[index].getValue() == 0){
                    wordCount++;
                }
                currNode.getChildren()[index].incrementValue();
            }
            currNode = currNode.getChildren()[index];
        }
    }

    @Override
    public INode find(String word) {
        char letter;

        int index = 0;
        Node currNode = root;

        word = word.toLowerCase();

        for (int i = 0; i < word.length(); i++) {
            letter = word.charAt(i);
            index = letter - 'a';
            Node child = currNode.getChildren()[index];

            if (child == null) return null;
            if (i < word.length() - 1) currNode = child;
        }

        if (currNode.getChildren()[index].getValue() > 0) {
            return currNode.getChildren()[index];
        }

        return null;
    }

    @Override
    public int getWordCount() { return wordCount; }

    @Override
    public int getNodeCount() { return nodeCount; }
}
