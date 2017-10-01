package io.dohack.doorcaller;

import java.net.URI;

import org.springframework.stereotype.Component;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Call.Status;
import com.twilio.type.PhoneNumber;

@Component
public class DoorcallerComponent {             
	private static final String ACCOUNT_SID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String AUTH_TOKEN = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    
    private MyCallStatus status = MyCallStatus.READY;
    private Call callUser = null;
    private Call callDoor = null;
	TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
    
    public void call() {
    	setStatus(MyCallStatus.WAITING);
    	
    	if(callUser != null) {
    		callUser = Call.fetcher(callUser.getSid()).fetch(client);
    	}
		if(callUser == null || callUser.getStatus() == Status.COMPLETED || callUser.getStatus() == Status.CANCELED || callUser.getStatus() == Status.BUSY || callUser.getStatus() == Status.FAILED || callUser.getStatus() == Status.NO_ANSWER) {
	        PhoneNumber user = new PhoneNumber("+49xxxxxxxxxxx");
	        PhoneNumber door = new PhoneNumber("+49xxxxxxxxxxx");
	        PhoneNumber from = new PhoneNumber("+49xxxxxxxxxxx");
	        URI uriUser = URI.create("https://handler.twilio.com/twiml/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	        URI uriDoor = URI.create("https://handler.twilio.com/twiml/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	
	        callUser = Call.creator(user, from, uriUser).create(client);
	        callDoor = Call.creator(door, from, uriDoor).create(client);

	        System.out.println("Call created");
		}
    }
    
    public void setStatus(MyCallStatus status) {
    	this.status = status;
    }
    
    public MyCallStatus getStatus() {
    	return status;
    }
}
