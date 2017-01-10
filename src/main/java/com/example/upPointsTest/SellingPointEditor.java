package com.example.upPointsTest;

import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Window;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;

public class SellingPointEditor extends Window implements FormFieldFactory, ClickListener {
	
	private final Item sellingPointItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;
    
    public SellingPointEditor(Item sellingPointItem) {
        this.sellingPointItem = sellingPointItem;
        editorForm = new Form();
        editorForm.setFormFieldFactory(this);
        editorForm.setBuffered(true);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(sellingPointItem, Arrays.asList("name", "fone",
                "address", "workingHours"));

        saveButton = new Button("Save", this);
        cancelButton = new Button("Cancel", this);

        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        setSizeUndefined();
        setContent(editorForm);
        //setCaption(buildCaption());
    }

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == saveButton) {
            editorForm.commit();
            fireEvent(new EditorSavedEvent(this, sellingPointItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();

	}

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		Field field = DefaultFieldFactory.get().createField(item, propertyId,
                uiContext);
        ((TextField) field).setNullRepresentation("");

        field.addValidator(new BeanValidator(SellingPoint.class, propertyId
                .toString()));

        return field;
	}
	
	public void addListener(EditorSavedListener listener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }
	
	public void removeListener(EditorSavedListener listener) {
        removeListener(EditorSavedEvent.class, listener);
    }
	
	public static class EditorSavedEvent extends Component.Event {

        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }
	
	public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }

}
