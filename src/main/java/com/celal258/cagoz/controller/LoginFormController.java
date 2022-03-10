package com.celal258.cagoz.controller;

import com.celal258.cagoz.config.StageManager;
import com.celal258.cagoz.entity.User;
import com.celal258.cagoz.repository.UserRepository;
import com.celal258.cagoz.utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import javafx.stage.Window;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class LoginFormController implements Initializable {
    @FXML
    private TextField kullaniciAdi;

    @FXML
    private PasswordField sifre;

    @FXML
    private Button submitButton;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StageManager stageManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = new User();
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        /*for(User u : userRepository.findAll()){
            System.out.println("geldi");
            if(u.getUsername() == kullaniciAdi.getText()){
                System.out.println("s");
                user.setId(u.getId());
                System.out.println("user id: "+user.getId());
            }
        }*/
        /*if(kullaniciAdi.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Kullanıcı adı kısmını boş bırakmayınız!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
        }
        else if(sifre.getText().isEmpty()) {
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Şifre kısmını boş bırakmayınız!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
        }*/
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(kullaniciAdi.getText(),sifre.getText());

            System.out.println(request.toString());
            Authentication result = authManager.authenticate(request);
            System.out.println(result.toString());
            SecurityContextHolder.getContext().setAuthentication(result);
            //updateUserInfo();

        } catch (InternalAuthenticationServiceException e){
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Kullanıcı adı bulunamadı!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
            return;
        }
        catch (BadCredentialsException e){
            AlertHelper.showAlert(Alert.AlertType.ERROR,"Hata","","Girdiğiniz bilgileri kontrol ediniz!",new ButtonType("Tamam", ButtonBar.ButtonData.OK_DONE),null);
            return;
        }
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails instanceof UserDetails){
            user = userRepository.findByUsername(((UserDetails) userDetails).getUsername());
        }
        stageManager.switchScene("/home.fxml","Ana Sayfa");
        HomeController homeController = stageManager.getLoader().getController();
        homeController.initUser(user.getId());
        /*private void updateUserInfo(){

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            lblUserName.setText(auth.getName());
            List<String> grantedAuthorities = auth.getAuthorities().stream().map( a -> a.toString()).collect(Collectors.toList());
            userRoles.clear();
            userRoles.addAll(grantedAuthorities);

        }*/

        /*AlertHelper.showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + kullaniciAdi.getText());*/
    }
}
