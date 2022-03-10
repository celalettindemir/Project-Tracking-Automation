package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.ProjectRepository;
import com.celal258.cagoz.repository.RemindRepository;
import com.celal258.cagoz.repository.UserRepository;
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
import java.io.BufferedInputStream;
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
public class HomeController implements Initializable {

    @FXML
    private TableView<HomeTableView> tableView;

    @FXML
    private TableColumn<HomeTableView, Long> id;

    @FXML
    private TableColumn<HomeTableView, String> projectName;

    @FXML
    private TableColumn<HomeTableView, String> firstName;

    @FXML
    private TableColumn<HomeTableView, String> projectType;

    @FXML
    private TableColumn<HomeTableView, String> operationType;

    @FXML
    private TableColumn<HomeTableView, String> dateOfGoing;

    @FXML
    private TableColumn<HomeTableView, String> dateOfReturn;

    @FXML
    private TableColumn<HomeTableView, String> approvalStatus;

    @FXML
    private TableColumn<HomeTableView, String> projectDescription;

    @FXML
    private TableColumn<HomeTableView, String> projectOrder;

    @FXML
    private TableColumn<HomeTableView, String> projectRemind;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField searchProjectNameTextField;

    @FXML
    private TextField searchCustomerNameTextField;

    @FXML
    private ComboBox comboBox;

    @FXML
    private DatePicker datePickerGoing;

    @FXML
    private DatePicker datePickerReturn;

    @FXML
    private CheckBox hatirlatmaCheckBox;

    @FXML
    private Button loggingButton;


    @Autowired
    private StageManager stageManager;
    @Autowired
    private HomeService homeService;

    @Autowired
    private UserRepository userRepository;

    private Boolean isChanged ;
    private Boolean isChanged2 ;
    private Boolean isChanged3 ;
    private Boolean isChanged4 ;
    private Boolean isChanged5 ;
    private Boolean isChanged6 ;
    private Boolean isChanged7 ;

    private User user;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableList.clear();
        createComboBoxData();
        homeService.getDbData(tableList);


