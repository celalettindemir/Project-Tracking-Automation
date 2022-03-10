package com.celal258.cagoz.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

public class AlertHelper {
    private static Alert alert;

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void showAlert(Alert.AlertType alertType, String title, String header, String body, ButtonType type1, ButtonType type2){
        if(type2 == null){
            alert = new Alert(alertType,body,type1);
        }
        else{
            alert = new Alert(alertType,body,type2,type1);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public static Alert getAlert() {
        return alert;
    }

    public static void setAlert(Alert alert) {
        AlertHelper.alert = alert;
    }
}