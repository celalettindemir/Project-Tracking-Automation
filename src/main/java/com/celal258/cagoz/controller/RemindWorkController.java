package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import com.celal258.cagoz.utils.AlertHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class RemindWorkController implements Initializable {

    @FXML
    private DatePicker remindStartDate;

    @FXML
    private RadioButton oneDayRadio;

    private String message;

    @FXML
    private RadioButton threeDaysRadio;

    @FXML
    private RadioButton sevenDaysRadio;

    private Boolean isInitRemind;

    @Autowired
    private LoggingRepository loggingRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;



    @FXML
    private TextArea remindDescription;

    @Autowired
    private StageManager stageManager;

    private String remindTime;

    private String projectName;

    private String operationType;

    private String userName;

    private Long projectId;

    private RemindWork remindWork;

    public Paper getPaper() {
        return getNewWorkController().getPaper();
    }

    private NewWorkController newWorkController;

    public NewWorkController getNewWorkController() {
        return newWorkController;
    }

    public void setNewWorkController(NewWorkController newWorkController) {
        this.newWorkController = newWorkController;
        if(!isInitRemind)
            SetReminderMessage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.remindWork = new RemindWork();
        remindTime = "1";
        remindDescription.textProperty().addListener((observableValue, eventHandler, t1) -> {
            remindDescription.setText(remindDescription.getText().toUpperCase());
        });

        isInitRemind = false;

    }
    public void initData(RemindWork remindWork) {
        if(remindWork != null){
            isInitRemind = true;
            this.remindWork=remindWork;
            if(remindWork.getStartDate()!=null)remindStartDate.setValue(remindWork.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            remindTime= remindWork.getRepeat();
            selectDaysRadio(remindTime);
            remindDescription.setText(remindWork.getDescription());
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

    public void SetReminderMessage(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //get current date time with Date()
        Date date = new Date();

        LocalDate localDate = LocalDate.parse(dateFormat.format(date), formatter);
        String comboBoxValue = newWorkController.getPaperOperationTypeComboBoxValue();
        String projectName = newWorkController.getProjectName();
        message = projectName + " isimli projenize ait " + comboBoxValue +" işi ile alakalı belgeleri ofisimizden teslim alabilirsiniz.";


        remindDescription.setText(message);
        remindStartDate.setValue(localDate);

    }

    public void SetProjectNameAndOperationType(String ProjectName , String OperationType , String UserName , Long ProjectId){

       projectName = ProjectName;
       operationType =OperationType;
       userName = UserName;
       projectId = ProjectId;
    }

    @FXML
    public void saveButtonAction(){
        if(getNewWorkController() != null) {
            System.out.println("İş Kaydetmesi fonksiyonuna girdi ... ");
            if (remindStartDate.getValue() != null) {
                try {
                    remindWork.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(remindStartDate.getValue().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                remindWork.setStartDate(new Date());
            }
            remindWork.setRepeat(remindTime);
            remindWork.setDescription(remindDescription.getText());
            remindWork.setWork(getPaper());
            try {

                if (getNewWorkController() != null) {
                    getPaper().setRemindWork(remindWork);
                    getPaper().setPaperRemind("Var");
                    newWorkController.getRemindCheckBox().setSelected(true);


                    Logging l1 = new Logging();
                    String logMessage = projectName+ " adlı projenin "+operationType;
                    setLogging(l1,"işlemine bir hatırlatma eklendi",logMessage);
                    System.out.println("==============");
                    System.out.println("==============");
                    System.out.println("l1.getProjectId()");
                    System.out.println(l1.getProjectId());
                    System.out.println("==============");
                    System.out.println("==============");
                    System.out.println("==============");
                    getNewWorkController().SetRemindWorkLogging(l1);





                }
                stageManager.closeDialog();
            } catch (HibernateException | ParseException e) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Hata", "", "Hatırlatma kaydedilirken bir hata oluştu!",
                        new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE), null);
                return;
            }
        }
    }


    private void setLogging(Logging logging, String eventType, String logText) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String dateString = format.format( new Date()   );
        Date date = format.parse(dateString);

        logging.setLoggingText(logText);

        LoggingEventType let1 = loggingEventTypeRepository.findByEventType(eventType);
        logging.setLoggingEventType(let1);
        logging.setLoggingDate(date);
        logging.setUserName(userName);
        logging.setProjectId(projectId);

    }


    @FXML
    public void deleteButtonAction() throws ParseException {
        if(getNewWorkController() != null) {
            getPaper().setRemindWork(null);
            getNewWorkController().getRemindCheckBox().setSelected(false);

            Logging l1 = new Logging();
            String logMessage = projectName+ " adlı projenin "+operationType;
            setLogging(l1,"işleminin hatırlatması silindi",logMessage);
            getNewWorkController().SetRemindWorkLogging(l1);

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
