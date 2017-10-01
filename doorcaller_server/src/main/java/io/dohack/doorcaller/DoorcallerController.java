package io.dohack.doorcaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doorcaller")
public class DoorcallerController {

	@Autowired
	DoorcallerComponent dc;
    
	@RequestMapping(value = "/call", method = RequestMethod.GET)
	public ResponseEntity<Object> callUser() {
        dc.call();
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/open", method = RequestMethod.GET)
	public ResponseEntity<Object> openDoor() {
        dc.setStatus(MyCallStatus.OPEN);
		System.out.println("Open Door");
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/shouldOpen", method = RequestMethod.GET)
	public ResponseEntity<Object> shouldOpen() {
		if(dc.getStatus() == MyCallStatus.OPEN) {
			System.out.println("Door Opened");
			dc.setStatus(MyCallStatus.READY);
			return ResponseEntity.status(202).build();
		}
		return ResponseEntity.status(102).build();
	}
}
