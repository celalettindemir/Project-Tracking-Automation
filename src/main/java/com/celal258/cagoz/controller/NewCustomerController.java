package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import com.celal258.cagoz.utils.AlertHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


@Component
public class NewCustomerController implements Initializable {

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Long> id;

    @FXML
    private TableColumn<Customer, String> firstName;

    @FXML
    private TableColumn<Customer, String> number;

    @FXML
    private TableColumn<Customer, String> company;

    @FXML
    private TableColumn<Customer, Integer> debt;

    @FXML
    private TableColumn<Customer, String> description;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField numberTextField;

    @FXML
    private TextField debtTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label numberWarningLabel;

    @FXML
    private Label customerNameWarningLabel;

    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StageManager stageManager;

    private User user;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerList.clear();
        for (Object[] customer: customerRepository.findAllCustomer()){
            for(int i=0;i<=5;i++)
                if(customer[i]==null)customer[i]="";
            customerList.addAll(new Customer(Long.parseLong(customer[0].toString()),customer[1].toString(),customer[2].toString(),customer[3].toString(),customer[5].toString(),Integer.parseInt(customer[4].toString())));
        }

        //uppercase
        searchTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            searchTextField.setText(searchTextField.getText().toUpperCase());
        });

        debtTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            debtTextField.setText(debtTextField.getText().toUpperCase());
        });

        descriptionTextArea.textProperty().addListener((observableValue, eventHandler, t1) -> {
            descriptionTextArea.setText(descriptionTextArea.getText().toUpperCase());
        });

        customerNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            customerNameTextField.setText(customerNameTextField.getText().toUpperCase());
        });


        setColumnProperties();
        selectRowData();

        addTextLimiter(numberTextField,11);
        addTextLimiterOnlyNumber(numberTextField);
        addTextLimiterOnlyWord(customerNameTextField,0);
    }

    public void initUser(Long id){
        user = userRepository.findById(id).get();

    }

    private void setColumnProperties(){
        tableView.setEditable(true);
        //Project project = new Project();

        id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        firstName.setCellValueFactory(cellData-> new SimpleObjectProperty<>(cellData.getValue().getFirstName()));
        number.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNumber()));
        company.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCompany()));
        debt.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDebt()));
        description.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCustomerDescription()));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Customer> filteredData = new FilteredList<>(customerList, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getFirstName()!=null&&customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (customer.getNumber()!=null&&customer.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }else if (customer.getCompany()!=null&&customer.getCompany().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }/*else if (customer.getDebt()!=null&&customer.getDebt().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }*/else if (customer.getCustomerDescription()!=null&&customer.getCustomerDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);
    }

    private void selectRowData(){
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                onEdit();
            }
        });
    }

    private void onEdit(){
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
            customerNameTextField.setText(selectedCustomer.getFirstName());
            numberTextField.setText(selectedCustomer.getNumber());
            //debtTextField.setText(selectedCustomer.getDebt());
            descriptionTextArea.setText(selectedCustomer.getCustomerDescription());
        }
    }

    private void setCustomer(Customer customer, User user){
        customer.setFirstName(customerNameTextField.getText());
        customer.setNumber(numberTextField.getText());
        //customer.setDebt(debtTextField.getText());
        customer.setCustomerDescription(descriptionTextArea.getText());
        customer.setUser(user);
    }
    public void clear()
    {
        customerNameTextField.clear();
        numberTextField.clear();
        debtTextField.clear();
        descriptionTextArea.clear();
        numberWarningLabel.setText("");
        customerNameWarningLabel.setText("");
    }


    private void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    private void addTextLimiterOnlyNumber(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }



            }
        });
    }


    private void addTextLimiterOnlyWord(final TextField tf, int i) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if(  !tf.getText().isEmpty() && tf.getText().substring(tf.getText().length() - 1).matches("[0-9]") ){
                    tf.setText( tf.getText().substring(0, oldValue.length()) );
                }

              /*  if(!tf.getText().matches("\\[a-zA-Z]")){
                    System.out.println(tf.getText());
                    System.out.println(oldValue);
                   // tf.setText(oldValue);
                }*/
            }
        });
    }




    @FXML
    public void deleteButtonAction(){
        boolean control = false;
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomerItem = tableView.getSelectionModel().getSelectedItem();
            //System.out.println("selectedCustomer.getProjects()");
            //System.out.println(selectedCustomer.getId());
            Customer selectedCustomer = customerRepository.findById(selectedCustomerItem.getId()).get();
            List<Project> customerProjects=projectRepository.findProjectByCustomerId(selectedCustomer.getId());
            String headerMessage;
            if(customerProjects.size()>0)
                 headerMessage = "Bu Müşteriye Ait "+customerProjects.size() +" Proje Bulunmaktadır !";
            else
                headerMessage = "Bu Müşteriye Ait Proje Bulunmamaktadır !";


            ButtonType apply = new ButtonType("Sil",ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("İptal",ButtonBar.ButtonData.CANCEL_CLOSE);
            AlertHelper.showAlert(Alert.AlertType.CONFIRMATION,"Müşteri Silme",headerMessage,"Müşteriyi silmek istediğinizden emin misiniz?",
                    apply,cancel);
            if(AlertHelper.getAlert().getResult() == apply){
                try{
                    if(customerProjects.size()>0)
                    {
                        customerProjects.forEach(p->{
                            Logging l1 = new Logging();
                            try {
                                setLogging(l1,"adlı müşteri silinirken silindi",selectedCustomer.getFirstName(),p.getProjectName());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            loggingRepository.save(l1);

                        });
                        projectRepository.deleteAll(customerProjects);
                    }


                    customerList.remove(selectedCustomerItem);
                    tableView.getSelectionModel().select(null);

                    Logging l1 = new Logging();
                    setLogging(l1,"adlı müşteri silindi",selectedCustomer.getFirstName());
                    loggingRepository.save(l1);

                    customerRepository.delete(selectedCustomer);
                    tableView.refresh();





                    clear();

                }catch (DataIntegrityViolationException | ParseException e){

                    //projectRepository.findProjects().
                    AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","İlgili müşteriye ait proje tanımlı!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
                    control = true;
                }
            }


        }
    }
    private void setLogging(Logging logging, String eventType,String loggingText) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);


        String tmpText = loggingText ;
        logging.setLoggingText(tmpText);
        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(this.user.getUsername());
    }
    private void setLogging(Logging logging, String eventType,String loggingText,String projectName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);


        String tmpText = projectName+" adlı proje "+loggingText + " ";
        logging.setLoggingText(tmpText);
        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(this.user.getUsername());
    }


    @FXML
    public void addButtonAction() throws ParseException {

        if(numberTextField.getLength()==11 && !(customerNameTextField.getText().equals(""))){

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateString = format.format( new Date()   );
            Date   date       = format.parse ( dateString);

            Customer customer = new Customer();
            setCustomer(customer, user);
            customerRepository.save(customer);
            customerList.addAll(customer);

            Logging l1 = new Logging();
            setLogging(l1,"adlı bir müşteri eklendi",customer.getFirstName());
            loggingRepository.save(l1);
            clear();
        }
        else
        {
            //AlertHelper.showAlert(Alert.AlertType.WARNING,"Eksik Bilgi","","11 haneli bir numara giriniz!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);

            if(numberTextField.getLength()!=11)
                numberWarningLabel.setText("11 haneli bir numara giriniz");
            else
                numberWarningLabel.setText("");
            if(customerNameTextField.getText().equals(""))
                customerNameWarningLabel.setText("Müşteri adı giriniz");
            else
                customerNameWarningLabel.setText("");
            return;
        }


        clear();
    }

    @FXML
    public void updateButtonAction() throws ParseException {
        if (tableView.getSelectionModel().getSelectedItem() != null && (numberTextField.getLength()==11 && !(customerNameTextField.getText().equals("")))) {
            Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
            setCustomer(selectedCustomer, user);
            customerRepository.save(selectedCustomer);
            tableView.refresh();


            Logging l1 = new Logging();
            setLogging(l1,"adlı müşterinin bilgileri güncellendi",selectedCustomer.getFirstName());
            loggingRepository.save(l1);

            clear();
            tableView.getSelectionModel().select(null);
        }
        else {

            if(numberTextField.getLength()!=11)
                numberWarningLabel.setText("11 haneli bir numara giriniz");
            else
                numberWarningLabel.setText("");
            if(customerNameTextField.getText().equals(""))
                customerNameWarningLabel.setText("Müşteri adı giriniz");
            else
                customerNameWarningLabel.setText("");
            return;

        }

    }
    /*@FXML
    public void textFieldChange(){
        tableView.getSelectionModel().select(null);

    }
*/

    @FXML
    public void changeSearchTextField(){
        //String aranan=searchTextField.getText();
    }
    @FXML
    public void backButtonAction()
    {
        clear();
        stageManager.switchScene("/home.fxml","home");
    }
}
