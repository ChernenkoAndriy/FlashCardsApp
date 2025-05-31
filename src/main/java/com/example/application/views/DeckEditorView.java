package com.example.application.views;

import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | SLEEVE")
public class DeckEditorView extends VerticalLayout {

}
