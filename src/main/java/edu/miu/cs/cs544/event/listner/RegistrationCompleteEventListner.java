package edu.miu.cs.cs544.event.listner;

import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.event.RegistrationCompleteEvent;
import edu.miu.cs.cs544.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListner implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        //Create the Verification Token for the user with link

        User user = registrationCompleteEvent.getUser();
        String Token = UUID.randomUUID().toString();
        userService.createVerificationToken(user,Token);
        //Send the email
        String url = registrationCompleteEvent.getAppUrl()
                + "/registrationConfirm?token="
                + Token;
        //send verification Email
        log.info("Click the link to verify your account: " + url);

    }
}
