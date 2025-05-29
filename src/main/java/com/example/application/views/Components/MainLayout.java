package com.example.application.views.Components;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;

//Цей клас кодує по суті оранжевий заголовок над іншими сторінками, тобто інші сторінки будуть вставлені в неї
public class MainLayout extends AppLayout implements RouterLayout {
    //цей сервіс потрібен щоб розлогінити користувача
    private final SecurityService securityService;
    //елементи заголовку
    private H3 titleLabel;
    private Image logoImage;
    private Button logoutButton;
    private Button themeToggleButton;
    //інжектиться сервіс
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        logoImage = new Image("icons/Logo.png", "Sleeve logo");
        titleLabel = new H3("Sleeve");
        logoutButton = new VioletButton("Log out");
        themeToggleButton = new VioletButton("Toggle Theme");
        configureContent();
        configureLayout();
    }
    //тут назначаються методи на кнопки
    private void configureContent() {
        logoutButton.addClickListener(e -> securityService.logout());
        themeToggleButton.addClickListener(e -> toggleTheme());
    }

    //тут просто визначаються кольори, положення і стиль
    private void configureLayout() {
        HorizontalLayout leftSection = new HorizontalLayout(logoImage, titleLabel);
        leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout header = new HorizontalLayout(leftSection, themeToggleButton, logoutButton);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(leftSection);
        header.setHeight("55px");
        header.getStyle()
                .set("background-color", "#ff9100")
                .set("padding-left", "10px")
                .set("padding-right", "10px");
        titleLabel.getStyle().set("color", "white");
        logoImage.setHeight("30px");
        logoImage.getStyle().set("margin-right", "10px");
        addToNavbar(header);   // це метод applayout, який вже готовий в бібліотеці.
        // метод додає до navbar з appLayout наш кастомний оранжевий елемент нагору
    }
    //метод міняє тему додатку на темну, в подальшому треба придумати як фіксувати результат
    private void toggleTheme() {
        UI ui = UI.getCurrent();
        boolean isDark = ui.getElement().getThemeList().contains("dark");
        ui.getElement().getThemeList().set("dark", !isDark);
    }
}
