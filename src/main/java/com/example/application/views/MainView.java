package com.example.application.views;

import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

//vaadin дозволяє кодувати сторінки як класи. По конвенції вони закінчуються на View
//це буде домашня сторінка на яку потрапить користувач після авторизації
//ця аннотація контролює доступ до сторінки
//вона дозволяє заходити лише авторизованим користувачам з ролями користувача або адміна
@RolesAllowed({"ADMIN", "USER"})
//встановлює пустий url(бо сторінка домашня) і говорить що використовується MainLayout
//тобто ця сторінка додається до сторінки в MainLayout яку я написав раніше
@Route(value = "", layout = MainLayout.class)
//назва вкладки
@PageTitle("Home | SLEEVE")

public class MainView extends VerticalLayout {
    public MainView() {
        H1 h1 = new H1("Welcome! Let`s learn some words!");
        h1.addClassName("banner");
        h1.getStyle().set("font-size", "300%");
        setAlignItems(Alignment.CENTER);
        add(h1);
    }
}