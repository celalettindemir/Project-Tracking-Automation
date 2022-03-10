package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import com.celal258.cagoz.service.ProjectDetailService;
import com.celal258.cagoz.utils.AlertHelper;
import com.celal258.cagoz.utils.FileFtp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class ProjectDetailController implements Initializable {

    @FXML
    private VBox MiddleFrameVBox;

    @FXML
    private VBox LeftFrameVBox;

    @FXML
    private CheckBox TapuCheckBox;

    @FXML
    private CheckBox completedCheckBox;

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
    private ComboBox projectTypeComboBox;

    @FXML
    private ComboBox projectDrawComboBox;

    @FXML
    private TableView<Paper> tableView;

    @FXML
    private TableColumn<Paper, Long> id;

    @FXML
    private TableColumn<Paper, String> operationType;

    @FXML
    private TableColumn<Paper, String> dateOfGoing;

    @FXML
    private TableColumn<Paper, String> dateOfReturn;

    @FXML
    private TableColumn<Paper, String> approvalStatus;

    @FXML
    private TableColumn<Paper, String> paperNote;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private Text projectIdText;

    @FXML
    private Text ikiNoktaText;

    @FXML
    private TextField projectNameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField receivedMoneyTextField;

    @FXML
    private TextField adaParselTextField;

    @FXML
    private TextField projectIdTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private CheckBox remindCheckBox;

    @FXML
    private ListView<String> documentFileListView;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    LoggingEventTypeRepository loggingEventTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    private ObservableList<Paper> paperList = FXCollections.observableArrayList();

    @Autowired
    private StageManager stageManager;

    @Autowired
    private ProjectDetailService projectDetailService;

    private Project project;

    private  Project prjctTemp ;

    private String pathName;

    private List<Document> documents;

    private User user;

    private ObservableList<File> langs;

    private List <Long> documentsIds;

    public Project getProject() {
        return project;
    }

    public Remind getRemind() {
        return project.getRemind();
    }

    public void setRemind(Remind remind) {

        this.project.setRemind(remind);
        if(remind == null){
            project.setProjectRemind("Yok");
            getRemindCheckBox().setSelected(false);
        }
        else{
            project.setProjectRemind("Var");
            getRemindCheckBox().setSelected(true);
        }

    }

    public CheckBox getRemindCheckBox() {
        return remindCheckBox;
    }

    public void setRemindCheckBox(CheckBox remindCheckBox) {
        this.remindCheckBox = remindCheckBox;
    }


    private String PathDeed="",PathLicense="",PathApplication="",PathSavingSubscriptionInformation="",PathDistrictAgriculture="",PathIdentityCard="",
            PathMunicipalityDeclaration="",PathProject="",PathIskan="",PathZoning="",PathDsi="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        projectDrawComboBox.getItems().addAll(
                "Kendi projesi var","Çizilecek","Belli değil"
        );
        //UpperCase
        customerNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            customerNameTextField.setText(customerNameTextField.getText().toUpperCase());
        });
        projectNameTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            projectNameTextField.setText(projectNameTextField.getText().toUpperCase());
        });
        descriptionTextArea.textProperty().addListener((observableValue, eventHandler, t1) -> {
            descriptionTextArea.setText(descriptionTextArea.getText().toUpperCase());
        });

        adaParselTextField.textProperty().addListener((observableValue, eventHandler, t1) -> {
            adaParselTextField.setText(adaParselTextField.getText().toUpperCase());
        });

        addTextLimiterOnlyNumber(priceTextField);
        addTextLimiterOnlyNumber(receivedMoneyTextField);




    }
    public void initData(Long id)
    {
        paperList.clear();
        for(Paper paper: paperRepository.findPapersByProjectId(id)){
            System.out.println(paper);
            paperList.addAll(paper);
        }
        project=projectRepository.findById(id).get();
        if(project!=null)
            setDataInput(project);
        if(project.getRemind() == null){
            remindCheckBox.setSelected(false);
            project.setProjectRemind("Yok");
        }
        else{
            remindCheckBox.setSelected(true);
            project.setProjectRemind("Var");
        }
        documents = new ArrayList<Document>();

        TapuCheckBox.setSelected(project.getProjectTapu());
        completedCheckBox.setSelected(project.getProjectDurum());
        RuhsatCheckBox.setSelected(project.getProjectRuhsat());
        VekaletCheckBox.setSelected(project.getProjectVekalet());
        ImarAffiCheckBox.setSelected(project.getProjectImarAffi());
        BelediyeYazisiCheckBox.setSelected(project.getProjectBelediyeYazisi());
        IlceTarimCheckBox.setSelected(project.getProjectIlceTarim());
        DsiCheckBox.setSelected(project.getProjectDSI());
        MevcutFaturaCheckBox.setSelected(project.getProjectMevcutFatura());
        EmlakBeyaniCheckBox.setSelected(project.getProjectEmlakBeyani());

        projectDrawComboBox.getSelectionModel().select(0);
        //projectDocumentComboBox.getSelectionModel().select(0);
        setColumnProperties();
        doubleClickAction();

        getDocumentsInit();
    }

    public void initUserId(User user)
    {
        this.user = user;
    }

    public void setDataInput(Project project)
    {
        user.getRoles().forEach(p->
        {
            System.out.println("Detay data input");
            System.out.println(p.getName());
            if(!p.getName().equals("ROLE_USER"))
            {
                projectIdText.setVisible(true);
                projectIdTextField.setVisible(true);
                ikiNoktaText.setVisible(true);
                projectIdTextField.setText(project.getId().toString());
            }


        });
        customerNameTextField.setText(project.getCustomer().getFirstName());
        projectNameTextField.setText(project.getProjectName());
        adaParselTextField.setText(project.getProjectAda());
        priceTextField.setText(Integer.toString(project.getPrice()));
        receivedMoneyTextField.setText(Integer.toString(project.getReceivedMoney()));
        descriptionTextArea.setText(project.getProjectDescription());

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
            System.out.println(ex);
        }
        return dateString;
    }
    private void setColumnProperties(){
        tableView.setEditable(true);
        //Project project = new Project();

        id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        operationType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOperationType()));
        dateOfGoing.setCellValueFactory(cellData -> new SimpleObjectProperty<>(convertStringToDate(cellData.getValue().getDateOfGoing())));
        dateOfReturn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(convertStringToDate(cellData.getValue().getDateOfReturn())));
        approvalStatus.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getApprovalStatus()));
        paperNote.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPaperNote()));

        tableView.setItems(paperList);


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


    private void doubleClickAction(){
        tableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)){
                if(tableView.getSelectionModel().getSelectedItem() != null){
                    NewWorkController newWorkController =stageManager.showDialog("/new_work.fxml","İş Düzenle").getController();
                    newWorkController.setInitData(tableView.getSelectionModel().getSelectedItem().getId());
                    newWorkController.editButtonforNewWorkPage(true);
                    newWorkController.setProjectDetailController(this);
                }
            }
        });
    }

    @FXML
    public void deleteButtonAction(){
        try{
            System.out.println("Proje Id ...:" + project.getId());
            projectRepository.deleteById(project.getId());

            Logging l1 = new Logging();
            setLogging(l1,"isimli proje silindi",project.getProjectName());
            loggingRepository.save(l1);

        }
        catch (DataIntegrityViolationException | ParseException e){ // projeye bağlı document varsa  silme işlemi yapmıyor olabilir .
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","İlgili proje silinememektedir!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
            return;
        }
        stageManager.switchScene("/home.fxml","Ana Sayfa");
    }


    private void FileSave() throws ParseException {
        ArrayList<String> path = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();



        langs.forEach(p-> {
            System.out.println("langs......");
            System.out.println(p.getPath());
         });
        String nameElement=convertStringToDate(project.getDate())+"/"+project.getId()+"/";
        langs.forEach(p->{
            path.add(p.getPath());

            System.out.println("p.getPath()......");
            System.out.println(p.getPath());


            name.add(nameElement+p.getName());
            Document doc=new Document();
            doc.setPath(nameElement+p.getName());
            doc.setProject(project);
            documents.add(doc);
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
            documentRepository.saveAll(documents);
        }




        System.out.println("p.getPath()");
        documents.clear();
        System.out.println(".getPath(p");

    }

    @FXML
    public void getDocumentsInit() {

        documentsIds=new ArrayList<Long>();

        documentFileListView.getItems().clear();

        if(!documentRepository.findDocumentsByProjectId(project.getId()).isEmpty()){

            documentRepository.findDocumentsByProjectId(project.getId()).forEach(document ->{


                documentFileListView.getItems().add(document.getPath());
                documentsIds.add(document.getId());

            });
        }


/*
       System.out.println(documentRepository.findDocumentsByProjectId(project.getId()).isEmpty());

        documentRepository.findDocumentsByProjectId(project.getId()).forEach(document ->{


            System.out.println("Path ::" + document.getPath());

            documentFileListView.getItems().add(document.getPath());

        });
*/
    }

    @FXML
    public void browseButtonAction() throws ParseException {

        langs = FXCollections.observableArrayList();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Yüklemek istediğiniz dosyayı seçiniz");
        fileChooser.setInitialFileName(new SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(new Date()) +".xlsx");
        File selectedFile=fileChooser.showOpenDialog(null);
        if(selectedFile!=null){
            langs.add(selectedFile);
            //documentFileListView.getItems().add(selectedFile.getPath());
            System.out.println(selectedFile.getName());
            FileSave();
            getDocumentsInit();



        }
        else
            System.out.println("file is not valid");


        System.out.println(".........IDLER Browse.........");
        documentsIds.forEach(Ids ->{


            System.out.println(Ids);
        });
    }
    @FXML
    public void deleteDocumentButtonAction() {

        //langs.remove(documentFileListView.getSelectionModel().getSelectedIndex());



        System.out.println("Liste Id....");

        System.out.println(documentFileListView.getSelectionModel().getSelectedIndex());

        System.out.println(".........IDLER.........");
        documentsIds.forEach(Ids ->{

            System.out.println(Ids);

        });

        System.out.println("Listeden seçilen ID");
        System.out.println(documentsIds.get(documentFileListView.getSelectionModel().getSelectedIndex()));

        Document tempDoc = documentRepository.findById(documentsIds.get(documentFileListView.getSelectionModel().getSelectedIndex())).get();
        pathName= tempDoc.getPath();

        documentRepository.deleteById(documentsIds.get(documentFileListView.getSelectionModel().getSelectedIndex()));
        documentsIds.remove(documentFileListView.getSelectionModel().getSelectedIndex());

        //documents.remove(documentFileListView.getSelectionModel().getSelectedIndex());

        documentFileListView.getItems().remove(documentFileListView.getSelectionModel().getSelectedIndex());
        Logging l1 = new Logging();
        try {
            setLogging(l1,"adında bir döküman silindi",project.getProjectName(),pathName);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        loggingRepository.save(l1);

        getDocumentsInit();

    }
    @FXML
    public void saveButtonAction() throws ParseException{



        prjctTemp = new Project();

        BeanUtils.copyProperties(project, prjctTemp);

        project.getCustomer().setFirstName(customerNameTextField.getText());
        project.setProjectName(projectNameTextField.getText());
        project.setPrice(Integer.valueOf(priceTextField.getText()));
        project.setReceivedMoney(Integer.valueOf(receivedMoneyTextField.getText()));
        project.setProjectOrder(Integer.valueOf(priceTextField.getText())-Integer.valueOf(receivedMoneyTextField.getText()) == 0 ? "Ödendi" : "Ödenmedi");
        project.setProjectDescription(descriptionTextArea.getText());
        project.setProjectAda(adaParselTextField.getText());
        project.setProjectTapu(TapuCheckBox.isSelected());
        project.setProjectRuhsat(RuhsatCheckBox.isSelected());
        project.setProjectVekalet(VekaletCheckBox.isSelected());
        project.setProjectImarAffi(ImarAffiCheckBox.isSelected());
        project.setProjectBelediyeYazisi(BelediyeYazisiCheckBox.isSelected());
        project.setProjectIlceTarim(IlceTarimCheckBox.isSelected());
        project.setProjectDSI(DsiCheckBox.isSelected());
        project.setProjectMevcutFatura(MevcutFaturaCheckBox.isSelected());
        project.setProjectEmlakBeyani(EmlakBeyaniCheckBox.isSelected());
        project.setCompleted(completedCheckBox.isSelected());

        if(project.getRemind() == null){
            project.setProjectRemind("Yok");
        }
        else{
            project.setProjectRemind("Var");
        }


        project.setProjectDurum(completedCheckBox.isSelected());

        if(!(prjctTemp.getProjectName().equals(project.getProjectName()) && prjctTemp.getCustomer()==project.getCustomer() &&
                prjctTemp.getPrice()==project.getPrice()&&prjctTemp.getReceivedMoney()==project.getReceivedMoney()&&
                prjctTemp.getProjectDescription().equals(project.getProjectDescription())))
        {

            Logging l1 = new Logging();
            setLogging(l1,"isimli projenin bilgileri düzenlendi",project.getProjectName());
            loggingRepository.save(l1);
        }


        projectRepository.save(project);

    }

    private void setLogging(Logging logging, String eventType,String loggingText) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);

        logging.setLoggingText(loggingText);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(project.getUser().getUsername());
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

    @FXML
    public void editEnableButtonAction(){
        if(MiddleFrameVBox.isDisable()){


            customerNameTextField.setDisable(false);
            customerNameTextField.setStyle("-fx-opacity: 1.0;");

            projectNameTextField.setDisable(false);
            projectNameTextField.setStyle("-fx-opacity: 1.0;");

            adaParselTextField.setDisable(false);
            adaParselTextField.setStyle("-fx-opacity: 1.0;");

            priceTextField.setDisable(false);
            priceTextField.setStyle("-fx-opacity: 1.0;");

            receivedMoneyTextField.setDisable(false);
            receivedMoneyTextField.setStyle("-fx-opacity: 1.0;");


            descriptionTextArea.setEditable(true);
            descriptionTextArea.setStyle("-fx-opacity: 1.0;");

            projectDrawComboBox.setDisable(false);
            projectDrawComboBox.setStyle("-fx-opacity: 1;");

            remindCheckBox.setDisable(false);
            completedCheckBox.setDisable(false);

            MiddleFrameVBox.setDisable(false);


        }
        else {
            MiddleFrameVBox.setDisable(true);

            customerNameTextField.setDisable(true);
            customerNameTextField.setStyle("-fx-opacity: 0.75;");

            projectNameTextField.setDisable(true);
            projectNameTextField.setStyle("-fx-opacity: 0.75;");

            adaParselTextField.setDisable(true);
            adaParselTextField.setStyle("-fx-opacity: 0.75;");

            priceTextField.setDisable(true);
            priceTextField.setStyle("-fx-opacity: 0.75;");

            receivedMoneyTextField.setDisable(true);
            receivedMoneyTextField.setStyle("-fx-opacity: 0.75;");

            descriptionTextArea.setEditable(false);
            descriptionTextArea.setStyle("-fx-opacity: 0.75;");

            projectDrawComboBox.setDisable(true);
            projectDrawComboBox.setStyle("-fx-opacity: 1;");

            remindCheckBox.setDisable(true);
            completedCheckBox.setDisable(true);

        }
    }
    @FXML
    public void setDisableAll(String projectType){

        System.out.println( "Proje Türü");
        System.out.println( projectType);

        MiddleFrameVBox.setDisable(true);

        customerNameTextField.setDisable(true);
        customerNameTextField.setStyle("-fx-opacity: 0.75;");

        projectNameTextField.setDisable(true);
        projectNameTextField.setStyle("-fx-opacity: 0.75;");

        adaParselTextField.setDisable(true);
        adaParselTextField.setStyle("-fx-opacity: 0.75;");

        priceTextField.setDisable(true);
        priceTextField.setStyle("-fx-opacity: 0.75;");

        receivedMoneyTextField.setDisable(true);
        receivedMoneyTextField.setStyle("-fx-opacity: 0.75;");

        descriptionTextArea.setEditable(false);
        descriptionTextArea.setStyle("-fx-opacity: 0.75;");

        projectDrawComboBox.setDisable(true);
        projectDrawComboBox.setStyle("-fx-opacity: 1;");

        remindCheckBox.setDisable(true);
        completedCheckBox.setDisable(true);

        disableProjectDocumentByType(projectType);
    }

    @FXML
    public void disableProjectDocumentByType(String projectType){


        TapuCheckBox.setDisable(false);
        RuhsatCheckBox.setDisable(false);
        VekaletCheckBox.setDisable(false);
        ImarAffiCheckBox.setDisable(false);
        BelediyeYazisiCheckBox.setDisable(false);
        IlceTarimCheckBox.setDisable(false);
        DsiCheckBox.setDisable(false);
        MevcutFaturaCheckBox.setDisable(false);
        EmlakBeyaniCheckBox.setDisable(false);

        switch (projectType)
        {
            case "Yeni Abone":
                IlceTarimCheckBox.setDisable(true);
                DsiCheckBox.setDisable(true);
                MevcutFaturaCheckBox.setDisable(true);
                break;
            case "Çevre Aydınlatma":
                ImarAffiCheckBox.setDisable(true);
                BelediyeYazisiCheckBox.setDisable(true);
                IlceTarimCheckBox.setDisable(true);
                DsiCheckBox.setDisable(true);
                EmlakBeyaniCheckBox.setDisable(true);
                break;
            case "Kat İlavesi":
                IlceTarimCheckBox.setDisable(true);
                DsiCheckBox.setDisable(true);
                break;
            case "Şantiye":
                ImarAffiCheckBox.setDisable(true);
                BelediyeYazisiCheckBox.setDisable(true);
                IlceTarimCheckBox.setDisable(true);
                DsiCheckBox.setDisable(true);
                MevcutFaturaCheckBox.setDisable(true);
                EmlakBeyaniCheckBox.setDisable(true);
                break;
            case "Tarımsal Sulama":
                RuhsatCheckBox.setDisable(true);
                ImarAffiCheckBox.setDisable(true);
                BelediyeYazisiCheckBox.setDisable(true);
                MevcutFaturaCheckBox.setDisable(true);
                EmlakBeyaniCheckBox.setDisable(true);
                break;
            case "Güç Düşümü/Artırımı":
                RuhsatCheckBox.setDisable(true);
                ImarAffiCheckBox.setDisable(true);
                BelediyeYazisiCheckBox.setDisable(true);
                IlceTarimCheckBox.setDisable(true);
                DsiCheckBox.setDisable(true);
                EmlakBeyaniCheckBox.setDisable(true);
                break;
        }




    }


    @FXML
    public void downloadButtonAction() {

        FileFtp fileFtp=new FileFtp();
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Yüklemek istediğiniz dosyayı seçiniz");
        //fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        File selectedDirectory=fileChooser.showDialog(null);

        if(selectedDirectory!=null){

            documentRepository.findDocumentsByProjectId(project.getId()).forEach(document ->{
                fileFtp.Download(document.getPath(),selectedDirectory+"/"+document.getPath().substring(document.getPath().lastIndexOf("/")));
            });

        }
        else
            System.out.println("file is not valid");
    }


    @FXML
    public void newWorkButtonAction(){
        NewWorkController newWorkController= stageManager.showDialog("/new_work.fxml","Yeni İşlem Ekleme").getController();
        newWorkController.setProjectDetailController(this);
        //newWorkController.setDisableRecordNumber();
        newWorkController.editButtonforNewWorkPage(false);
        newWorkController.deleteButton.setVisible(true);
    }

    @FXML
    public void handleCheckBoxAction(){
        if(!remindCheckBox.isSelected()){
            remindCheckBox.setSelected(true);
            RemindController remindController= stageManager.showDialog("/new_remind.fxml","Hatırlatma").getController();
            remindController.initData(project.getRemind());
            remindController.setProjectDetailController(this);
        }
        else{
            remindCheckBox.setSelected(false);
            RemindController remindController= stageManager.showDialog("/new_remind.fxml","Hatırlatma").getController();
            remindController.setProjectDetailController(this);
        }
    }

    @FXML
    public void onMouseClicked(){
        System.out.println("Tiklandi");
    }
    @FXML
    public void backButtonAction(){
        stageManager.switchScene("/home.fxml","home");
    }
}
