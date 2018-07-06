package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private Label dateTimeLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Contact> tableView;

    private ContactData contactData;

    @FXML
    private ContextMenu deleteMenu;


    private SortedList<Contact> sortedList;

    private FilteredList<Contact> filteredList;
    @FXML
    private TextField searchPhoneNumber;


    @FXML
    public void initialize() {
        deleteMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");
        deleteMenu.getItems().addAll(delete, edit);

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
                if (contact != null) {
                    editContacts();
                }
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Contact contact = tableView.getSelectionModel().getSelectedItem();
                if (contact != null) {
                    deleteContact();
                }
            }
        });

        contactData = new ContactData();
        contactData.loadContacts();
        tableView.setRowFactory(new Callback<TableView<Contact>, TableRow<Contact>>() {
            @Override
            public TableRow<Contact> call(TableView<Contact> param) {

                TableRow<Contact> contactTableRow = new TableRow<Contact>() {
                };
                contactTableRow.emptyProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        if (newValue) {
                            if (contactTableRow.isEmpty()) tableView.setContextMenu(null);
                        } else tableView.setContextMenu(deleteMenu);
                    }
                });

                return contactTableRow;
            }
        });

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy, hh:mm:ss a");
            Calendar cal = Calendar.getInstance();
            dateTimeLabel.setText(dateFormat.format(cal.getTime()));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        filteredList = new FilteredList<>(contactData.getContacts(), new Predicate<Contact>() {
            @Override
            public boolean test(Contact contact) {
                return true;
            }
        });
        sortedList = new SortedList<>(filteredList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
            }
        });
        tableView.setItems(sortedList);
        tableView.getSelectionModel().selectFirst();

    }

    @FXML
    public void searchIt(KeyEvent event) {
        String getText = searchPhoneNumber.getText().trim();
        if (event.getCode().equals(KeyCode.ENTER)) {
            sortPhoneNumber(sortedList, getText);
        }
    }

    @FXML
    public String sortPhoneNumber(SortedList<Contact> arr, String phoneNumber) {
        for (int i = 0; i < sortedList.size(); i++) {
            for (int j = 0; j < sortedList.size() - 1; j++) {
                if (sortedList.get(i).getPhoneNumber().compareToIgnoreCase(sortedList.get(j).getPhoneNumber()) < 0) {
                    Contact temp = sortedList.get(i);
                    this.sortedList.set(i, sortedList.get(j));
                    this.sortedList.set(j, temp);
                }
            }
        }

        int low = 0;
        int high = arr.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int compareMiddle = arr.get(mid).getPhoneNumber().compareToIgnoreCase(phoneNumber);
            if (compareMiddle > 0) {
                high = mid - 1;

            } else if (compareMiddle < 0) {
                low = mid + 1;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Found phone number : " + phoneNumber);
                alert.setContentText("Name " + sortedList.get(mid).getFirstName() + " " + sortedList.get(mid).getLastName() + "\n" + "Phone number : " + sortedList.get(mid).getPhoneNumber() + "\n" + "Address: " + sortedList.get(mid).getaddresses());
                alert.showAndWait();
                return phoneNumber;
            }
        }
        return "No such name here.";
    }


//        String getText = searchPhoneNumber.getText().trim();
//        for (int i = 0; i < sortedList.size(); i++) {
//            if (getText.equalsIgnoreCase(sortedList.get(i).getPhoneNumber())) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Found phone number : " + getText);
//                alert.setContentText("Name " + sortedList.get(i).getFirstName() + " " + sortedList.get(i).getLastName() + "\n" + "Phone number : " + sortedList.get(i).getPhoneNumber() + "\n" + "Address: " + sortedList.get(i).getaddresses());
//                alert.showAndWait();
//                return getText;
//            }
//        }


    @FXML
    public void editContacts() {
        Contact contact = tableView.getSelectionModel().getSelectedItem();
        if (contact != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Editing items : " + contact.getFirstName() + " " + contact.getLastName());
            dialog.initOwner(borderPane.getScene().getWindow());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("AddNewContacts.fxml"));
            try {
                dialog.setDialogPane(fxmlLoader.load());
            } catch (Exception e) {
                System.out.println("e.getMessage() " + e.getMessage());
            }
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            AddnewContactsController contactsController = fxmlLoader.getController();
            contactsController.editContacts(contact);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                contactsController.updateContacts(contact);
                contactData.saveContacts();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No table to edit");
            alert.setContentText("Click ok to return to the contact box");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteContact() {
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        if (selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected");
            alert.setHeaderText(null);
            alert.setContentText("PLease select the contact you want to delete.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected contact: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactData.deleteContact(selectedContact);
            contactData.saveContacts();
        }
    }


    @FXML
    public void showDialogPane() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(borderPane.getScene().getWindow());
        dialog.setTitle("Add a new contacts here.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddNewContacts.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e.getMessage() " + e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> getOk = dialog.showAndWait();
        if (getOk.isPresent() && getOk.get() == ButtonType.OK) {
            ObservableList<Contact> list = sortedList;
            AddnewContactsController addnewContactsController = fxmlLoader.getController();
            Contact contact = addnewContactsController.addInTheFileSystem();
            /* for (int i = 0; i < sortedList.size(); i++) {*/
                /*if (sortedList.get(i).getPhoneNumber().compareToIgnoreCase(contact.getPhoneNumber()) == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Same phone number " + contact.getPhoneNumber());
                    alert.setContentText("Click ok to return.");
                    alert.showAndWait();
                    return;
                } else if (list.isEmpty()) {
                    contactData.addContact(contact);
                } else {}*/
            if (contact != null) {
                contactData.addContact(contact);
                contactData.saveContacts();
                System.out.println("Ok Pressed");
            }
        }
        System.out.println("Cancel button pressed.");
    }

    @FXML
    public void deleteSelectedItems(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DELETE)) {
            deleteContact();
        }
    }

    @FXML
    public void getLink() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com"));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error while loading your default browser.");
            Optional<ButtonType> getAns = alert.showAndWait();
            if (getAns.isPresent() && getAns.get() == ButtonType.YES) {
                alert.showAndWait();
            }
        }
    }
}

