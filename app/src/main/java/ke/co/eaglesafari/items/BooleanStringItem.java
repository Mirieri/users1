package ke.co.eaglesafari.items;

public class BooleanStringItem {
    String message;
    Boolean aBoolean;

    public String getMessage() {
        return message;
    }

    public BooleanStringItem setMessage(String message) {
        this.message = message;
        return this;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public BooleanStringItem setaBoolean(Boolean aBoolean) {
        this.aBoolean = aBoolean;
        return  this;
    }
}
