package com.github.arotnov.vaadin.osgi.websocket;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.vaadin.annotations.Push;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Push(transport = Transport.WEBSOCKET, value = PushMode.AUTOMATIC)
@PushStateNavigation
public class TestUI extends UI implements Listener {

	private static final long serialVersionUID = 1L;

	private final EventDispatcher dispatcher;
	
	private final TextField sendField = new TextField("Send Message");
	private final TextField receivedField = new TextField("Received Message");
	
	private final TextField maxInactiveInterval = new TextField("Max Inactive Interval");
	private final TextField creationTime = new TextField("Creation Time");
	private final TextField lastAccessedTime = new TextField("Last Accessed Time");
	private final TextField lastRequestTimestamp = new TextField("Last Request Timestamp");
	
	private final Button invalidateButton = new Button("Invalidate");
	
	private final VerticalLayout inputs = new VerticalLayout(sendField, 
			receivedField, 
			invalidateButton);
	private final VerticalLayout info = new VerticalLayout(creationTime, maxInactiveInterval, 
			lastAccessedTime, 
			lastRequestTimestamp);
	private final HorizontalLayout main = new HorizontalLayout(inputs, info);
	
	public TestUI(EventDispatcher dispatcher) {
		super();
		this.dispatcher = dispatcher;
	}

	@Override
	protected void init(VaadinRequest request) {
		dispatcher.addListener(this);		
		
		sendField.addValueChangeListener(event -> dispatcher.dispatch(event.getValue()));
		receivedField.setReadOnly(true);
		invalidateButton.addClickListener(event -> VaadinServletService.getCurrentServletRequest().getSession().invalidate());
		
		maxInactiveInterval.setWidth(250, Unit.PIXELS);
		maxInactiveInterval.setReadOnly(true);
		creationTime.setWidth(250, Unit.PIXELS);
		creationTime.setReadOnly(true);
		lastAccessedTime.setWidth(250, Unit.PIXELS);
		lastAccessedTime.setReadOnly(true);
		lastRequestTimestamp.setWidth(250, Unit.PIXELS);
		lastRequestTimestamp.setReadOnly(true);
		
		updateSessionInfo();
		setContent(main);
	}

	@Override
	public void detach() {
		super.detach();
		dispatcher.removeListener(this);
	}
	
	private void updateSessionInfo() {
		HttpSession session = VaadinServletService.getCurrentServletRequest().getSession();
		int maxInactiveInterval = session.getMaxInactiveInterval();
		Date creationTime = new Date(session.getCreationTime());
		Date lastAccessedTime = new Date(session.getLastAccessedTime());
		Date lastRequestTimestamp = new Date(VaadinSession.getCurrent().getLastRequestTimestamp());
		
		
		this.maxInactiveInterval.setValue(String.valueOf(maxInactiveInterval));
		this.creationTime.setValue(creationTime.toString());
		this.lastAccessedTime.setValue(lastAccessedTime.toString());
		this.lastRequestTimestamp.setValue(lastRequestTimestamp.toString());
	}

	@Override
	public void onEvent(String value) {
		access(() -> {
			this.receivedField.setValue(value);	
			updateSessionInfo();
		});
	}
}
