package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.Logging;
import com.celal258.cagoz.entity.LoggingEventType;
import com.celal258.cagoz.entity.Paper;
import com.celal258.cagoz.entity.Project;
import com.celal258.cagoz.repository.LoggingEventTypeRepository;
import com.celal258.cagoz.repository.LoggingRepository;
import com.celal258.cagoz.repository.PaperRepository;
import com.celal258.cagoz.repository.ProjectRepository;
import com.celal258.cagoz.utils.AlertHelper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class NewWorkController implements Initializable {

    @FXML
    private VBox newWorkEditEnableVBox;

    @FXML
    private VBox DeleteAndUpdateVBox;

    @FXML
    private GridPane InputGP;

    @FXML
    private CheckBox remindCheckBox;

    @FXML
    private ComboBox paperOperationTypeComboBox;

    @FXML
    private ComboBox paperApprovalStatusComboBox;

    @FXML
    private DatePicker paperDateOfGoing;

    @FXML
    private DatePicker paperDateOfReturn;

    @FXML
    private TextArea paperNote;

    @FXML
    private TextField recordNumber;

    @FXML
    public Button deleteButton;

    @FXML
    public Button newWorkEditEnableButton;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private ProjectRepository projectRepository;

    private ProjectDetailController projectDetailController;

    private NewProjectController newProjectController;

    private Logging remindWorkLogging;

    private Logging remindWorkDeleteLogging;

    private Boolean isInitWorked;

    private Paper paper;

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Project getProject() {

        if(getNewProjectController()!=null)
            return getNewProjectController().getProject();
        else
            return getProjectDetailController().getProject();
    }

    public String getPaperOperationTypeComboBoxValue(){

        return paperOperationTypeComboBox.getSelectionModel().getSelectedItem().toString();
    }

    public String getProjectName () {
        if(getNewProjectController()!=null)
            return getNewProjectController().getProject().getUser().getUsername();
        else
            return getProjectDetailController().getProject().getUser().getUsername();

    }

    public void SetRemindWorkLogging (Logging temp) {
        remindWorkLogging = temp;

    }

    public void SetRemindWorkDeleteLogging (Logging temp) {
        remindWorkDeleteLogging = temp;

    }

    public String GetUserName () {
        if(getNewProjectController()!=null)
            return getNewProjectController().getProject().getUser().getUsername();
        else
            return getProjectDetailController().getProject().getUser().getUsername();

    }

    public Long GetProjectId () {
        if(getNewProjectController()!=null)
            return getNewProjectController().getProject().getId();
        else
            return getProjectDetailController().getProject().getId();

    }

    public NewProjectController getNewProjectController() {
        return newProjectController;
    }

    public void setNewProjectController(NewProjectController newProjectController) {
        this.newProjectController = newProjectController;
    }

    public ProjectDetailController getProjectDetailController() {
        return projectDetailController;
    }

    public void setProjectDetailController(ProjectDetailController projectDetailController) {
        this.projectDetailController = projectDetailController;
    }

    public NewWorkController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         paperOperationTypeComboBox.getItems().addAll(
                "Enerji Müsaadesi",
                "Proje Onayı",
                "Yapı Denetim",
                "Abone Kontrolü",
                "Sevk",
                "Sayaç Alımı"
        );
        paperApprovalStatusComboBox.getItems().addAll(
                "Bilinmiyor",
                "Onaylanmadı",
                "Onaylandı"
        );
        isInitWorked = false;
        remindWorkLogging =null;
        remindWorkDeleteLogging = null;
        paperOperationTypeComboBox.getSelectionModel().select(0);
        paperApprovalStatusComboBox.getSelectionModel().select(0);

        paperOperationTypeComboBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, o, t1) ->
        {

            if (paperOperationTypeComboBox.getSelectionModel().getSelectedIndex() == 0)
                    recordNumber.setDisable(false);
            else
            {
                recordNumber.setDisable(true);
                recordNumber.setText("");
            }
        });
        paperNote.textProperty().addListener((observableValue, eventHandler, t1) -> {
            paperNote.setText(paperNote.getText().toUpperCase());
        });

        paper = new Paper();
    }

    public void setDisableRecordNumber(){
        recordNumber.setDisable(true);
    }

    public void newWorkEditEnableButtonAction(){
        editButtonforNewWorkPage(false);
    }

    public CheckBox getRemindCheckBox() {
        return remindCheckBox;
    }

    public void editButtonforNewWorkPage(Boolean editEnable){

        newWorkEditEnableVBox.setVisible(editEnable);
        DeleteAndUpdateVBox.setVisible(!editEnable);

        if(editEnable)
            InputGP.setDisable(true);
        else
            InputGP.setDisable(false);

    }

    public void setInitData(Long id){
        paper=paperRepository.findById(id).get();
        paperOperationTypeComboBox.getSelectionModel().select(paper.getOperationType());
        paperApprovalStatusComboBox.getSelectionModel().select(paper.getApprovalStatus());
        if(paper.getDateOfGoing()!=null)paperDateOfGoing.setValue(paper.getDateOfGoing().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if(paper.getDateOfReturn()!=null)paperDateOfReturn.setValue(paper.getDateOfReturn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        paperNote.setText(paper.getPaperNote());
        recordNumber.setText(paper.getRecordNumber());
        remindCheckBox.setSelected(paper.getRemindWork()!=null?true:false);
        isInitWorked =true;
    }


    @FXML
    public void handleCheckBoxAction(){



        RemindWorkController remindWorkController= stageManager.showDialog("/new_remind_work.fxml","Hatırlatma").getController();

        remindWorkController.SetProjectNameAndOperationType(getProject().getProjectName(),paperOperationTypeComboBox.getSelectionModel().getSelectedItem().toString(),GetUserName(),GetProjectId());

        if(!remindCheckBox.isSelected()&&paper.getRemindWork()!=null){
            remindWorkController.initData(paper.getRemindWork());
            remindWorkController.setNewWorkController(this);
        }

        remindWorkController.setNewWorkController(this);


        if(remindCheckBox.isSelected())
            remindCheckBox.setSelected(false);
        else
            remindCheckBox.setSelected(true);
    }
    @FXML
    public void saveButtonAction() throws ParseException {

        System.out.println("Yeni işlem kaydetme fonksiyonu calisti ... ");
        boolean control = false;
        ButtonType ok = new ButtonType("Tamam",ButtonBar.ButtonData.OK_DONE);
        ButtonType apply = new ButtonType("Onayla",ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("İptal",ButtonBar.ButtonData.CANCEL_CLOSE);
        AlertHelper.showAlert(Alert.AlertType.CONFIRMATION,"Yeni İşlem Ekleme","Bilgi","Yeni işlem eklemek istediğinizden emin misiniz?",
                apply,cancel);
        if(AlertHelper.getAlert().getResult() == apply){
            try{
                if(!(paperDateOfGoing.getEditor().getText().isEmpty())&&paperDateOfGoing.getValue()!=null)paper.setDateOfGoing(new SimpleDateFormat("yyyy-MM-dd").parse(paperDateOfGoing.getValue().toString()));
                else
                    paper.setDateOfGoing(null);
                if(!(paperDateOfReturn.getEditor().getText().isEmpty())&&paperDateOfReturn.getValue()!=null)paper.setDateOfReturn(new SimpleDateFormat("yyyy-MM-dd").parse(paperDateOfReturn.getValue().toString()));
                else
                    paper.setDateOfReturn(null);
                paper.setApprovalStatus(paperApprovalStatusComboBox.getSelectionModel().getSelectedItem().toString());
                paper.setOperationType(paperOperationTypeComboBox.getSelectionModel().getSelectedItem().toString());
                paper.setPaperNote(paperNote.getText());
                paper.setRecordNumber(recordNumber.getText());
                paper.setProject(getProject());

                if(paper.getRemindWork()!=null)
                    paper.setPaperRemind("Var");
                else
                    paper.setPaperRemind("Yok");


                if(getNewProjectController()!=null){
                    getNewProjectController().saveButtonAction();
                }
                paperRepository.save(paper);

                if(!isInitWorked){
                    Logging l1 = new Logging();
                    String logMessage = paper.getProject().getProjectName() + " isimli projeye " + paper.getOperationType() ;
                    setLogging(l1,"isimli işlem eklendi",logMessage);
                    loggingRepository.save(l1);
                    if(remindWorkLogging!=null)
                        loggingRepository.save(remindWorkLogging);
                    if(remindWorkDeleteLogging!=null)
                        loggingRepository.save(remindWorkDeleteLogging);
                }
                else
                {
                    Logging l1 = new Logging();
                    String logMessage = paper.getProject().getProjectName() + " isimli projenin " + paper.getOperationType() ;
                    setLogging(l1,"isimli işlemi düzenlendi",logMessage);
                    loggingRepository.save(l1);
                    if(remindWorkLogging!=null)
                        loggingRepository.save(remindWorkLogging);
                    if(remindWorkDeleteLogging!=null)
                        loggingRepository.save(remindWorkDeleteLogging);
                }


            }
            catch (HibernateException e){
                System.out.println("Hata!");
                AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Yeni işlem kaydedilirken bir hata oluştu!",ok,null);
                return;
            }
            if (getProjectDetailController()!=null){
                getProjectDetailController().initData(getProject().getId());
                stageManager.closeDialog();
            }
            if(getNewProjectController()!=null)getNewProjectController().backButtonAction();
        }
        projectDetailController=null;
        newProjectController=null;
    }

    private void setLogging(Logging logging, String eventType, String prjctName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);



        logging.setLoggingText(prjctName);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(paper.getProject().getUser().getUsername());
        logging.setProjectId(paper.getProject().getId());

    }
    @FXML
    public void deleteButtonAction(){
        paperRepository.deleteById(paper.getId());
        projectDetailController.initData(getProject().getId());
        stageManager.closeDialog();
    }

    @FXML
    public void selectReturnDate(){

        remindCheckBox.setDisable(false);
    }
}
