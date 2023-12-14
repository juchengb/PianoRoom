package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.Reservation;


public interface ReservationDao {
	int addReservation(Reservation reservation);
	int updateReservationById(Integer id, Reservation reservation);
	int deleteReservationById(Integer id);
	Optional<Reservation> getReservationById(Integer id);
	List<Reservation> findAllReservations();
}
