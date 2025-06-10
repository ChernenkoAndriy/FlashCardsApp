package com.example.application.views;

import com.example.application.service.PasswordResetService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("passwordrecovery")
@PageTitle("Password Recovery | SLEEVE")
@AnonymousAllowed
public class PasswordRecoveryView extends VerticalLayout {

    private final PasswordResetService passwordResetService;

    private final Image logoImage = new Image("icons/Icon.png", "Sleeve icon");
    private final H1 header = new H1("Recover your password");
    private final EmailField email = new EmailField("Email");
    private final Button button = new Button("Submit");

    public PasswordRecoveryView(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
        initialize();
        setLayout();
    }

    private void initialize() {
        email.setPlaceholder("Enter your registered email");
        email.setMaxLength(100);
        email.setWidthFull();
        email.setClearButtonVisible(true);

        button.setWidthFull();
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> handleRecovery());
    }

    private void setLayout() {
        logoImage.setHeight("100px");

        VerticalLayout formLayout = new VerticalLayout(
                logoImage, header, email, button
        );
        formLayout.setWidth("25%");
        formLayout.setAlignItems(Alignment.CENTER);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(formLayout);
    }

    private void handleRecovery() {
        String emailValue = email.getValue();

        if (emailValue == null || emailValue.trim().isEmpty()) {
            email.setInvalid(true);
            Notification.show("Please enter your email.", 5000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (!email.isInvalid()) {
            try {
                button.setEnabled(false);
                button.setText("Sending...");

                passwordResetService.initiatePasswordReset(emailValue.trim());

                Notification.show("If the email is registered, you'll receive recovery instructions.",
                                5000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                UI.getCurrent().navigate("login");

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                System.out.println(e.getStackTrace());
                Notification.show("An error occurred. Please try again later.",
                                5000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } finally {
                button.setEnabled(true);
                button.setText("Submit");
            }
        } else {
            Notification.show("Please enter a valid email address.", 5000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}