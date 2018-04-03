package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AddnewContactsController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField addressField;


    @FXML
    public Contact addInTheFileSystem() {
        String firstNameText = firstNameField.getText().trim();
        String lastNameText = lastNameField.getText().trim();
        String phoneNumberText = phoneNumberField.getText().trim();
        String text = addressField.getText().trim();
        if (firstNameText.length() == 0 && lastNameText.length() == 0 && phoneNumberText.length() == 0 && text.length() == 0) {
            Contact contact = new Contact(firstNameText, lastNameText, phoneNumberText, text);
            return contact;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No table to edit");
            alert.setContentText("Click ok to return to the contact box");
            alert.showAndWait();
            return null;
        }
    }


    @FXML
    public void editContacts(Contact contact) {
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        addressField.setText(contact.getaddresses());
    }

    @FXML
    public void updateContacts(Contact contact) {
        contact.setFirstName(firstNameField.getText());
        contact.setLastName(lastNameField.getText());
        contact.setPhoneNumber(phoneNumberField.getText());
        contact.setaddresses(addressField.getText());
    }


}
