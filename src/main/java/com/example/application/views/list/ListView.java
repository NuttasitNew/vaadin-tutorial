package com.example.application.views.list;

import com.example.application.data.Contact;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("list")
@Route(value = "")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    public ListView() {
        addClassName("list-view");
        setSizeFull();

        configureGrid();
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
    }

}
