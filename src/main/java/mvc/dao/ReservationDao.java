package mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import mvc.entity.Reservation;


public interface ReservationDao {
//	預約-Reservation
//	新增
	int addReservation(Reservation reservation);
	
//	查詢
	Optional<Reservation> getReservationById(Integer id);
	Optional<Reservation> getLatestReservationByUserId(Integer userId);
	Optional<Reservation> getUserMonthlyCountsByUserId(Integer userId);
	Optional<Reservation> getUserMonthlyHoursByUserId(Integer userId);
	int getCurrentDayCheckinByUserId(Integer userId); // AndByCurrentTime
	List<Reservation> findFutureResrvationsByUserId(Integer userId);
	List<Reservation> findPastResrvationsByUserId(Integer userId);
	List<Reservation> findAllUserMonthlyhours();
	
//	修改
	int updateReservationById(Integer id, Reservation reservation);
	int updateCheckinById(Integer id, Date checkin);
	int updateCheckoutById(Integer id, Date checkout);

//	刪除
	int deleteReservationById(Integer id);


}
