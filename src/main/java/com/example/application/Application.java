package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
//підключає css файли в папці з таким ім'ям. там будуть робитись деякі речі по стилізації
@Theme(value = "customization")
//ця анотація типу як дозволяє робити сайт завантажуваним на пк
//я хуй знає як це працює, але очевидно що воно конфігурує тут деякі речі
@PWA(
        name = "Sleeve", //ім'я додатку
        shortName = "SLV",
        offlinePath="offline.html", //сторінка коли немає інтернету
        offlineResources = { "images/offline.png" } //вказує що малюнок з оленем на офлайн сторінці мусить бути доступним в оффлайні
)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
