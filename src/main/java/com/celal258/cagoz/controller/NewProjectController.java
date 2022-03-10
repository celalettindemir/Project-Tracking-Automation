package com.celal258.cagoz.controller;

import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import com.celal258.cagoz.utils.AlertHelper;
import com.celal258.cagoz.utils.AutoCompleteComboBoxListener;
import com.celal258.cagoz.utils.FileFtp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import com.celal258.cagoz.config.StageManager;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Component
public class NewProjectController implements Initializable {

    @FXML
    private CheckBox TapuCheckBox;

    @FXML
    private CheckBox RuhsatCheckBox;

    @FXML
    private CheckBox VekaletCheckBox;

    @FXML
    private CheckBox ImarAffiCheckBox;

    @FXML
    private CheckBox BelediyeYazisiCheckBox;

    @FXML
    private CheckBox IlceTarimCheckBox;

    @FXML
    private CheckBox DsiCheckBox;

    @FXML
    private CheckBox MevcutFaturaCheckBox;

    @FXML
    private CheckBox EmlakBeyaniCheckBox;


    @FXML
    public ComboBox projectTypeComboBox;

    @FXML
    private ComboBox projectDrawComboBox;

    @FXML
    private CheckBox remindCheckBox;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    public TextField projectNameTextField;

    @FXML
    public TextField adaParselTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField receivedMoneyTextField;

    @FXML
    private TextArea noteTextField;

    @FXML
    private ListView<String> documentFileListView;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    LoggingEventTypeRepository loggingEventTypeRepository;

    @Autowired
    private RemindRepository remindRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private StageManager stageManager;

    private User user;
    private Project project;
    private Customer customer;
    private List<Document> document;
    private Paper paper;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Remind getRemind() {
        return project.getRemind();
    }

    public boolean IsTapuSelected(){
        return TapuCheckBox.isSelected();
    }

    public boolean IsRuhsatSelected(){
        return RuhsatCheckBox.isSelected();
    }

    public boolean IsVekaletSelected(){
        return VekaletCheckBox.isSelected();
    }

    public boolean IsMevcutFaturaSelected(){
        return MevcutFaturaCheckBox.isSelected();
    }

    public boolean IsDsiSelected(){
        return DsiCheckBox.isSelected();
    }

    public boolean IsImarAffiSelected(){
        return ImarAffiCheckBox.isSelected();
    }

    public boolean IsEmlakBeyaniSelected(){
        return EmlakBeyaniCheckBox.isSelected();
    }
    public boolean IsBelediyeYazisiSelected(){
        return BelediyeYazisiCheckBox.isSelected();
    }

    public boolean IsIlceTarimSelected(){
        return IlceTarimCheckBox.isSelected();
    }

    public void setRemind(Remind remind) {

        if(remind == null){
            project.setProjectRemind("Yok");
            getRemindCheckBox().setSelected(false);
        }
        else{
            this.project.setRemind(remind);
            this.project.getRemind().setProject(this.project);
            getRemindCheckBox().setSelected(true);
            project.setProjectRemind("Var");
        }
    }

    public CheckBox getRemindCheckBox() {
        return remindCheckBox;
    }

    public void setRemindCheckBox(CheckBox remindCheckBox) {
        this.remindCheckBox = remindCheckBox;
    }

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();

    class CustomerConverter extends StringConverter<Customer>
    {
        // Method to convert a Person-Object to a String
        @Override
        public String toString(Customer person)
        {
            //Bunun iin fonksiyon yazılabilir getname diye
            return person == null? null : person.getFirstName();
        }

        // Method to convert a String to a Person-Object
        @Override
        public Customer fromString(String string)
        {
            Customer person = new Customer();
            person.setFirstName(string);
            return person;
        }
    }





