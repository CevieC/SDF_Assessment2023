package playstore;


public class Main{
    public static void main(String[] args) throws Exception {

        // check if the csv filename exists in the parameter
        // if(args.length <= 0) {
        //     System.err.println("No CSV File found, try again.");
        //     System.exit(1);
        // }
        PlayStore.getMap("sdf-assessment\\task01\\googleplaystore.csv");
        PlayStore.sortCategory();
        PlayStore.display();
    }
}