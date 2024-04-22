package edu.miu.cs.cs544.advice.reservation;

import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReservationCheckAspect {

    @Autowired
    private UserRepository userRepository;

    @Before("execution(* edu.miu.cs.cs544.service.ReservationService.getReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.getAllReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.updateReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.deleteReservation(..))")
    public void checkUserReservation(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication;
//        User user = userRepository.findByEmail(email);
//        System.out.println("Email: "+email);
        System.out.println("User: "+authentication);
//        if (user == null || !user.getEmail().equals(email)) {
//            throw new SecurityException("User is not authorized to perform this operation");
//        }
    }
}