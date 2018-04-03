package sample;

import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty firstName = new SimpleStringProperty("");
    private SimpleStringProperty lastName = new SimpleStringProperty("");
    private SimpleStringProperty phoneNumber = new SimpleStringProperty("");
    private SimpleStringProperty addresses = new SimpleStringProperty("");

    public Contact() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Contact)) {
            return false;
        }
        Contact persons = (Contact) obj;

        // persons.id.equals() leads to the default implementation in Object
        // --> instead use this one.
        // The Property classes have their own isEqualTo method
        // with get(), you will get your simple boolean from the returned BooleanBinding
        return persons.firstName.isEqualTo(firstName).get() &&
                persons.lastName.isEqualTo(lastName).get() &&
                persons.phoneNumber.isEqualTo(phoneNumber).get() &&
                persons.addresses.isEqualTo(addresses).get();
    }

    public Contact(String firstName, String lastName, String phoneNumber, String addresses) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.phoneNumber.set(phoneNumber);
        this.addresses.set(addresses);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getaddresses() {
        return addresses.get();
    }

    public SimpleStringProperty addressesProperty() {
        return addresses;
    }

    public void setaddresses(String addresses) {
        this.addresses.set(addresses);
    }
    
    
}
