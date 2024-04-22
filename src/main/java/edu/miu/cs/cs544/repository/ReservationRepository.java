package edu.miu.cs.cs544.repository;

import edu.miu.cs.cs544.domain.Customer;
import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {


    Collection<Object> findAllByReservationType(ReservationType reservationType);
    @Query("SELECT r FROM Reservation r JOIN r.items i WHERE i.product.type = :productType")
    Collection<Object> findAllProduct_ProductType(@Param("productType")ProductType productType);

    Optional<Reservation> findReservationByIdAndCustomer(int id, Customer customer);

    List<Reservation> findReservationsByCustomer(Customer customer);
}
