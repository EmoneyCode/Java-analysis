import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Class to read and process a Java file, count keywords and comments, and print analysis results.
 */
public class Read{
    String [] keywords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch",
    "char", "class", "const", "continue", "default", "do", "double",
    "else", "enum", "extends", "final", "finally", "float", "for", "if",
    "goto", "implements", "import", "instanceof", "int", "interface",
    "long", "native", "new", "null", "package", "private", "protected",
    "public", "return", "short", "static", "strictfp", "super", "switch",
    "synchronized", "this", "throw", "throws", "transient", "try", "void",
    "volatile", "while", "true", "false"};

    HashMap <String, Integer> foundWords = new HashMap<> ();
    ArrayList <String> total = new ArrayList <> ();

    public int singleLine = 0;
    public int commentBlocks = 0;
    public int totalLines = 0;

    /**
     * Processes a Java file to count keywords, comments, and total lines.
     * 
     * @param fileName The name of the Java file to process.
     */
    public void processJavaFile(String fileName) {
        try (Scanner scan = new Scanner(new File(fileName))) {
            boolean inCommentBlock = false;

            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                totalLines++;

                if (line.startsWith("/*") && !line.endsWith("*/")) { // Start of a comment block
                    commentBlocks++;
                    inCommentBlock = true;
                } else if (line.startsWith("/*") && line.contains("*/")) { // Entire line is a comment block
                    commentBlocks++;
                    inCommentBlock = false;
                    keyWords(line);
                } else if (line.contains("*/")) { // End of a comment block
                    inCommentBlock = false;
                    keyWords(line);
                } else if (line.contains("//") && !inCommentBlock) { // Single-line comment
                    singleLine++;
                    keyWords(line);
                } else if ( !inCommentBlock) { // Single-line comment
                    keyWords(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Handle file not found exception
        }

    }
    /**
     * Checks if a word is surrounded by quotes.
     * 
     * @param word The word to check.
     * @return true if the word is in quotes, false otherwise.
     */
    private boolean isInQuotes(String word) {
        return (word.startsWith("\"") && word.endsWith("\"")) || (word.startsWith("'") && word.endsWith("'"));
    }
    /**
     * Processes a line of text to extract keywords and update the frequency count.
     * 
     * @param line The line of text to process.
     */
    public void keyWords(String line){
        line = line.replaceAll("\"[^\"]*\"", "\"\""); //get rid of quotes
        String[] parts = line.split("//"); //split commented out code
        String[] words = parts[0].trim().split("[\\p{Punct}\\s\\(\\)”“\"‘’']"); // Split the part by whitespace
        for (String word : words) {
            if (!word.startsWith("//") && !word.startsWith("/*")) { // Exclude comments
                if (!isInQuotes(word)) { // Exclude keywords in quotes
                    for (String keyword : keywords) {
                        if (word.equals(keyword)) {
                            foundWords.put(keyword, foundWords.getOrDefault(keyword, 0) + 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Prints analysis results to a file based on the specified order.
     * 
     * @param order The order in which to print the results ("", "reversed", "increasing", "decreasing").
     */
    public void printOut(String order){
        try(PrintWriter pw = new PrintWriter(new File("analysis.txt"))){
            //print out first part
            pw.printf("Total # of lines: %d\n",totalLines);
            pw.printf("# of single line comments: %d\n",singleLine);
            pw.printf("# of multiline comment blocks: %d\n", commentBlocks);
            if(order.equals("")){ // alphabetical 
                TreeMap<String, Integer> treeMap = new TreeMap<>(foundWords); //put into a tree map because it already does it
                for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    pw.printf("%s - %d\n",key, value);
                }
            }
            
            else if(order.equals("reversed")){ //reversed alphabetical
                Comparator<String> reverseOrderComparator = Comparator.reverseOrder();
                TreeMap<String, Integer> treeMap = new TreeMap<>(reverseOrderComparator);
                treeMap.putAll(foundWords);
                for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    pw.printf("%s - %d\n",key, value);
                }
            }
            // else if (order.equals("increasing")) {
                //  Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue();
                // TreeMap<String, Integer> treeMap = new TreeMap<>(valueComparator);
                // treeMap.putAll(foundWords);
            //     for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            //         String key = entry.getKey();
            //         int value = entry.getValue();
            //         pw.printf("%s - %d\n", key, value);
            //     }
            // } 
            // else if (order.equals("decreasing")) {
            //     Comparator<Map.Entry<String, Integer>> valueComparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
            //     TreeMap<String, Integer> treeMap = new TreeMap<>(valueComparator);
            //     treeMap.putAll(foundWords);
            //     for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            //         String key = entry.getKey();
            //         int value = entry.getValue();
            //         pw.printf("%s - %d\n", key, value);
            //     } 
            //} Couldn't figure it out, but this is my attempt
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}