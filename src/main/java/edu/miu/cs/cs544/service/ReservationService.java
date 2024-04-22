package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.CustomError;

import edu.miu.cs.cs544.domain.ProductType;
import edu.miu.cs.cs544.domain.ReservationType;
import edu.miu.cs.cs544.domain.dto.ReservationDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ReservationService {

    ReservationDTO createReservation(ReservationDTO reservationDTO) throws CustomError;

    ReservationDTO getReservation(int id) throws CustomError;

    List<ReservationDTO> getAllReservation() throws CustomError;

    ReservationDTO updateReservation(int id, ReservationDTO reservationDTO) throws CustomError;

    void deleteReservation(int id) throws CustomError;

    List<ReservationDTO> getAllReservationByProductType(ProductType productType);

    List<ReservationDTO> getAllReservationByReservationType(ReservationType reservationType);


}
