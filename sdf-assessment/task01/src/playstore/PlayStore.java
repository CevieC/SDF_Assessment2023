package playstore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Category {
    public String m_catName;
    public String m_topAppName;
    public float m_topAppRating;
    public String m_btmAppName;
    public float m_btmAppRating;
    public float m_avgCatRating;
    public int m_numRecord;
}


public class PlayStore {

    private String m_appName;
    private String m_category;
    private float m_rating;
    static Map<String, List<PlayStore>> grouped = new HashMap<String, List<PlayStore>>();
    static Map<String, Integer> discardedRecordsCountPerCategory = new HashMap<>();
    static List<Category> catList = new ArrayList<Category>();

    public PlayStore(){

    }

    public PlayStore(String _app, String _category, float _rating){
        m_appName = _app;
        m_category = _category;
        m_rating = _rating;

    }

	public String getM_app() { return this.m_appName; }

	public String getM_category() { return m_category; }

	public float getM_rating() { return this.m_rating; }


    public static Map<String, List<PlayStore>> getMap(String _filePath) throws IOException {
        try {
            FileReader reader = new FileReader(_filePath);
            BufferedReader br = new BufferedReader(reader);
    
            br.readLine(); // Skip the header line if present
    
            grouped = br.lines()
                .skip(1)
                .map(row -> row.trim())
                .filter(row -> !row.isEmpty())
                .map(row -> row.split(","))
                .filter(fields -> isValidData(fields))
                .map(fields -> new PlayStore(fields[Constants.COL_APP], fields[Constants.COL_CATEGORY].toUpperCase(), Float.parseFloat(fields[Constants.COL_RATING])))
                .collect(Collectors.groupingBy(PlayStore::getM_category));

            br.close();
    
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + _filePath);
            e.printStackTrace();
        }
        return grouped;
        
    }
    
    private static void incrementDiscardedCount(String _category) {
        discardedRecordsCountPerCategory.put(_category, discardedRecordsCountPerCategory.getOrDefault(_category, 0) + 1);
    }

    private static boolean isValidData(String[] _fields) {
        // Check if the rating field is not 'NaN'
        if (_fields[Constants.COL_RATING].equalsIgnoreCase("NaN")) {
            incrementDiscardedCount(_fields[Constants.COL_CATEGORY]);
            return false;
        }
    
        // Check if the rating is a valid float number
        try {
            Float.parseFloat(_fields[Constants.COL_RATING]);
        } catch (NumberFormatException e) {
            incrementDiscardedCount(_fields[Constants.COL_CATEGORY]);
            return false;
        }
        return true;
    }
    
    public static void sortCategory() {
        for (String cat : grouped.keySet()) {
            Category category = new Category();
            category.m_catName = cat;
            List<PlayStore> apps = grouped.get(cat);
            String topApp = "";
            float topRating = 0;
            String btmApp = "";
            float btmRating = 50;
            for (PlayStore app : apps) {
                if (app.m_rating >= topRating) {
                    topRating = app.m_rating;
                    topApp = app.m_appName;
                }
                if (app.m_rating <= btmRating) {
                    btmRating = app.m_rating;
                    btmApp = app.m_appName;
                }
                category.m_avgCatRating += app.getM_rating();
                ++category.m_numRecord;
            }
            category.m_topAppName = topApp;
            category.m_topAppRating = topRating;
            category.m_btmAppName = btmApp;
            category.m_btmAppRating = btmRating;
            category.m_avgCatRating /= category.m_numRecord;
            catList.add(category);
        }
    }

    // Calculates the data needed for display
    public static void display(){
        
        int count = 1;
        for (Category cat : catList) {
            System.out.println("Category " + count + " : " + cat.m_catName);
            System.out.printf("    Highest: %s, Rating: %.2f\n", cat.m_topAppName, cat.m_topAppRating);
            System.out.printf("    Lowest: %s, Rating: %.2f\n", cat.m_btmAppName, cat.m_btmAppRating);
            System.out.printf("    Average Rating: %.2f\n", cat.m_avgCatRating);
            System.out.printf("    Count: %d\n", cat.m_numRecord);
            System.out.printf("    Discarded: %d\n", discardedRecordsCountPerCategory.getOrDefault(cat.m_catName, 0));
            System.out.println();

            ++count;
        }

        // for (String cat: grouped.keySet()) {
        //     List<PlayStore> apps = grouped.get(cat);
        //     System.out.printf("Category: %s\n", cat);
        //     for (PlayStore app: apps) {
        //         System.out.printf("\t%s, %f\n", app.getM_app(), app.getM_rating());
        //     }

        int size = grouped.values().stream().mapToInt(List::size).sum();
        System.out.printf("Total lines in file: <%d>", size);
        // }
    }

}
