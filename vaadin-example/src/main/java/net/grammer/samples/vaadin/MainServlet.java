package net.grammer.samples.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(asyncSupported = true, urlPatterns = { "/*" })
@VaadinServletConfiguration(ui=MainUI.class, productionMode = false)
public class MainServlet extends VaadinServlet {
	private static final long serialVersionUID = 1L;
}
