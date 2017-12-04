package net.grammer.samples.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("grammer")
public class MainUI extends UI {

	private static final long serialVersionUID = 1L;

	private final VerticalLayout layout = new VerticalLayout();
	private final Label label = new Label("Здрасте");
	private final TextField text = new TextField();
	
	@Override
	protected void init(VaadinRequest request) {
		layout.addComponents(label, text);
		setContent(layout);
	}
}
