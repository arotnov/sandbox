package com.github.arotnov.vaadin.osgi.websocket;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.Registration;

public class SingleProviderVaadinServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	private final UIProvider uiProvider;
	private Registration sessionInitListenerRegistration;
	private Registration sessionDestroyListenerRegistration;

	public SingleProviderVaadinServlet(UIProvider uiProvider) {
		super();
		this.uiProvider = uiProvider;
	}
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	@Override
	protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		VaadinServletService vaadinServletService = new VaadinServletService(this, deploymentConfiguration);
		sessionInitListenerRegistration = vaadinServletService
				.addSessionInitListener((event) -> event.getSession().addUIProvider(uiProvider));
		sessionDestroyListenerRegistration = vaadinServletService
				.addSessionDestroyListener((event) -> event.getSession().removeUIProvider(uiProvider));
		vaadinServletService.init();
		return vaadinServletService;
	}

	@Override
	public void destroy() {
		super.destroy();
		sessionInitListenerRegistration.remove();
		sessionInitListenerRegistration = null;
		sessionDestroyListenerRegistration.remove();
		sessionDestroyListenerRegistration = null;
	}
}
