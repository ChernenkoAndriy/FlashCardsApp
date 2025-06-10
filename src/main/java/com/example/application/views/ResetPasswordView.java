package com.example.application.views;

import com.example.application.service.PasswordResetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Route("resetpassword")
@AnonymousAllowed
public class ResetPasswordView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    @Autowired
    private PasswordResetService passwordResetService;
    private final Image logoImage = new Image("icons/Icon.png", "Sleeve icon");
    private String token;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    private Button resetButton;
    private VerticalLayout formLayout;
    private VerticalLayout errorLayout;

    public ResetPasswordView() {
        createUI();
    }

    private void createUI() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        logoImage.setHeight("100px");
        logoImage.setWidth("100px");
        // Error layout (initially hidden)
        errorLayout = new VerticalLayout();
        errorLayout.setVisible(false);
        errorLayout.setAlignItems(Alignment.CENTER);
        errorLayout.setWidth("400px");

        // Form layout
        formLayout = new VerticalLayout();
        formLayout.setWidth("400px");
        formLayout.setAlignItems(Alignment.CENTER);
        formLayout.setPadding(true);
        formLayout.setSpacing(true);

        H2 title = new H2("Reset Password");
        title.getStyle().set("text-align", "center");

        newPasswordField = new PasswordField("New Password");
        newPasswordField.setRequired(true);
        newPasswordField.setMinLength(6);
        newPasswordField.setErrorMessage("Password must be at least 6 characters long");
        newPasswordField.setSizeFull();
        confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setSizeFull();
        resetButton = new Button("Reset Password");
        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.addClickListener(e -> handlePasswordReset());
        resetButton.setWidthFull();
        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
        backToLoginButton.setWidthFull();
        formLayout.add(logoImage, title, newPasswordField, confirmPasswordField, resetButton, backToLoginButton);
        add(errorLayout, formLayout);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        // Цей метод викликається для URL параметрів типу /resetpassword/token
        this.token = parameter;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Обробляємо query параметри
        Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();

        if (parameters.containsKey("token")) {
            this.token = parameters.get("token").get(0);
        }

        // Якщо токен не знайдено, показуємо помилку
        if (token == null || token.trim().isEmpty()) {
            showError("Invalid password reset link. Please request a new password reset.");
            return;
        }

        // Перевіряємо валідність токена
        if (!passwordResetService.isValidToken(token)) {
            showError("This password reset link is invalid or has expired. Please request a new password reset.");
        }
    }

    private void handlePasswordReset() {
        String newPassword = newPasswordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        // Валідація
        if (newPassword == null || newPassword.trim().isEmpty()) {
            showNotification("Password cannot be empty", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (newPassword.length() < 6) {
            showNotification("Password must be at least 6 characters long", NotificationVariant.LUMO_ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showNotification("Passwords do not match", NotificationVariant.LUMO_ERROR);

            return;
        }

        // Спроба скинути пароль
        try {
            boolean success = passwordResetService.resetPassword(token, newPassword);
            if (success) {
                showNotification("Password reset successfully! You can now login with your new password.", NotificationVariant.LUMO_SUCCESS);

                // Перенаправляємо на сторінку логіну через 2 секунди
                getUI().ifPresent(ui -> {
                    ui.access(() -> {
                        try {
                            Thread.sleep(2000);
                            ui.navigate(LoginView.class);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                });
            } else {
                showNotification("Password reset failed. The link may be invalid or expired.", NotificationVariant.LUMO_ERROR);
            }
        } catch (Exception e) {
            showNotification("An error occurred while resetting your password. Please try again.", NotificationVariant.LUMO_ERROR);
        }
    }

    private void showError(String message) {
        errorLayout.removeAll();
        errorLayout.setVisible(true);
        formLayout.setVisible(false);

        H2 errorTitle = new H2("Error");
        errorTitle.getStyle().set("color", "var(--lumo-error-color)");
        errorTitle.getStyle().set("text-align", "center");

        Paragraph errorMessage = new Paragraph(message);
        errorMessage.getStyle().set("text-align", "center");

        Button requestNewResetButton = new Button("Request New Password Reset");
        requestNewResetButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("passwordrecovery")));

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));

        errorLayout.add(errorTitle, errorMessage, requestNewResetButton, backToLoginButton);
        errorLayout.setAlignItems(Alignment.CENTER);
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification notification = Notification.show(message, 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(variant);
    }
}