    ObservableList<File> langs = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerData.clear();
        user = new User();
        document=new ArrayList<Document>();
        project=new Project();
        project.setDate(new Date());
        for(User u: userRepository.findAll()){
            user.setId(u.getId());
            user.setUsername(u.getUsername());
        }
        customerData.addAll((Collection<? extends Customer>) customerRepository.findAll());
        customerNameComboBox.setConverter(new CustomerConverter());
        customerNameComboBox.getItems().addAll(customerData);
        new AutoCompleteComboBoxListener<Customer>(customerNameComboBox);
        customerNameComboBox.setOnAction(e -> {
            //customerNameComboBox.getItems().clear();
            if(customerNameComboBox.getValue().getId()!=null) {
                customer = customerNameComboBox.getValue();
            }
        });

        projectNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            projectNameTextField.setText(projectNameTextField.getText().toUpperCase());
        });

        noteTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            noteTextField.setText(noteTextField.getText().toUpperCase());
        });

        adaParselTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            adaParselTextField.setText(adaParselTextField.getText().toUpperCase());
        });


        projectTypeComboBox.getItems().addAll(
                "Yeni Abone",
                "Çevre Aydınlatma",
                "Kat İlavesi",
                "Şantiye",
                "Tarımsal Sulama",
                "Güç Düşümü/Artırımı"
        );
        projectTypeComboBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, o, t1) ->
        {

            TapuCheckBox.setDisable(false);
            RuhsatCheckBox.setDisable(false);
            VekaletCheckBox.setDisable(false);
            ImarAffiCheckBox.setDisable(false);
            BelediyeYazisiCheckBox.setDisable(false);
            IlceTarimCheckBox.setDisable(false);
            DsiCheckBox.setDisable(false);
            MevcutFaturaCheckBox.setDisable(false);
            EmlakBeyaniCheckBox.setDisable(false);

            switch (projectTypeComboBox.getSelectionModel().getSelectedIndex())
            {
                case 0:
                    IlceTarimCheckBox.setDisable(true);
                    DsiCheckBox.setDisable(true);
                    MevcutFaturaCheckBox.setDisable(true);
                    break;
                case 1:
                    ImarAffiCheckBox.setDisable(true);
                    BelediyeYazisiCheckBox.setDisable(true);
                    IlceTarimCheckBox.setDisable(true);
                    DsiCheckBox.setDisable(true);
                    EmlakBeyaniCheckBox.setDisable(true);
                    break;
                case 2:
                    IlceTarimCheckBox.setDisable(true);
                    DsiCheckBox.setDisable(true);
                    break;
                case 3:
                    ImarAffiCheckBox.setDisable(true);
                    BelediyeYazisiCheckBox.setDisable(true);
                    IlceTarimCheckBox.setDisable(true);
                    DsiCheckBox.setDisable(true);
                    MevcutFaturaCheckBox.setDisable(true);
                    EmlakBeyaniCheckBox.setDisable(true);
                    break;
                case 4:
                    RuhsatCheckBox.setDisable(true);
                    ImarAffiCheckBox.setDisable(true);
                    BelediyeYazisiCheckBox.setDisable(true);
                    MevcutFaturaCheckBox.setDisable(true);
                    EmlakBeyaniCheckBox.setDisable(true);
                    break;
                case 5:
                    RuhsatCheckBox.setDisable(true);
                    ImarAffiCheckBox.setDisable(true);
                    BelediyeYazisiCheckBox.setDisable(true);
                    IlceTarimCheckBox.setDisable(true);
                    DsiCheckBox.setDisable(true);
                    EmlakBeyaniCheckBox.setDisable(true);
                    break;
            }
        });
        projectTypeComboBox.getSelectionModel();
        projectDrawComboBox.getItems().addAll(
                "Kendi projesi var","Çizilecek","Belli değil"
        );

        addTextLimiterOnlyNumber(priceTextField);
        addTextLimiterOnlyNumber(receivedMoneyTextField);

        addTextLimiter(priceTextField ,8);
        addTextLimiter(receivedMoneyTextField ,8);


        isRecievedMoneyEqualPrice(receivedMoneyTextField);
        isRecievedMoneyMorethanPrice(priceTextField);


        projectTypeComboBox.getSelectionModel().select(0);
        projectDrawComboBox.getSelectionModel().select(0);

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
    private void isRecievedMoneyEqualPrice(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {

                int alinanPara , ucret ;

                try {
                    if(!receivedMoneyTextField.getText().isEmpty()&& !priceTextField.getText().isEmpty())
                    {


                        alinanPara =  Integer.parseInt(receivedMoneyTextField.getText().trim());
                        ucret =  Integer.parseInt(priceTextField.getText().trim());

                        if (alinanPara  > ucret) {
                             receivedMoneyTextField.setText(oldValue);
                        }


                    }

                }
                catch (NumberFormatException nfe)
                {
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }
            }
        });
    }
    private void isRecievedMoneyMorethanPrice(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {

                int alinanPara , ucret ;

                try {
                    if(!receivedMoneyTextField.getText().isEmpty()&& !priceTextField.getText().isEmpty())
                    {

                        alinanPara =  Integer.parseInt(receivedMoneyTextField.getText().trim());
                        ucret =  Integer.parseInt(priceTextField.getText().trim());


                        if (alinanPara  > ucret) {
                            receivedMoneyTextField.setText(priceTextField.getText());
                        }



                    }
                    else if ( priceTextField.getText().isEmpty()){
                        receivedMoneyTextField.setText("");
                    }

                }
                catch (NumberFormatException nfe)
                {
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }
            }
        });
    }


    private void setVariableProject() {
        project.setProjectType(projectTypeComboBox.getSelectionModel().getSelectedItem().toString());
        project.setProjectName(projectNameTextField.getText());
        project.setPrice(Integer.parseInt(priceTextField.getText()));
        project.setReceivedMoney(Integer.parseInt(receivedMoneyTextField.getText()));
        project.setProjectDescription(noteTextField.getText());
        project.setProjectTapu(TapuCheckBox.isSelected());
        project.setProjectRuhsat(RuhsatCheckBox.isSelected());
        project.setProjectVekalet(VekaletCheckBox.isSelected());
        project.setProjectImarAffi(ImarAffiCheckBox.isSelected());
        project.setProjectBelediyeYazisi(BelediyeYazisiCheckBox.isSelected());
        project.setProjectIlceTarim(IlceTarimCheckBox.isSelected());
        project.setProjectDSI(DsiCheckBox.isSelected());
        project.setProjectMevcutFatura(MevcutFaturaCheckBox.isSelected());
        project.setProjectEmlakBeyani(EmlakBeyaniCheckBox.isSelected());
        project.setProjectAda(adaParselTextField.getText());
        String orderResult = Integer.parseInt(priceTextField.getText())-Integer.parseInt(receivedMoneyTextField.getText()) == 0 ? "Ödendi" : "Ödenmedi";
        project.setProjectOrder(orderResult);
        if(project.getRemind()!=null)
            project.setProjectRemind("Var");
        else
            project.setProjectRemind("Yok");
        project.setUser(user);
        project.setCustomer(customer);
    }
    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    public String convertStringToDate(Date indate)
    {
        String dateString = null;
            /*you can also use DateFormat reference instead of SimpleDateFormat
             * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
             */
            try{
                dateString = formatter.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }

    private void setLogging(Logging logging, String eventType,String prjctName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);



        logging.setLoggingText(prjctName);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(user.getUsername());
        logging.setProjectId(project.getId());


    }

    private void setLogging(Logging logging, String eventType,String loggingText,String pathName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);

        String logText = loggingText+" isimli projeye "+pathName+" " ;
        logging.setLoggingText(logText);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(project.getUser().getUsername());
        logging.setProjectId(project.getId());


    }

    private void fileSave() throws ParseException {
        ArrayList<String> path=new ArrayList<>();
        ArrayList<String> name=new ArrayList<>();

        String nameElement=convertStringToDate(project.getDate())+"/"+project.getId()+"/";

        langs.forEach(p->{
            path.add(p.getPath());

            name.add(nameElement+p.getName());
            Document doc=new Document();
            doc.setPath(nameElement+p.getName());
            doc.setProject(project);
            document.add(doc);
            Logging l1 = new Logging();

            try {
                setLogging(l1,"adında bir döküman eklendi",project.getProjectName(),p.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            loggingRepository.save(l1);
        });
        if(name.size()!=0) {
            new FileFtp().upload(name, path);
            documentRepository.saveAll(document);
        }



        langs.clear();


    }
    @FXML
    public void saveButtonAction(){
        ButtonType ok = new ButtonType("Tamam",ButtonBar.ButtonData.OK_DONE);
        ButtonType apply = new ButtonType("Onayla",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("İptal",ButtonBar.ButtonData.CANCEL_CLOSE);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION,"Yeni Proje Ekleme","Bilgi","Projeyi eklemek istediğinizden emin misiniz?",apply,cancel);
        if (AlertHelper.getAlert().getResult() == apply) {
            //do stuff
            try {

                setVariableProject();
                projectRepository.save(project);
                //remindRepository.save(project.getRemind());



                Logging l1 = new Logging();
                setLogging(l1,"isimli proje eklendi",project.getProjectName());
                loggingRepository.save(l1);

                fileSave();

                if(project.getProjectRemind().equals("Var"))
                {
                    Logging l2 = new Logging();
                    String logMessage = getProject().getProjectName();
                    setLogging(l2,"isimli projeye hatırlatma eklendi",logMessage);
                    loggingRepository.save(l2);
                }


            }
            catch (HibernateException | ParseException e){
                System.out.println("Hata!");
                AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Yeni proje kaydedilirken bir hata oluştu!",ok,null);
                return;
            }
        }
        stageManager.switchScene("/home.fxml","Ana Ekran");
    }



    @FXML
    public void browseButtonAction(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Yüklemek istediğiniz dosyayı seçiniz");
        fileChooser.setInitialFileName(new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new Date()) +".xlsx");
        File selectedFile=fileChooser.showOpenDialog(null);
        if(selectedFile!=null){

            langs.add(selectedFile);
            documentFileListView.getItems().add(selectedFile.getPath());
            System.out.println(selectedFile.getName());
        }
        else
            System.out.println("file is not valid");
    }

    @FXML
    public void handleCheckBoxAction(){

        //setVariableProject();
        if(projectNameTextField.getText()!=null)
            project.setProjectName(projectNameTextField.getText());

        RemindController remindController= stageManager.showDialog("/new_remind.fxml","Hatırlatma").getController();
        if(!remindCheckBox.isSelected()){
            remindCheckBox.setSelected(true);
            remindController.initData(project.getRemind());
            remindController.setNewProjectController(this);
        }
        else{

            remindCheckBox.setSelected(false);
            remindController.setNewProjectController(this);
        }
    }

    @FXML
    public void newPaperButtonAction(){  // Veriler yoksa uyarı versin
        //henuz yok
        NewWorkController newWorkController= stageManager.showDialog("/new_work.fxml","Yeni İş Ekle").getController();
        setVariableProject();
        newWorkController.deleteButton.setVisible(false);
        newWorkController.newWorkEditEnableButton.setVisible(false);
        newWorkController.setNewProjectController(this);

    }

    @FXML
    public void deleteDocumentButtonAction() {
        langs.remove(documentFileListView.getSelectionModel().getSelectedIndex());
        documentFileListView.getItems().remove(documentFileListView.getSelectionModel().getSelectedIndex());
    }
    @FXML
    public void backButtonAction(){
        stageManager.closeDialog();
        stageManager.switchScene("/home.fxml","Ana Ekran");
    }
}
