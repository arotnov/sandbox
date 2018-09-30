package com.github.arotnov.vaadin.osgi.websocket;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.vaadin.server.Constants;
import com.vaadin.server.DefaultDeploymentConfiguration;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Component(immediate = true, service = VaadinServletRegistration.class)
public class VaadinServletRegistration {

	private final EventDispatcher dispatcher = new EventDispatcher();
	private ServiceRegistration<VaadinServlet> registration;
	
	@Activate
	void activate(BundleContext context) {
		
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME, "Test");
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "/*");
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED, true);
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX + Constants.SERVLET_PARAMETER_PRODUCTION_MODE, false);
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX + Constants.SERVLET_PARAMETER_HEARTBEAT_INTERVAL, 10);
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX + Constants.SERVLET_PARAMETER_CLOSE_IDLE_SESSIONS, DefaultDeploymentConfiguration.DEFAULT_CLOSE_IDLE_SESSIONS);
		properties.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX + "org.atmosphere.websocket.maxIdleTime", 30000);
		
		
		UIProvider uiProvider = new UIProvider() {
			private static final long serialVersionUID = 1L;
			@Override
			public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
				return TestUI.class;
			}
			@Override
			public UI createInstance(UICreateEvent event) {
				return new TestUI(dispatcher);
			}
		};
		VaadinServlet servlet = new SingleProviderVaadinServlet(uiProvider);
		registration = context.registerService(VaadinServlet.class, servlet, properties);
	}

	@Deactivate
	void deactivate() {
		registration.unregister();
		registration = null;
	}
}
