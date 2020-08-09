package fyp.water_delivery_driver.Model;

public class cartItems {
    String name,productid,weight,image;
    int quantity,price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public cartItems(String name, String productid, String weight, String image, int quantity, int price) {
        this.name = name;
        this.productid = productid;
        this.weight = weight;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }
    public cartItems(){

    }
}
