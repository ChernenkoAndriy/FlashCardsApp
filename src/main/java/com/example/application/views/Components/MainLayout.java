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

        // Застосування теми на основі збереженого значення у localStorage
        UI.getCurrent().getPage().executeJs("""
            const savedTheme = localStorage.getItem('theme');
            if (savedTheme === 'dark') {
                document.documentElement.setAttribute('theme', 'dark');
                $0.themeList.add('dark');
            }
        """, UI.getCurrent().getElement());
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

        addToNavbar(header);   // метод додає до navbar з appLayout наш кастомний оранжевий елемент нагору
    }

    //метод міняє тему додатку на темну або світлу, і зберігає вибір у localStorage
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

            // Збереження теми в cookie
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


