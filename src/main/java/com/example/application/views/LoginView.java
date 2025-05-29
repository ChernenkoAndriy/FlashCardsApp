package com.example.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
//vaadin дозволяє кодувати сторінки як класи. По конвенції вони закінчуються на View

// Ця сторінка буде відображатися при вході користувача (форма логіну)

// Вказує маршрут (URL-адресу), за якою доступна ця сторінка: localhost:8080/login
@Route("login")

// Назва сторінки, яка відображатиметься у вкладці браузера
@PageTitle("Login | SLEEVE")

// Дозволяє доступ до цієї сторінки навіть неавторизованим користувачам
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	// Компонент Vaadin з готовою формою логіну (логін + пароль)
	private final LoginForm loginForm = new LoginForm();

	// Зображення логотипу
	private Image logoImage;

	// Посилання на сторінку реєстрації
	private RouterLink signUpLink;

	// Посилання для відновлення паролю
	private RouterLink forgotPasswordLink;

	public LoginView() {
		configureComponents();
		createLayout();
	}

	private void configureComponents() {
		// Встановлює шлях, на який буде відправлятись форма (POST-запит на /login)
		loginForm.setAction("login");
		loginForm.setForgotPasswordButtonVisible(false);
		logoImage = new Image("icons/Icon.png", "Sleeve icon");

		signUpLink = new RouterLink("Create an account", SignUpView.class);
		forgotPasswordLink = new RouterLink("Forgot password?", ForgotPasswordView.class);
	}

	// Побудова сторінки: стилізація і додавання компонентів у вертикальний лейаут
	private void createLayout() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		logoImage.setHeight("100px");
		add(logoImage, loginForm, signUpLink, forgotPasswordLink);
	}

	// Метод викликається перед тим, як користувач зайде на сторінку
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// Якщо в URL є параметр error (після невдалої спроби входу), показати повідомлення про помилку
		if (event.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			loginForm.setError(true); // Показує повідомлення "Incorrect username or password"
		}
	}
}
