package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository

public interface ScheduleRepository extends JpaRepository<ScheduleInfo, Long>
{

	@Query("SELECT e FROM ScheduleInfo e WHERE e.empId = ?1")
	List<ScheduleInfo> findByempId(int empId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM schedule_info WHERE emp_id = ?1", nativeQuery = true)
	void deleteById(int empId);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM schedule_info WHERE start_date >= ?1 AND emp_id = ?2", nativeQuery = true)
	void deleteByStartdate(String startDate, int empId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM schedule_info WHERE end_date <= ?1 AND emp_id = ?2", nativeQuery = true)
	void deleteByEndDate(String endDate, int empId);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM schedule_info WHERE start_date >= ?1 AND end_date <= ?2 AND emp_id = ?3", nativeQuery = true)
	void deleteBetweenDates(String startDate, String endDate, int empId);
	
	@Query("SELECT s FROM ScheduleInfo s WHERE (?2 between s.startDate AND s.endDate) AND (s.startDate <= ?1)")
	List<ScheduleInfo> fetchScheduleBetweenDates(String startDate, String endDate);
	
	@Query("SELECT s FROM ScheduleInfo s WHERE (?1 between s.startDate AND s.endDate) AND (s.endDate >= ?2)")
	List<ScheduleInfo> fetchSchedulesUpdateStartDate(String startDate, String endDate);
	
	@Query("SELECT s FROM ScheduleInfo s WHERE ( s.startDate <= ?1 AND s.endDate >= ?2)")
	List<ScheduleInfo> fetchScheduleUpdateStartandEndDates(String startDate, String endDate);


	@Query("SELECT d FROM ScheduleInfo d WHERE d.startDate <= ?1 AND d.endDate >= ?1")
	List<ScheduleInfo> getScheduleByDate(String date);
	

}
