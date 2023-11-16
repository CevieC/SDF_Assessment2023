package playstore;


public class Main{
    public static void main(String[] args) throws Exception {

        // check if the csv filename exists in the parameter
        if(args.length <= 0) {
            System.err.println("No CSV File found, try again.");
            System.exit(1);
        }
        PlayStore.getMap(args[0]);
        PlayStore.sortCategory();
        PlayStore.display();
    }
}