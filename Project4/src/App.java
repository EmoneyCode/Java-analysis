/**
 * Main class to run the Java file analysis application.
 */
public class App{
    /**
     * The main method that starts the application.
     * 
     * @param args Command line arguments. The first argument should specify the order ("reversed" or any other value).
     */
    public static void main(String[] args) throws Exception {
        try{
            Read read = new Read();
            read.processJavaFile("JavaFile.txt");
            if (args.length >= 1) {
                read.printOut(args[0]); // passes the first argmunet
            } 
            else if (args.length == 0){
                read.printOut(""); // default alphabetical order
            }
            else {
                System.out.println("Usage: java App <order>"); // Print usage information if the argument format is incorrect
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
