package edu.miu.cs.cs544.config;

import edu.miu.cs.cs544.domain.CustomError;
import edu.miu.cs.cs544.repository.UserRepository;
import edu.miu.cs.cs544.domain.RoleType;
import edu.miu.cs.cs544.domain.User;
import edu.miu.cs.cs544.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminCheckAspect {

    @Autowired
    private UserService userService;

    @Before("execution(* edu.miu.cs.cs544.service.ProductService.addProduct(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ProductService.updateProduct(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ProductService.deleteProduct(..))")
    public void checkAdminUser(JoinPoint joinPoint) throws CustomError {
        System.out.println(joinPoint.getSignature().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String email = (String) jwtAuthenticationToken.getTokenAttributes().get("email");
            System.out.println("email: " + email);
            User user = userService.findUserByEmail(email);
            if (user == null || user.getRoleType() != RoleType.ADMIN) {
                throw new CustomError("You are not authorized to perform this action");
            }
        }

    }

    @Before("execution(* edu.miu.cs.cs544.service.ProductService.getAllProducts(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ProductService.getProduct(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ProductService.getAllAvailableProducts(..))"+
            "execution(* edu.miu.cs.cs544.service.ReservationService.getReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.getAllReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.updateReservation(..)) || " +
            "execution(* edu.miu.cs.cs544.service.ReservationService.deleteReservation(..))")

    public void checkUser(JoinPoint joinPoint) throws CustomError {
        System.out.println(joinPoint.getSignature().getName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String email = (String) jwtAuthenticationToken.getTokenAttributes().get("email");
            System.out.println("email: " + email);
            User user = userService.findUserByEmail(email);
            if (user == null || (user.getRoleType() != RoleType.ADMIN && !user.getEmail().equals(email))) {
                throw new CustomError("You are not authorized to perform this action");
            }
        }

    }

}