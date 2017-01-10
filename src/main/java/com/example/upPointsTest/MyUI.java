package com.example.upPointsTest;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.Notification;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */

@Theme("valo")
public class MyUI extends UI {
	
//	static {
//        DemoDataGenerator.create();
//    }
	
	public static Authentication AUTH;

    @Override
    protected void init(VaadinRequest request) {
    	AUTH = new Authentication();
    	new Navigator(this, this);
		
		getNavigator().addView(LoginPage.NAME, LoginPage.class);
		getNavigator().setErrorView(LoginPage.class);
		
		Page.getCurrent().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				router(event.getUriFragment());
			}
		});
		
		
		router("");
        //setContent(new SellingPointMainView());
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
    //@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
    
    private void router(String route){
		Notification.show(route);
		if(getSession().getAttribute("user") != null){
			getNavigator().addView(SellingPointMainView.NAME, SellingPointMainView.class);
			//getNavigator().addView(OtherSecurePage.NAME, OtherSecurePage.class);
			//if(route.equals("!OtherSecure")){
			getNavigator().navigateTo(SellingPointMainView.NAME);
			//}else{
			//	getNavigator().navigateTo(SecurePage.NAME);
			//}
		}else{
			getNavigator().navigateTo(LoginPage.NAME);
		}
	}
}
