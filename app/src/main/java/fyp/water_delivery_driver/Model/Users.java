package fyp.water_delivery_driver.Model;

public class Users {
    String Name;
    String Email;
    String Password;
    String Phone;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    String cnic;
    String vehicleNo;

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public Users(){

   }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Users(String name, String email, String password, String phone,String cnic,String vehicleNo) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        this.cnic = cnic;
        this.vehicleNo=vehicleNo;
    }
}
