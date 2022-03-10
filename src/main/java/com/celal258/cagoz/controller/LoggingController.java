package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import com.celal258.cagoz.service.HomeService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class LoggingController implements Initializable {

    @FXML
    private TableView<Logging> tableView;

    @FXML
    private TableColumn<Logging, String> userName;

    @FXML
    private TableColumn<Logging, String> loggingText;

    @FXML
    private TableColumn<Logging, String> loggingDate;

    @FXML
    private TableColumn<Logging, Long> projectId;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField searchProjectIdTextInput;

    @FXML
    private ComboBox comboBox;

    @FXML
    private DatePicker datePickerGoing;

    @FXML
    private DatePicker datePickerReturn;


    @Autowired
    private StageManager stageManager;

    @Autowired
    private HomeService homeService;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;

    @Autowired
    private UserRepository userRepository;

    private Boolean isChanged ;
    private Boolean isChanged2 ;
    private Boolean isChanged3 ;
    private Boolean isChanged4 ;
    private Boolean isChanged5;


    private ObservableList<Logging> loggingList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loggingList.clear();
        createComboBoxData();

        for (Logging forLogging: loggingRepository.findAll()){
            Logging tempLogging = new Logging();

            tempLogging.setProjectId(forLogging.getProjectId());
            tempLogging.setLoggingDate(forLogging.getLoggingDate());
            tempLogging.setUserName(forLogging.getUserName());
            String tempLoggingText = forLogging.getLoggingText()+" "+forLogging.getLoggingEventType().getEventType();

            tempLogging.setLoggingText(tempLoggingText);
            tempLogging.setLoggingEventType(forLogging.getLoggingEventType());



            loggingList.add(tempLogging);

        }
        FXCollections.reverse(loggingList);
        setColumnProperties();


    }


   private void filterTheData(ObservableValue observable,Object oldValue,Object newValue2 , Boolean t1 ){

        isChanged= true;
        isChanged2= true;
        isChanged3= true;
        isChanged4= true;
        isChanged5=true;



        filteredData.setPredicate(logging -> {

            if(!searchTextField.getText().equals("") && searchTextField.getText()!=null){
                System.out.println("Searchİnput if ");
                isChanged = true;
                String newValue = searchTextField.getText();
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    isChanged = true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();


                //System.out.println("Durum ..." + project.getPaper().getOperationType().toLowerCase()) ;
                //System.out.println(" Aranan ..." + lowerCaseFilter) ;
                if (logging.getLoggingText()!=null&&logging.getLoggingText().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged = true; // Filter matches last name.
                }
                else
                    isChanged = false; // Does not match.
            }

            if(!searchProjectIdTextInput.getText().equals("") && searchProjectIdTextInput.getText()!=null){
                System.out.println("Project Id if ");
                isChanged5 = true;
                Long newValue =Long.parseLong(searchProjectIdTextInput.getText());


                // If filter text is empty, display all persons.
                if (searchProjectIdTextInput.getText() == null || searchProjectIdTextInput.getText().isEmpty()) {
                    isChanged5 = true;
                }
                // Compare first name and last name of every person with filter text.
                //String lowerCaseFilter = newValue.toLowerCase();


                //System.out.println("Durum ..." + project.getPaper().getOperationType().toLowerCase()) ;
                //System.out.println(" Aranan ..." + lowerCaseFilter) ;
                if (logging.getProjectId()!=null&&logging.getProjectId().equals(newValue)) {
                    isChanged5 = true; // Filter matches last name.
                }
                else
                    isChanged5 = false; // Does not match.
            }




            if (comboBox.getSelectionModel().getSelectedItem()!=null){
                System.out.println("combobox if ");
                System.out.println(comboBox.getSelectionModel().getSelectedIndex());

                String newValue = (String) comboBox.getSelectionModel().getSelectedItem();

                String approvalStatus=logging.getUserName();
                // If filter text is empty, display all persons.
                if (newValue == null || (newValue).isEmpty()) {
                    isChanged2 = true;
                }
                if (approvalStatus == null || (approvalStatus).isEmpty()) {
                    isChanged2 = false;
                    return false;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = (newValue).toLowerCase();

                if (approvalStatus.toLowerCase().contains(lowerCaseFilter)) {
                    isChanged2 = true; // Filter matches first name.
                }
                else
                    isChanged2 = false; // Does not match.

            }
            if (!datePickerGoing.getEditor().getText().equals(null)){
                System.out.println("pickerGoing if ");
                System.out.println(datePickerGoing.getValue());



                LocalDate newValue = (LocalDate) datePickerGoing.getValue();
                LocalDate date=logging.getLoggingDate()!=null ?logging.getLoggingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate():LocalDate.MIN;

                if (newValue == null || newValue==LocalDate.MIN) {
                    isChanged3 = true;
                }
                if(newValue!=null){
                    if (date.compareTo(newValue)>=0) {
                        isChanged3 = true;
                    }
                    else
                        isChanged3 = false;
                }

            }

            if (!datePickerReturn.getEditor().getText().equals(null)){
                System.out.println("pickerReturn if ");
                System.out.println(datePickerReturn.getValue());


                LocalDate newValue = (LocalDate) datePickerReturn.getValue();;
                LocalDate date=logging.getLoggingDate()!=null ?logging.getLoggingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate():LocalDate.MIN;

                if (newValue == null || newValue==LocalDate.MIN) {
                    isChanged4 = true;
                }

                if(newValue!=null){
                    if (date.compareTo(newValue)<=0) {
                        isChanged4 = true;
                    }
                    else
                        isChanged4 = false;
                }

            }

            if(isChanged&&isChanged2&&isChanged3&&isChanged4&&isChanged5)
                return true;
            else
                return false;
        });
    }




    FilteredList<Logging> filteredData = new FilteredList<>(loggingList, p -> true);

    private void setColumnProperties(){
        tableView.setEditable(true);
        //Project project = new Project();

        userName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUserName()));
        loggingText.setCellValueFactory(cellData-> new SimpleObjectProperty<>(cellData.getValue().getLoggingText()));
        loggingDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(convertStringToDate(cellData.getValue().getLoggingDate())));
        projectId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProjectId()));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).

        // 2. Set the filter Predicate whenever the filter changes.
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTheData(observable,null,null , null);
        });

        searchProjectIdTextInput.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTheData(observable,null,null , null);
        });

        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            filterTheData(options ,null ,(String) null , null);

        });

        datePickerGoing.valueProperty().addListener((options, oldValue, newValue) -> {

            filterTheData(options,oldValue,newValue,null);
        });
        datePickerReturn.valueProperty().addListener((options, oldValue, newValue) -> {

            filterTheData(options,null,null,null);


        });




        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Logging> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);
    }

    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        /*you can also use DateFormat reference instead of SimpleDateFormat
         * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
         */
        try{
            dateString = formatter.format( indate );
        }catch (Exception ex ){
            // System.out.println(ex);
        }
        return dateString;
    }



    public void filteredButtonAction(ActionEvent event){

        System.out.println(searchTextField.getText()+"\n"+comboBox.getValue());
        System.out.println(datePickerGoing.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println(datePickerReturn.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }



    private void createComboBoxData(){
        String [] usersDb;
        for (User forUser: userRepository.findAll()){

            comboBox.getItems().add(forUser.getUsername());

        }


    }





    LocalDate returnDate,goingDate;
    @FXML
    public void datePickerReturnText() throws IOException {
        if(returnDate!=datePickerReturn.getValue())
            returnDate=datePickerReturn.getValue();
        else
            datePickerReturn.setValue(null);
    }

    @FXML
    public void filteredClearButtonAction(){
        if(!searchTextField.getText().equals(""))
            searchTextField.clear();

        if(!searchProjectIdTextInput.getText().equals(""))
            searchProjectIdTextInput.clear();


        if(!(comboBox.getSelectionModel().getSelectedIndex()==2))
            comboBox.getSelectionModel().select(-1);

        if(!datePickerGoing.getEditor().getText().equals("")) {
            datePickerGoing.getEditor().clear();
            datePickerGoing.setValue(null);
            filteredData.setPredicate(x->true);

        }
        if(!datePickerReturn.getEditor().getText().equals("")){
            datePickerReturn.getEditor().clear();
            datePickerReturn.setValue(null);
            filteredData.setPredicate(x->true);
        }

    }

    @FXML
    public void datePickerGoingText() throws IOException {
        if(goingDate!=datePickerGoing.getValue())
            goingDate=datePickerGoing.getValue();
        else
            datePickerGoing.setValue(null);
    }

    @FXML
    public void backButtonAction()
    {

        stageManager.switchScene("/home.fxml","home");
    }
}

