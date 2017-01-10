package com.example.upPointsTest;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalSplitPanel;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.example.upPointsTest.SellingPointEditor.EditorSavedEvent;
import com.example.upPointsTest.SellingPointEditor.EditorSavedListener;
//import com.example.upPointsTest.sellingpoint.domain.SellingPoint;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class SellingPointMainView extends HorizontalSplitPanel implements View, ComponentContainer {
	public static final String PERSISTENCE_UNIT = "sellingpoint";
	public static final String NAME = "SellingPoint";
	
	private Tree groupTree;

    private Table sellingPointTable;

    private TextField searchField;

    private Button newButton;
    private Button deleteButton;
    private Button editButton;

    private JPAContainer<SellingPoint> sellingPoints;

    private SellingPoint sellingPointFilter;
    private String textFilter;

    public SellingPointMainView() {
        sellingPoints = JPAContainerFactory.make(SellingPoint.class, PERSISTENCE_UNIT);
        buildTree();
        buildMainArea();

        setSplitPosition(30);
    }

    private void buildMainArea() {
        VerticalLayout verticalLayout = new VerticalLayout();
        setSecondComponent(verticalLayout);

        sellingPointTable = new Table(null, sellingPoints);
        sellingPointTable.setSelectable(true);
        sellingPointTable.setImmediate(true);
        sellingPointTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });

        sellingPointTable.setSizeFull();
        // personTable.setSelectable(true);
        sellingPointTable.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                	sellingPointTable.select(event.getItemId());
                }
            }
        });

        sellingPointTable.setVisibleColumns(new Object[] { "name", "fone",
                "address", "workingHours" });

        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Add");
        newButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<SellingPoint> newSellingPointItem = new BeanItem<SellingPoint>(
                        new SellingPoint());
                SellingPointEditor personEditor = new SellingPointEditor(newSellingPointItem);
                personEditor.addListener(new EditorSavedListener() {

					@Override
                    public void editorSaved(EditorSavedEvent event) {
                    	sellingPoints.addEntity(newSellingPointItem.getBean());
                    }
                });
                UI.getCurrent().addWindow(personEditor);
            }
        });

        deleteButton = new Button("Delete");
        deleteButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	sellingPoints.removeItem(sellingPointTable.getValue());
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Edit");
        editButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(
                        new SellingPointEditor(sellingPointTable.getItem(sellingPointTable
                                .getValue())));
            }
        });
        editButton.setEnabled(false);

        searchField = new TextField();
        searchField.setInputPrompt("Search by name");
        searchField.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {
                textFilter = event.getText();
                updateFilters();
            }
        });

        toolbar.addComponent(newButton);
        toolbar.addComponent(deleteButton);
        toolbar.addComponent(editButton);
        toolbar.addComponent(searchField);
        toolbar.setWidth("100%");
        toolbar.setExpandRatio(searchField, 1);
        toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

        verticalLayout.addComponent(toolbar);
        verticalLayout.addComponent(sellingPointTable);
        verticalLayout.setExpandRatio(sellingPointTable, 1);
        verticalLayout.setSizeFull();

    }
    
    private void buildTree() {
        groupTree = new Tree(null, sellingPoints);
        groupTree.setItemCaptionPropertyId("name");

        groupTree.setImmediate(true);
        groupTree.setSelectable(true);
        groupTree.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Object id = event.getProperty().getValue();
                if (id != null) {
                	SellingPoint entity = sellingPoints.getItem(id).getEntity();
                	sellingPointFilter = entity;
                } else if (sellingPointFilter != null) {
                	sellingPointFilter = null;
                }
                updateFilters();
            }

        });
        setFirstComponent(groupTree);
    }
    
    private void updateFilters() {
    	sellingPoints.setApplyFiltersImmediately(false);
    	sellingPoints.removeAllContainerFilters();
        if (textFilter != null && !sellingPointFilter.equals("")) {
            Or or = new Or(new Like("name", textFilter + "%", false),
                    new Like("fone", textFilter + "%", false));
            sellingPoints.addContainerFilter(or);
        }
        sellingPoints.applyFilters();
    }
    
    @Override
	public void enter(ViewChangeEvent event) {	
	}
}
