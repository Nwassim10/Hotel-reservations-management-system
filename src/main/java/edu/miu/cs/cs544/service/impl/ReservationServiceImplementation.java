package edu.miu.cs.cs544.service.impl;

import edu.miu.cs.cs544.domain.*;
import edu.miu.cs.cs544.domain.adapter.ReservationAdapter;
import edu.miu.cs.cs544.domain.dto.ReservationDTO;
import edu.miu.cs.cs544.repository.CustomerRepository;
import edu.miu.cs.cs544.repository.ProductRepository;
import edu.miu.cs.cs544.repository.ReservationRepository;
import edu.miu.cs.cs544.repository.UserRepository;
import edu.miu.cs.cs544.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true)
@Service
public class ReservationServiceImplementation implements ReservationService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public ReservationServiceImplementation(CustomerRepository customerRepository,
                                            ReservationRepository reservationRepository,
                                            ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) throws CustomError {
        String email = getEmailFromAuthentication();
        Customer customer = customerRepository.findByEmail(email);
        System.out.println(customer);
        if (customer == null) {
            throw new CustomError(email + " is not valid", HttpStatus.NOT_FOUND);
        }
        Reservation reservation = ReservationAdapter.getReservation(reservationDTO);
        reservation.setCustomer(customer);
        reservation.setAuditData(getAuditData(email));

        List<Item> itemList = reservation.getItems();
        System.out.println(reservation);
        for (Item item : itemList) {
            Optional<Product> availableItem = productRepository.findById(item.getProduct().getId());
            if (availableItem.isPresent()) {
                if (!availableItem.get().getIsAvailable()) {
                    throw new CustomError(availableItem.get().getName() + " is not available");
                }
                availableItem.get().setIsAvailable(false);
                productRepository.save(availableItem.get());
            }
            item.setAuditData(getAuditData(email));
        }

        return ReservationAdapter.getReservationDTO(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDTO getReservation(int id) throws CustomError {

        String email = getEmailFromAuthentication();
        if (email==null)
            throw new CustomError("Email does not exist");

        System.out.println("User Email: "+email);

        User user = userRepository.findByEmail(email);
        if (user==null)
            throw new CustomError("User does not exist");

        RoleType roleType = user.getRoleType();
        Optional<Reservation> reservation;

        if (roleType == RoleType.ADMIN)
        {
        reservation = reservationRepository.findById(id);

        }else{
            Customer customer = customerRepository.findCustomerByUser(user);

            if (customer == null)
                throw new CustomError("Customer not found");

            reservation = reservationRepository.findReservationByIdAndCustomer(id,customer);
        }


        if (reservation.isPresent())
            return ReservationAdapter.getReservationDTO(reservation.get());

        throw new CustomError("Reservation with ID: " + id + " not found",HttpStatus.NOT_FOUND);

    }

    @Override
    @Transactional
    public List<ReservationDTO> getAllReservation() throws CustomError {
        String email = getEmailFromAuthentication();
        if (email==null)
            throw new CustomError("Email does not exist");

        System.out.println("User Email: "+email);

        User user = userRepository.findByEmail(email);
        if (user==null)
            throw new CustomError("User does not exist");

        RoleType roleType = user.getRoleType();

        List<ReservationDTO> reservationDTOList = new ArrayList<>();

        if (roleType == RoleType.ADMIN){

       reservationDTOList = reservationRepository
                .findAll()
                .stream()
                .map(ReservationAdapter::getReservationDTO)
                .collect(
                        Collectors
                                .toList()
                );

        }else{

            Customer customer = customerRepository.findCustomerByUser(user);

            if (customer == null)
                throw new CustomError("Customer not found");


            reservationDTOList = reservationRepository.findReservationsByCustomer(customer)
                    .stream()
                    .map(ReservationAdapter::getReservationDTO)
                    .collect(
                            Collectors
                                    .toList()
                    );
        }

        if (reservationDTOList.size() <= 0)
            throw new CustomError("No reservations found",HttpStatus.NOT_FOUND);

        return reservationDTOList;
    }

    @Override
    public ReservationDTO updateReservation(int id, ReservationDTO reservationDTO) throws CustomError {
        Optional<Reservation> existingReservation = reservationRepository.findById(id);

        if (existingReservation.isPresent()) {
            Reservation updatedReservation = ReservationAdapter.getReservation(reservationDTO);
            updatedReservation.setId(id);
            updatedReservation.setCustomer(existingReservation.get().getCustomer());

            // Update product availability
            List<Item> updatedItemList = updatedReservation.getItems();
            List<Item> existingItemList = existingReservation.get().getItems();
            AuditData auditData = new AuditData();

            for (Item updatedItem : updatedItemList) {
                Optional<Product> existingProduct = productRepository.findById(updatedItem.getProduct().getId());

                if (existingProduct.isPresent()) {
                    // Restore availability for the previous product
                    if (existingItemList.stream().noneMatch(item -> item.getProduct().getId().equals(updatedItem.getProduct().getId()))) {
                        existingProduct.get().setIsAvailable(true);
                        productRepository.save(existingProduct.get());
                    }

                    // Update availability for the new product
                    Optional<Product> updatedProduct = productRepository.findById(updatedItem.getProduct().getId());
                    if (updatedProduct.isPresent()) {
                        for(Item existingItem: existingItemList){
                            if(updatedItem.getId() != existingItem.getId()){
                                if (!updatedProduct.get().getIsAvailable()) {
                                    throw new CustomError(updatedProduct.get().getName() + " is not available");
                                }
                            }
                        }


                        updatedProduct.get().setIsAvailable(false);
                        productRepository.save(updatedProduct.get());
                    }
                }
                auditData.setUpdatedBy(getEmailFromAuthentication());
                auditData.setUpdatedOn(LocalDateTime.now());
                auditData.setCreatedOn(existingReservation.get().getAuditData().getCreatedOn());
                auditData.setCreatedBy(existingReservation.get().getAuditData().getCreatedBy());
                updatedItem.setAuditData(auditData);
            }

            // Save the updated reservation
            updatedReservation.setAuditData(auditData);
            Reservation savedReservation = reservationRepository.save(updatedReservation);
            return ReservationAdapter.getReservationDTO(savedReservation);
        } else {
            throw new CustomError("Reservation with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteReservation(int id) throws CustomError {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservationRepository.delete(reservation.get());
        } else {
            throw new CustomError("Reservation with ID: " + id + " not found");
        }
    }


    @Override
    public List<ReservationDTO> getAllReservationByProductType(ProductType productType) {
        List<ReservationDTO> reservationDTOList = reservationRepository
                .findAllProduct_ProductType(productType)
                .stream()
                .map((Object reservation) -> ReservationAdapter.getReservationDTO((Reservation) reservation))
                .collect(Collectors.toList());

        return reservationDTOList;

    }

    @Override
    public List<ReservationDTO> getAllReservationByReservationType(ReservationType reservationType) {
        List<ReservationDTO> reservationDTOList = reservationRepository
                .findAllByReservationType(reservationType)
                .stream()
                .map((Object reservation) -> ReservationAdapter.getReservationDTO((Reservation) reservation))
                .collect(Collectors.toList());

        return reservationDTOList;

    }

    private AuditData getAuditData(String email) {
        AuditData auditData = new AuditData();
        auditData.setCreatedBy(email);
        auditData.setUpdatedOn(LocalDateTime.now());
        auditData.setCreatedOn(LocalDateTime.now());
        auditData.setUpdatedBy(email);
        return auditData;
    }
    private String getEmailFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Map<String, Object> attributes = jwtAuthenticationToken.getTokenAttributes();
            return (String) attributes.get("email");
        }
        return null;
    }
}
