package clientFiles;

public class Item {
    
    int  m_prod_Id;
    String m_title;
    float m_price;
    float m_rating;

    public Item() {

    }

    public Item(int _id, String _title, float _price, float _rating) {
        m_prod_Id = 0;
        m_title = "";
        m_price = 0;
        m_rating = 0;
    }

    public int getprod_Id() {return m_prod_Id;}

    public String getTitle() {return m_title;}

    public float getPrice() {return m_price;}

    public float getRating() {return m_rating;}

}
