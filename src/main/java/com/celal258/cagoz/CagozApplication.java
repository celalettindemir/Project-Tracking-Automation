package com.celal258.cagoz;

import com.celal258.cagoz.config.StageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;


@SpringBootApplication
public class CagozApplication extends Application {
	private ConfigurableApplicationContext springContext;
	private Parent rootNode;
	private FXMLLoader fxmlLoader;

	@Autowired
	private StageManager stageManager;


	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(CagozApplication.class);
		fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(springContext::getBean);
		initSecurity();
	}
	@Override
	public void start(Stage primaryStage) throws Exception{

		stageManager = springContext.getBean(StageManager.class);

		stageManager.setContext(springContext);
		stageManager.setPrimaryStage(primaryStage);
		//stageManager=new StageManager(fxmlLoader,primaryStage);
		stageManager.switchScene("/login_form.fxml","Hello World");

		/*fxmlLoader.setLocation(getClass().getResource("/home.fxml"));
		rootNode = fxmlLoader.load();

		primaryStage.setTitle("Hello World");
		Scene scene = new Scene(rootNode, 1280, 768);
		primaryStage.setScene(scene);
		primaryStage.show();*/
	}
	@Override
	public void stop() {
		springContext.stop();
		System.exit(SpringApplication.exit(springContext));
	}
	public static void initSecurity() {
		SecurityContextHolder.setStrategyName("MODE_GLOBAL");
		initAnonymous();
	}
	public static void initAnonymous() {
		AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
				"anonymous", "anonymous",
				AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

		SecurityContextHolder.getContext().setAuthentication(auth);
	}


	public static void logout(){
		SecurityContextHolder.clearContext();
		initAnonymous();
	}
}
