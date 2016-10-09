package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class Product {
    //Table name
    public static String TABLE_NAME = "Product";
    //Columns
    public static String COLUMN_ID = "product_id";
    public static String COLUMN_USER_ID = "userId";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_TYPE = "type";
    public static String COLUMN_PRICE = "price";
    public static String COLUMN_DESCRIPTION = "description";

    private int product_id;
    private int userId;
    private String name;
    private String type;
    private int price;
    private String description;

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return description;
    }

    public void setDescripcion(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
