package DataModels;

public class UserModel {

    private int custId;
    String fName;
    String lname;
    String email;
    String password;

    public UserModel(int custId, String fname, String lname, String email, String pword) {
        this.custId = custId;
        this.fName = fname;
        this.lname = lname;
        this.email = email;
        this.password = pword;
    }

    public UserModel() {
    }

    public int getCustId() {
        return custId;
    }

    public String getfName() {
        return fName;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void setCustId(int custId) {
        this.custId = custId;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String pword) {
        this.password= pword;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "custId=" + custId +
                ", fname='" + fName + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
