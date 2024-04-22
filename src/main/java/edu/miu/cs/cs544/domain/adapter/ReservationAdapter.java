package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Reservation;
import edu.miu.cs.cs544.domain.dto.ReservationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationAdapter {

    // converts reservation object to a DTO
    public static ReservationDTO getReservationDTO(Reservation reservation) {
        return new ReservationDTO(reservation.getId(),
                ItemAdapter.getItemsDTOs(reservation.getItems())
//                reservation.getCustomer(),
                ,
                AuditDataAdapter.getAuditDataDTO(reservation.getAuditData()),
                reservation.getReservationType());


    }

    // Converts DTO to reservation objects
    public static Reservation getReservation(ReservationDTO reservationDTO) {
        return new Reservation(reservationDTO.getId(),
                null,
                ItemAdapter.getItems(reservationDTO.getItems()),
                AuditDataAdapter.getAuditData(reservationDTO.getAuditData()),
                reservationDTO.getReservationType()
        );
    }

    public static List<Reservation> getReservationList(List<ReservationDTO> reservationDTOList) {
        if (reservationDTOList.isEmpty())
            return new ArrayList<>();
        return reservationDTOList
                .stream()
                .map(ReservationAdapter::getReservation)
                .collect(
                        Collectors
                                .toList()
                );
    }

    public static List<ReservationDTO> getReservationDTOList(List<Reservation> reservationList) {
        if (reservationList.isEmpty())
            return new ArrayList<>();

        return reservationList
                .stream()
                .map(ReservationAdapter::getReservationDTO)
                .collect(
                        Collectors
                                .toList()
                );
    }


}
