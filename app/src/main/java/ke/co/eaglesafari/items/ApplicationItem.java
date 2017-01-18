package ke.co.eaglesafari.items;


public class ApplicationItem {

  String   first_name,last_name,email,telephone,type;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEmpty()
    {
        if(first_name.isEmpty())
            return true;
        if(last_name.isEmpty())
            return true;
        if(email.isEmpty())
            return true;
        if(telephone.isEmpty())
            return true;
        return type.isEmpty();

    }


}
