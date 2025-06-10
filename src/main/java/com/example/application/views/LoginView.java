package com.example.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | SLEEVE")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	private final LoginForm loginForm = new LoginForm();
	private Image logoImage;
	private RouterLink signUpLink;
	private RouterLink passwordRecoveryLink;

	public LoginView() {
		configureComponents();
		createLayout();
	}

	private void configureComponents() {
		loginForm.setAction("login");
		loginForm.setForgotPasswordButtonVisible(false);
		logoImage = new Image("icons/Icon.png", "Sleeve icon");
		signUpLink = new RouterLink("Create an account", SignUpView.class);
		passwordRecoveryLink = new RouterLink("Forgot password?", PasswordRecoveryView.class);
	}
	private void createLayout() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		logoImage.setHeight("100px");
		add(logoImage, loginForm);
		add(signUpLink) ;
		add(passwordRecoveryLink);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (event.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			loginForm.setError(true);
		}
	}
}
