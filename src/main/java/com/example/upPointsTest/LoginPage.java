package com.example.upPointsTest;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;

import com.example.upPointsTest.UserEditor.EditorSavedEvent;
import com.example.upPointsTest.UserEditor.EditorSavedListener;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import sun.util.logging.resources.logging;

public class LoginPage extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String PERSISTENCE_UNIT = "sellingpoint";
	public static final String NAME = "";
	private JPAContainer<User> Users;

	public LoginPage(){
		Users = JPAContainerFactory.make(User.class, PERSISTENCE_UNIT);
		Panel panel = new Panel("Login");
		panel.setSizeUndefined();
		addComponent(panel);

		
		FormLayout content = new FormLayout();
		TextField username = new TextField("Username");
		content.addComponent(username);
		PasswordField password = new PasswordField("Password");
		content.addComponent(password);

		Button send = new Button("Enter");
		send.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(authenticate(username.getValue(), password.getValue())){
					VaadinSession.getCurrent().setAttribute("user", username.getValue());
					getUI().getNavigator().addView(SellingPointMainView.NAME, SellingPointMainView.class);
					//getUI().getNavigator().addView(OtherSecurePage.NAME, OtherSecurePage.class);
					Page.getCurrent().setUriFragment("!"+SellingPointMainView.NAME);
				}else{
					Notification.show("Invalid credentials", Notification.Type.ERROR_MESSAGE);
				}
			}
			
		});
		Button newButton = new Button("AddUser");
        newButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<User> newUserItem = new BeanItem<User>(
                        new User());
                UserEditor userEditor = new UserEditor(newUserItem);
                userEditor.addListener(new EditorSavedListener() {

					@Override
                    public void editorSaved(EditorSavedEvent event) {
                    	Users.addEntity(newUserItem.getBean());
                    }
                });
                UI.getCurrent().addWindow(userEditor);
            }
        });
		content.addComponent(send);
		content.addComponent(newButton);
		content.setSizeUndefined();
		content.setMargin(true);
		panel.setContent(content);

		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
	
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	public Boolean authenticate(String usernameInput, String passwordInput){
		EntityProvider<User> ep = Users.getEntityProvider();
		EntityManager entityManager = ep.getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> root = cq.from(User.class);
		Path<User> username = root.get("username");
		Path<User> password = root.get("password");

		cq.select(cb.count(root)).where(
		    cb.and(
		        cb.equal(username, usernameInput),
		        cb.equal(password, passwordInput)
		    )
		);

		Long result = entityManager.createQuery(cq).getSingleResult();
		if(result.intValue() > 0){
			return true;
		}
		return false;
	}

}
