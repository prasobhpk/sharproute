package com.sharproute.fix;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

@Component
@Scope("prototype")
public class DefaultApplication implements Application {

	public void fromAdmin(Message arg0, SessionID arg1) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub

	}

	public void fromApp(Message arg0, SessionID arg1) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub

	}

	public void onCreate(SessionID arg0) {
		// TODO Auto-generated method stub

	}

	public void onLogon(SessionID arg0) {
		// TODO Auto-generated method stub

	}

	public void onLogout(SessionID arg0) {
		// TODO Auto-generated method stub

	}

	public void toAdmin(Message arg0, SessionID arg1) {
		// TODO Auto-generated method stub

	}

	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		// TODO Auto-generated method stub

	}

}
