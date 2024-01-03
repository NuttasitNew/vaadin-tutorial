package com.example.application.views.list;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

import static com.vaadin.flow.component.ComponentUtil.addListener;


public class ContactForm extends FormLayout {
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Close");
    private Contact contact;

    public ContactForm(List<Company> companyList, List<Status> statusList){
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        company.setItems(companyList);
        company.setItemLabelGenerator(Company::getName);

        status.setItems(statusList);
        status.setItemLabelGenerator(Status::getName);
        
        add(
                firstName,
                lastName,
                email,
                company,
                status,
                createButtonsLayout()
        );

    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(Class<DeleteEvent> deleteEventClass, ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(Class<SaveEvent> saveEventClass, ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(Class<CloseEvent> closeEventClass, ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

    public void setContact(Contact contact){
        this.contact = contact;
        binder.readBean(contact);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try{
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this,contact));
        }catch (ValidationException e){
            e.printStackTrace();
        }
        
        
    }
}