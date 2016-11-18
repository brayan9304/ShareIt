package co.edu.udea.compumovil.gr06.shareit.UI.model;

/**
 * Created by brayan on 8/10/16.
 */

public class Product {
    //Table name
    public static String CHILD = "products";
    //Columns
    public static String COLUMN_ID = "product_id";
    public static String COLUMN_USER_ID = "userId";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_TYPE = "type";
    public static String COLUMN_PRICE = "price";
    public static String COLUMN_DESCRIPTION = "description";

    private String product_type;
    private double calification;
    private String email;
    private String productName;
    private String pathPoto;
    private String description;
    private String nameUser;
    private int price;
    private byte[] productPicture;


    public Product() {
    }

    public byte[] getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(byte[] productPicture) {
        this.productPicture = productPicture;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public double getCalification() {
        return calification;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCalification(double calification) {
        this.calification = calification;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathPoto() {
        return pathPoto;
    }

    public void setPathPoto(String pathPoto) {
        this.pathPoto = pathPoto;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
