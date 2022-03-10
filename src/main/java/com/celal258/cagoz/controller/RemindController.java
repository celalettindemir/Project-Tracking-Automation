package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.LoggingEventTypeRepository;
import com.celal258.cagoz.repository.LoggingRepository;
import com.celal258.cagoz.repository.ProjectRepository;
import com.celal258.cagoz.repository.RemindRepository;
import com.celal258.cagoz.utils.AlertHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.aspectj.bridge.IMessage;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.net.URL;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class RemindController implements Initializable {

    @FXML
    private DatePicker remindStartDate;

    @FXML
    private RadioButton oneDayRadio;

    @FXML
    private RadioButton threeDaysRadio;

    @FXML
    private RadioButton sevenDaysRadio;

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
    private TextArea remindDescription;

    @Autowired
    private RemindRepository remindRepository;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;



    @Autowired
    private StageManager stageManager;

    private Remind remind;

    private String remindTime;

    private Boolean editted;

    private Boolean isInitRemind;

    private String message;

    private Project project;

    private Paper paper;


    public Project getProject() {

        if(getNewProjectController()!=null)
            return getNewProjectController().getProject();
        else
            return getProjectDetailController().getProject();
    }
    public Initializable getController() {

        if(getNewProjectController()!=null)
            return getNewProjectController();
        else
            return getProjectDetailController();
    }


    public Paper getPaper() {
        return getNewWorkController().getPaper();
    }

    private ProjectDetailController projectDetailController;
    private NewProjectController newProjectController;

    private NewWorkController newWorkController;

    public ProjectDetailController getProjectDetailController() {
        return projectDetailController;
    }

    public void setProjectDetailController(ProjectDetailController projectDetailController) {
        this.projectDetailController = projectDetailController;

        if(!isInitRemind)
            SetReminderMessage();
    }

    public NewProjectController getNewProjectController() {
        return newProjectController;
    }

    public void setNewProjectController(NewProjectController newProjectController) {
        this.newProjectController = newProjectController;

        if(!isInitRemind)
            SetReminderMessage();
    }
    public NewWorkController getNewWorkController() {
        return newWorkController;
    }

    public void setNewWorkController(NewWorkController newWorkController) {
        this.newWorkController = newWorkController;

    }
    public RemindController(){
        remind = new Remind();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.remind = new Remind();
        remindTime = "1";
        isInitRemind = false;
        remindDescription.textProperty().addListener((observableValue, eventHandler, t1) -> {
            remindDescription.setText(remindDescription.getText().toUpperCase());
        });
    }

    public void SetReminderMessage(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        LocalDate localDate = LocalDate.parse(dateFormat.format(date), formatter);

        remindStartDate.setValue(localDate);

        editted = false;
        if (getProjectDetailController() != null) {
            project = getProjectDetailController().getProject();
            String projectName = project.getProjectName();
            message = projectName + " isimli başvurunuzun ";
            String projectType = project.getProjectType();
            switch (projectType)
            {
                case "Yeni Abone":

                    if(!(getProjectDetailController().IsTapuSelected()))
                    {
                        message += "Tapu belgesi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsRuhsatSelected()) && !(getProjectDetailController().IsImarAffiSelected()) && !(getProjectDetailController().IsBelediyeYazisiSelected()) && !(getProjectDetailController().IsEmlakBeyaniSelected())){

                        message+="Ruhsat,İmar Affı,Belediye Yazısı,Emlak Beyanı belgelerinden birisi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    break;
                case "Çevre Aydınlatma":

                    System.out.println("Switch içine de geldim ");
                    if(!(getProjectDetailController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsRuhsatSelected())){
                        message+="Ruhsat belgesi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsMevcutFaturaSelected())){

                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }


                    break;
                case "Kat İlavesi":
                    if(!(getProjectDetailController().IsTapuSelected()))
                    {
                        message += "Tapu belgesi ,  ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsRuhsatSelected()) && !(getProjectDetailController().IsImarAffiSelected()) && !(getProjectDetailController().IsBelediyeYazisiSelected()) && !(getProjectDetailController().IsEmlakBeyaniSelected())){

                        message+="Ruhsat,İmar Affı,Belediye Yazısı,Emlak Beyanı belgelerinden birisi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsMevcutFaturaSelected())){
                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }

                    break;
                case "Şantiye":
                    if(!(getProjectDetailController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }



                    if(!(getProjectDetailController().IsRuhsatSelected())){
                        message+="Ruhsat belgesi , ";
                        editted = true;
                    }



                    if(!(getProjectDetailController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }


                    break;
                case "Tarımsal Sulama":
                    if(!(getProjectDetailController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }



                    if(!(getProjectDetailController().IsDsiSelected())){
                        message+="DSİ belgesi , ";
                        editted = true;
                    }



                    if(!(getProjectDetailController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }


                    if(!(getProjectDetailController().IsIlceTarimSelected())){
                        message += "İlçe Tarım belgesi , ";
                        editted = true;
                    }


                    break;

                case "Güç Düşümü/Artırımı":
                    if(!(getProjectDetailController().IsTapuSelected())) {
                        message += "Tapu belgesi ,  ";
                        editted = true;
                    }

                    if(!(getProjectDetailController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getProjectDetailController().IsMevcutFaturaSelected())){
                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }
                    break;
            }


            if (editted)
                message+= "eksikliği giderildikten sonra işleme alınacaktır";
            else
                message = " ";

            remindDescription.setText(message);

        }
        else if (getNewProjectController() != null) {
            //project = getNewProjectController().getProject();
            String projectName2 = getNewProjectController().projectNameTextField.getText();
            message = projectName2 + " isimli başvurunuzun ";
            String projectType = getNewProjectController().projectTypeComboBox.getValue().toString();
            System.out.println(projectType);
            switch (projectType)
            {
                case "Yeni Abone":

                    if(!(getNewProjectController().IsTapuSelected()))
                    {
                        message += "Tapu belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsRuhsatSelected()) && !(getNewProjectController().IsImarAffiSelected()) && !(getNewProjectController().IsBelediyeYazisiSelected()) && !(getNewProjectController().IsEmlakBeyaniSelected())){

                        message+="Ruhsat,İmar Affı,Belediye Yazısı,Emlak Beyanı belgelerinden birisi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    break;
                case "Çevre Aydınlatma":
                    System.out.println("Switch içine de geldim ");
                    if(!(getNewProjectController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsRuhsatSelected())){
                        message+="Ruhsat belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsMevcutFaturaSelected())){
                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }
                    break;
                case "Kat İlavesi":
                    if(!(getNewProjectController().IsTapuSelected()))
                    {
                        message += "Tapu belgesi ,  ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsRuhsatSelected()) && !(getNewProjectController().IsImarAffiSelected()) && !(getNewProjectController().IsBelediyeYazisiSelected()) && !(getNewProjectController().IsEmlakBeyaniSelected())){

                        message+="Ruhsat,İmar Affı,Belediye Yazısı,Emlak Beyanı belgelerinden birisi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsMevcutFaturaSelected())){
                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }

                    break;
                case "Şantiye":
                    if(!(getNewProjectController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }



                    if(!(getNewProjectController().IsRuhsatSelected())){
                        message+="Ruhsat belgesi , ";
                        editted = true;
                    }



                    if(!(getNewProjectController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    break;
                case "Tarımsal Sulama":
                    if(!(getNewProjectController().IsTapuSelected())){
                        message += "Tapu belgesi ,";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsDsiSelected())){
                        message+="DSİ belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsVekaletSelected())){
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsIlceTarimSelected())){
                        message += "İlçe Tarım belgesi , ";
                        editted = true;
                    }
                    break;

                case "Güç Düşümü/Artırımı":
                    if(!(getNewProjectController().IsTapuSelected())) {
                        message += "Tapu belgesi ,  ";
                        editted = true;
                    }

                    if(!(getNewProjectController().IsVekaletSelected()))
                    {
                        message += "Vekalet belgesi , ";
                        editted = true;
                    }
                    if(!(getNewProjectController().IsMevcutFaturaSelected())){
                        message+= "Mevcut Fatura belgesi , ";
                        editted = true;
                    }
                    break;
            }
            if (editted)
                message+= "eksikliği giderildikten sonra işleme alınacaktır";
            else
                message = " ";

            remindDescription.setText(message);

        }
        remindDescription.setText(message);
    }


    public void initData(Remind remind) {
        if(remind != null){
            isInitRemind = true;
            this.remind=remind;
            if(remind.getStartDate()!=null)remindStartDate.setValue(remind.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            remindTime = remind.getRepeat();
            selectDaysRadio(remindTime);
            remindDescription.setText(remind.getDescription());
        }
    }

    public void selectDaysRadio(String days){

        if(days.equals("1"))
            oneDayRadio.setSelected(true);
        else if (days.equals("3"))
            threeDaysRadio.setSelected(true);
        else if (days.equals("7"))
            sevenDaysRadio.setSelected(true);
    }

    @FXML
    public void saveButtonAction(){
        System.out.println("Proje Kaydetmesi fonksiyonuna girdi ... ");
        if (remindStartDate.getValue() != null) {
            try {
                remind.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(remindStartDate.getValue().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        remind.setRepeat(remindTime);
        remind.setDescription(remindDescription.getText());

        System.out.println("=========?============");
        System.out.println("getProject().getProjectName()");
        System.out.println(getProject().getProjectName());

        remind.setProject(getProject());
        try {

            if (getProjectDetailController() != null) {
                getProject().setRemind(remind);
                projectDetailController.getRemindCheckBox().setSelected(true);
                getProject().setProjectRemind("Var");
                projectRepository.save(getProject());

                Logging l1 = new Logging();
                String logMessage = getProject().getProjectName();
                setLogging(l1,"isimli projeye hatırlatma eklendi",logMessage);
                loggingRepository.save(l1);

            }
            if (getNewProjectController() != null) {
                getProject().setRemind(remind);
                getNewProjectController().getRemindCheckBox().setSelected(true);
                getProject().setProjectRemind("Var");
                //projectRepository.save(project);
            }






            projectDetailController=null;
            newProjectController=null;
            stageManager.closeDialog();
        } catch (HibernateException | ParseException e) {
            AlertHelper.showAlert(Alert.AlertType.ERROR, "Hata", "", "Hatırlatma kaydedilirken bir hata oluştu!",
                    new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE), null);
            return;
        }

    }

    private void setLogging(Logging logging, String eventType, String prjctName) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);

        logging.setLoggingText(prjctName);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(getProject().getUser().getUsername());
        logging.setProjectId(getProject().getId());

    }

    @FXML
    public void deleteButtonAction() throws ParseException {
        //remindRepository.deleteById(project.getId());
        if(projectDetailController!=null){

            projectDetailController.getProject().setRemind(null);
            projectDetailController.getRemindCheckBox().setSelected(false);
            projectDetailController.getProject().setProjectRemind("Yok");
            projectRepository.save(projectDetailController.getProject());

            Logging l1 = new Logging();
            String logMessage = remind.getProject().getProjectName();
            setLogging(l1,"isimli projeye ait hatırlatma silindi",logMessage);
            loggingRepository.save(l1);

        }
        if (newProjectController!=null){

            newProjectController.getProject().setRemind(null);
            newProjectController.getRemindCheckBox().setSelected(false);
            newProjectController.getProject().setProjectRemind("Yok");
        }


        stageManager.closeDialog();
    }

    @FXML
    public void setRemindTimesRadio(){
        if(oneDayRadio.isSelected())
            remindTime = "1";
        else if (threeDaysRadio.isSelected())
            remindTime = "3";
        else
            remindTime = "7";
    }

}
