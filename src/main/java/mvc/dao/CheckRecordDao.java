package mvc.dao;

import java.util.List;
import java.util.Optional;

import mvc.bean.CheckRecord;

public interface CheckRecordDao {
	int addCheckRecord(CheckRecord checkRecord);
	int updateCheckRecordById(Integer id, CheckRecord checkRecord);
	int deleteCheckRecordById(Integer id);
	Optional<CheckRecord> getCheckRecordById(Integer id);
	List<CheckRecord> findAllCheckRecords();
}
