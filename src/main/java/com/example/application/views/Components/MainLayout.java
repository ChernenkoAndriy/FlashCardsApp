package com.example.application.views.Components;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.PostConstruct;

public class MainLayout extends AppLayout implements RouterLayout {
    private final SecurityService securityService;
    private H3 titleLabel;
    private Image logoImage;
    private Button logoutButton;
    private Button themeToggleButton;
    private Button backButton;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        logoImage = new Image("icons/Logo.png", "Sleeve logo");
        titleLabel = new H3("Sleeve");
        logoutButton = new VioletButton("Log out");
        themeToggleButton = new VioletButton("Toggle Theme");
        backButton = new VioletButton("Back");

        configureContent();
        configureLayout();

        UI.getCurrent().getPage().executeJs("""
            const savedTheme = localStorage.getItem('theme');
            if (savedTheme === 'dark') {
                document.documentElement.setAttribute('theme', 'dark');
                $0.themeList.add('dark');
            }
        """, UI.getCurrent().getElement());
    }

    private void configureContent() {
        logoutButton.addClickListener(e -> securityService.logout());

        themeToggleButton.addClickListener(e -> toggleTheme());

        backButton.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());

        // Натискання на логотип — перехід на домашню сторінку
        logoImage.getElement().getStyle().set("cursor", "pointer");  // змінюємо курсор на pointer
        logoImage.addClickListener(e -> UI.getCurrent().navigate(""));  // або UI.getCurrent().navigate(HomeView.class);
    }

    private void configureLayout() {
        HorizontalLayout leftSection = new HorizontalLayout(logoImage, titleLabel);
        leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
        leftSection.setSpacing(true);

        HorizontalLayout rightSection = new HorizontalLayout(backButton, themeToggleButton, logoutButton);
        rightSection.setAlignItems(FlexComponent.Alignment.CENTER);
        rightSection.setSpacing(true);

        HorizontalLayout header = new HorizontalLayout(leftSection, rightSection);
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

        addToNavbar(header);
    }


    private void toggleTheme() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        boolean darkMode;

        if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK);
            darkMode = false;
        } else {
            themeList.add(Lumo.DARK);
            darkMode = true;
        }

        UI.getCurrent().getPage().executeJs(
                "document.cookie = 'darkTheme=' + $0 + '; path=/; max-age=31536000'", darkMode
        );
    }

    @PostConstruct
    private void applySavedTheme() {
        UI.getCurrent().getPage().executeJs(
                "return document.cookie;"
        ).then(String.class, cookies -> {
            if (cookies != null && cookies.contains("darkTheme=true")) {
                ThemeList themeList = UI.getCurrent().getElement().getThemeList();
                themeList.add(Lumo.DARK);
            }
        });
    }
}
