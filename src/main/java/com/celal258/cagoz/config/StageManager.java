package com.celal258.cagoz.config;


import static org.slf4j.LoggerFactory.getLogger;

import java.util.*;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = true)
public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private Stage primaryStage;
    private Stack<Stage> currentDialogStage;
    private ResourceBundle resourceBundle;
    private ApplicationContext context;
    private Parent rootNode;
    private FXMLLoader loader;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getCurrentDialogStage() {
        return currentDialogStage.peek();
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public StageManager() {
        currentDialogStage=new Stack<Stage>();
    }

    public void switchScene(String fxml,String title) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(fxml);
        show(viewRootNodeHierarchy, title);
    }

    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        //scene.getStylesheets().add("/styles/Styles.css");

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    public FXMLLoader showDialog( String fxml,String title) {
        FXMLLoader tempLoader=loader;
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(fxml);
        FXMLLoader returnLoader=loader;
        loader=tempLoader;
        Stage dialog = new Stage();
        currentDialogStage.push(dialog);
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                currentDialogStage.pop();
            }
        });
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);
        dialog.sizeToScene();
        dialog.centerOnScreen();
        dialog.setScene(new Scene(viewRootNodeHierarchy));
        try {
            dialog.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }

        return returnLoader;
    }
    public void closeDialog() {
        if(currentDialogStage.size()!=0)
            currentDialogStage.pop().close();
    }
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean); //Spring now FXML Controller Factory
            loader.setResources(resourceBundle);
            loader.setLocation(getClass().getResource(fxmlFilePath));
            rootNode = loader.load();
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }


    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }
}