        setColumnProperties();
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount()==2&&event.getButton().equals(MouseButton.PRIMARY)) {
                onDoubleClickTableView();
            }
        });
        searchTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            searchTextField.setText(searchTextField.getText().toUpperCase());
        });
        searchProjectNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            searchProjectNameTextField.setText(searchProjectNameTextField.getText().toUpperCase());
        });
        searchCustomerNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            searchCustomerNameTextField.setText(searchCustomerNameTextField.getText().toUpperCase());
        });

        FXCollections.reverse(tableList);
    }

    public void initUser(Long id){
        user = userRepository.findById(id).get();
        user.getRoles().forEach(p->
        {
            System.out.println(p.getName());
            if(!p.getName().equals("ROLE_USER"))
            {
                loggingButton.setVisible(true);
            }
        });
    }

    public void onDoubleClickTableView(){
        if(tableView.getSelectionModel().getSelectedItem() != null){
            stageManager.switchScene("/project_detail.fxml","Project Detail");
            ProjectDetailController projectDetailController=stageManager.getLoader().getController();
            HomeTableView selectedCustomer = tableView.getSelectionModel().getSelectedItem();
            projectDetailController.initUserId(user);
            projectDetailController.initData(selectedCustomer.getProject().getId());

            projectDetailController.setDisableAll(selectedCustomer.getProject().getProjectType());

        }
    }
    private void createComboBoxData(){
        comboBox.getItems().addAll(
                "Onaylandı",
                "Onaylanmadı",
                ""
        );
    }

    DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
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
    private ObservableList<HomeTableView> tableList = FXCollections.observableArrayList();
    FilteredList<HomeTableView> filteredData = new FilteredList<>(tableList, p -> true);

    private void filterTheData(ObservableValue observable,Object oldValue,Object newValue2 , Boolean t1 ){

        isChanged= true;
        isChanged2= true;
        isChanged3= true;
        isChanged4= true;
        isChanged5= true;
        isChanged6= true;
        isChanged7= true;


        filteredData.setPredicate(project -> {

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
                if (project.getPaper().getOperationType()!=null&&project.getPaper().getOperationType().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged = true; // Filter matches last name.
                }
                else if (project.getProject().getProjectDescription()!=null&&project.getProject().getProjectDescription().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged = true; // Filter matches last name.
                }
                else if (project.getPaper().getRecordNumber()!=null&&project.getPaper().getRecordNumber().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged = true; // Filter matches last name.
                }
                else
                    isChanged = false; // Does not match.
            }

            if(!searchProjectNameTextField.getText().equals("") && searchProjectNameTextField.getText()!=null){
                System.out.println("searchProjectNameTextField if ");
                isChanged6 = true;
                String newValue = searchProjectNameTextField.getText();
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    isChanged6 = true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (project.getProject().getProjectName().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged6 = true; // Filter matches first name.
                }
                else
                    isChanged6 = false; // Does not match.
            }
            if(!searchCustomerNameTextField.getText().equals("") && searchCustomerNameTextField.getText()!=null){
                System.out.println("searchCustomerNameTextField if ");
                isChanged7 = true;
                String newValue = searchCustomerNameTextField.getText();
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    isChanged7 = true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (project.getCustomer().getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    isChanged7 = true; // Filter matches last name.
                }
                else
                    isChanged7 = false; // Does not match.
            }

            if (comboBox.getSelectionModel().getSelectedItem()!=null){
                System.out.println("combobox if ");
                System.out.println(comboBox.getSelectionModel().getSelectedIndex());
                String newValue = (String) comboBox.getSelectionModel().getSelectedItem();

                String approvalStatus=project.getPaper().getApprovalStatus();
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
                LocalDate date=project.getPaper().getDateOfGoing()!=null ?project.getPaper().getDateOfGoing().toInstant().atZone(ZoneId.systemDefault()).toLocalDate():LocalDate.MIN;

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
                LocalDate date=project.getPaper().getDateOfReturn()!=null ?project.getPaper().getDateOfReturn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate():LocalDate.MAX;

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
            if (hatirlatmaCheckBox.isSelected()){
                System.out.println("checkBox if ");

                Boolean t2 = hatirlatmaCheckBox.isSelected();
                if(t2&&project.getProject().getProjectRemind().equals("Var"))
                    isChanged5 = true;
                else if(!t2)
                    isChanged5 = true;
                else
                    isChanged5 = false;
            }
            if(isChanged&&isChanged2&&isChanged3&&isChanged4&&isChanged5&&isChanged6&&isChanged7)
                return true;
            else
                return false;
        });
    }


    private void setColumnProperties(){
        tableView.setEditable(true);

        id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getId()));
        projectName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjectName()));
        firstName.setCellValueFactory(cellData-> new SimpleObjectProperty<>(cellData.getValue().getCustomer().getFirstName()));
        projectType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjectType()));
        dateOfGoing.setCellValueFactory(cellData -> new SimpleObjectProperty<>(convertStringToDate(cellData.getValue().getPaper().getDateOfGoing())));
        dateOfReturn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(convertStringToDate(cellData.getValue().getPaper().getDateOfReturn())));
        approvalStatus.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPaper().getApprovalStatus()));
        operationType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPaper().getOperationType()));
        projectDescription.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjectDescription()));
        projectOrder.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjectOrder()));
        projectRemind.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProject().getProjectRemind()));

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        // 2. Set the filter Predicate whenever the filter changes.

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTheData(observable,null,null , null);
        });

        searchProjectNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTheData(observable,null,null , null);
        });

        searchCustomerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTheData(observable,null,null , null);
        });

        comboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

           filterTheData(options ,null ,(String) null , null);

        });

        hatirlatmaCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            filterTheData(observableValue ,null ,null , t1);
        });

        datePickerGoing.valueProperty().addListener((options, oldValue, newValue) -> {

            filterTheData(options,oldValue,newValue,null);
        });
        datePickerReturn.valueProperty().addListener((options, oldValue, newValue) -> {

            filterTheData(options,null,null,null);


        });


        // 3. Wrap the FilteredList in a SortedList.
        SortedList<HomeTableView> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);
    }


    public void filteredButtonAction(ActionEvent event){

        System.out.println(searchTextField.getText()+"\n"+comboBox.getValue());
        System.out.println(datePickerGoing.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println(datePickerReturn.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }


    @FXML
    public void newProjectButtonAction(ActionEvent event) throws IOException {
        stageManager.switchScene("/new_project.fxml","Proje");
        //yonlendirme("new_project.fxml", event);
    }

    @FXML
    public void loggingButtonAction(ActionEvent event) throws IOException {
        stageManager.switchScene("/logging.fxml","Hareket Listesi");
        /*System.out.println("=================================");
        System.out.println("=================================");
        System.out.println("=================================");
        System.out.println("Logging butonu çalıştı");
        System.out.println("=================================");
        System.out.println("=================================");
        System.out.println("=================================");
        //yonlendirme("new_project.fxml", event);*/
    }

    @FXML
    public void newCustomerButtonAction(ActionEvent event) throws IOException {
        stageManager.switchScene("/new_customer.fxml","Müşteri Listesi");
        NewCustomerController newCustomerController = stageManager.getLoader().getController();
        newCustomerController.initUser(user.getId());
    }


    private void addHometablewiewToArray(Map<String, Object[]> data,int i,HomeTableView homeTableView)
    {
        data.put(String.valueOf(i+1),new Object[]{

                        homeTableView.getProject().getProjectName(),
                        homeTableView.getCustomer().getFirstName(),
                        homeTableView.getProject().getProjectType(),
                        homeTableView.getPaper().getDateOfGoing() != null ? homeTableView.getPaper().getDateOfGoing().toString() : " ",
                        homeTableView.getPaper().getDateOfReturn() != null ? homeTableView.getPaper().getDateOfReturn().toString() : " ",
                        homeTableView.getPaper().getApprovalStatus(),
                        homeTableView.getProject().getProjectDescription(),
                        homeTableView.getProject().getProjectRemind()
                }
        );
    }

    private void writeExcelFile(String fileName){

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("test");
        /*XSSFCellStyle backgroundStyle = workbook.createCellStyle();
        backgroundStyle.setAlignment(HorizontalAlignment.LEFT);
        backgroundStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        backgroundStyle.setFillPattern(FillPatternType.BIG_SPOTS);*/
        sheet.setDefaultColumnWidth(20);
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("0",new Object[]{"Proje Adı","Müşteri Adı","Proje Türü","Gidiş Tarihi","Dönüş Tarihi","Onay Durumu","Not","Hatırlatma"});




        for(int i = 0;i < tableList.size();i++){
            HomeTableView homeTableView = tableList.get(i);
            // System.out.println("papers: "+homeTableView.getProject().getPapers());


            if(homeTableView.getProject().getCompleted() == false) {

                addHometablewiewToArray(data,i,homeTableView);

            }
            else
            {
                for( int x=0; x<homeTableView.getProject().getPapers().size(); x++)
                {
                    System.out.println(homeTableView.getProject().getProjectName()+"    "+homeTableView.getProject().getCompleted());

                    if(homeTableView.getProject().getPapers().get(x).getDateOfReturn() == null)
                    {

                        addHometablewiewToArray(data,i,homeTableView);
                        break;
                    }
                }
            }
        }

        Set<String> keySet = data.keySet();
        int rowNum = 0;
        for(String key: keySet){
            Row row = sheet.createRow(rowNum++);
            Object[] objArr = data.get(key);
            int cellNum = 0;
            for(Object obj : objArr){
                Cell cell = row.createCell(cellNum++);
                if(obj instanceof String){
                    cell.setCellValue((String)obj);
                }
                else if(obj instanceof Integer){
                    cell.setCellValue((Integer)obj);
                }
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            System.out.println(fileName+" başarılı şekilde diske yazılmıştır.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void excelExportButtonAction(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Dosyanın kaydedileceği yeri seçiniz");
        fileChooser.setInitialFileName(new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date()) +".xlsx");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            writeExcelFile(file.getPath());
        }
        //writeExcelFile("test.xlsx");
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

        if(!searchProjectNameTextField.getText().equals(""))
            searchProjectNameTextField.clear();

        if(!searchCustomerNameTextField.getText().equals(""))
            searchCustomerNameTextField.clear();

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
        if(hatirlatmaCheckBox.isSelected())
            hatirlatmaCheckBox.setSelected(false);
    }

    @FXML
    public void datePickerGoingText() throws IOException {
        if(goingDate!=datePickerGoing.getValue())
            goingDate=datePickerGoing.getValue();
        else
            datePickerGoing.setValue(null);
    }

}

