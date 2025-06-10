package com.example.application.views;
import com.example.application.data.Language;
import com.example.application.data.User;
import com.example.application.service.LanguageService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


import java.util.List;

@Route("signup")
@PageTitle("Sign Up | SLEEVE")
@AnonymousAllowed
public class SignUpView extends VerticalLayout {
    private final UserService userService;
    private final LanguageService languageService;
    private final Binder<User> binder = new Binder<>(User.class);
    private Image logoImage = new Image("icons/Icon.png", "Sleeve icon");
    private final H1 header = new H1("Create new account");
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final TextField email = new TextField();
    private final ComboBox<Language> languageComboBox = new ComboBox<>();
    private final Button button = new Button("Sign Up");
    public SignUpView(LanguageService languageService, UserService userService) {
        this.languageService = languageService;
        this.userService = userService;
        initialize();
        setLayout();
    }
    private void initialize() {
        List<Language> languages = languageService.findAll();
        languageComboBox.setItems(languages);
        languageComboBox.setItemLabelGenerator(Language::getName);
        languageComboBox.setAllowCustomValue(false);

        username.setMaxLength(50);
        binder.forField(username)
                .asRequired("Username is required")
                .withValidator(name -> name.length() >= 3, "Username must be at least 3 characters long")
                .withValidator(name -> name.length() <= 50, "Username must be at most 50 characters long")
                .bind(User::getUsername, User::setUsername);

        email.setMaxLength(100);
        binder.forField(email)
                .asRequired("Email is required")
                .withValidator(e -> e.length() <= 100, "Email must be at most 100 characters long")
                .withValidator(e -> e.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"), "Invalid email format")
                .bind(User::getEmail, User::setEmail);

        password.setMaxLength(255);
        binder.forField(password)
                .asRequired("Password is required")
                .withValidator(pwd -> pwd.length() >= 6, "Password must be at least 6 characters long")
                .withValidator(pwd -> pwd.length() <= 255, "Password must be at most 255 characters long")
                .bind(User::getPassword, User::setPassword);

        languageComboBox.setMaxWidth("100%");
        binder.forField(languageComboBox)
                .asRequired("Language is required")
                .withConverter(Language::getName, name ->
                                languageService.findAll().stream()
                                        .filter(lang -> lang.getName().equals(name))
                                        .findFirst()
                                        .orElse(null),
                        "Invalid language")
                .bind(User::getLanguage, User::setLanguage);

        button.addClickListener(event -> {
            signUp();
        });
    }
    private void setLayout() {
        username.setLabel("Username");
        password.setLabel("Password");
        email.setLabel("Email");
        languageComboBox.setLabel("Native Language");
        logoImage.setHeight("100px");
        username.setWidthFull();
        password.setWidthFull();
        email.setWidthFull();
        languageComboBox.setWidthFull();
        button.setWidthFull();

        VerticalLayout formLayout = new VerticalLayout(
               logoImage, header, username, password, email, languageComboBox, button
        );
        formLayout.setWidth("25%");
        formLayout.setAlignItems(Alignment.CENTER);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(formLayout);
    }
    private void signUp() {
        try {
            if (binder.isValid()) {
                User user = new User();
                if (binder.writeBeanIfValid(user)) {
                    user.setWorkload(0);
                    userService.save(user);
                    binder.setBean(new User());
                    UI.getCurrent().navigate("login");
                    Notification.show("Account created. Please, log in.").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            } else {
                binder.validate();
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                if (e.getMessage().contains("key 'user.username'")) {
                    notifyError("Such username is already registered. Please, choose another one.");
                    username.setInvalid(true);
                } else if (e.getMessage().contains("key 'user.email'")) {
                    notifyError("Such email is already registered.");
                    email.setInvalid(true);
                }
            } else {
                notifyError("Error saving user");
            }
        }
    }

    private void notifyError(String errorMessage) {
        com.vaadin.flow.component.notification.Notification.show(errorMessage, 6000, Notification.Position.BOTTOM_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

}

