package com.np.schoolpgi.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmailIgnoreCase(String email);

	@Query(value = "SELECT u FROM User u WHERE u.email = :email and u.id <> :id")
	User findByEmailIgnoreCaseNotId(@Param("email") String email, @Param("id") Integer id);

	@Query(value = "SELECT u FROM User u WHERE u.id=:id")
	User findByUserId(@Param("id") Integer id);

	@Query(value = "SELECT * FROM user_details u, login l,instance_master inst, level_master lev, role_master rl  "
			+ "WHERE u.created_by=:createdBy AND u.instance_id=inst.id AND u.level_id=lev.id AND u.role_id=rl.id AND  u.u_id=l.u_id AND "
			+ "( " + "lower(u.name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(u.email) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(inst.instance_name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(lev.level_name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(rl.name) LIKE lower(concat('%',:searchKey,'%')) OR lower(l.username) LIKE lower(concat('%',:searchKey,'%')) OR (CONCAT(u.phone_no,'' )) like (concat('%',:searchKey,'%')) "
			+ ")", nativeQuery = true)
	Page<User> findByCreatedBy(@Param("createdBy") Integer createdBy, Pageable pageable,
			@Param("searchKey") String searchKey);

	List<Optional<User>> findByRoleIdId(Integer roleId);

	List<User> findByLevelMasterId(Integer id);

	@Query(value = "SELECT * FROM user_details u, login l,instance_master inst, level_master lev, role_master rl  "
			+ "WHERE u.instance_id=inst.id AND u.level_id=lev.id AND u.role_id=rl.id AND u.u_id=l.u_id AND " + "( "
			+ "lower(u.name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(u.email) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(inst.instance_name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(lev.level_name) LIKE lower(concat('%',:searchKey,'%')) OR "
			+ "lower(rl.name) LIKE lower(concat('%',:searchKey,'%')) OR lower(l.username) LIKE lower(concat('%',:searchKey,'%')) OR (CONCAT(u.phone_no,'' )) like (concat('%',:searchKey,'%'))"
			+ ")", nativeQuery = true)
	Page<User> findAllUser(Pageable pageable, @Param("searchKey") String searchKey);

	@Query(value = "SELECT * FROM user_details u WHERE u.created_by=:id", nativeQuery = true)
	List<User> findByCreatedBy(@Param("id") int id);

	@Query(value = "SELECT \r\n" + "    CASE u.level_id\r\n" + "        WHEN 1	THEN 'National'\r\n"
			+ "				WHEN 2	THEN 'State'\r\n" + "				WHEN 3	THEN 'District'\r\n"
			+ "				WHEN 4	THEN 'Block'\r\n" + "				WHEN 5	THEN 'School'\r\n"
			+ "        ELSE 'Unknown'\r\n" + "    END AS level_label,\r\n" + "		    CASE u.role_id\r\n"
			+ "        WHEN 1	THEN 'Admin'\r\n" + "				WHEN 3	THEN 'Approver'\r\n"
			+ "				WHEN 4	THEN 'Reviewer'\r\n" + "				WHEN 5	THEN 'Viewer'\r\n"
			+ "				WHEN 7	THEN 'Admin 2'\r\n" + "				WHEN 8	THEN 'Level Admin'\r\n"
			+ "				WHEN 6	THEN 'Data Entry'\r\n" + "				WHEN 2	THEN 'Data Entry1'\r\n"
			+ "        ELSE 'Unknown'\r\n" + "    END AS role_id,\r\n" + "    COUNT(*)  \r\n"
			+ "FROM user_details u \r\n" + "WHERE u.instance_id IN (:instanceIds) \r\n"
			+ "GROUP BY u.level_id, u.role_id", nativeQuery = true)
	List<Object[]> getUserDetails(@Param("instanceIds") List<Integer> instanceIds);

//	@Query(value = "SELECT \r\n" + "    CASE u.level_id\r\n" + "        WHEN 1	THEN 'National'\r\n"
//			+ "				WHEN 2	THEN 'State'\r\n" + "				WHEN 3	THEN 'District'\r\n"
//			+ "				WHEN 4	THEN 'Block'\r\n" + "				WHEN 5	THEN 'School'\r\n"
//			+ "				WHEN 6	THEN 'School'\r\n" + "				WHEN 7	THEN 'State 2'\r\n"
//			+ "				WHEN 8	THEN 'District 2'\r\n" + "				WHEN 9	THEN 'State 3'\r\n"
//			+ "				WHEN 10	THEN 'District 3'\r\n" + "				WHEN 11	THEN 'Data Entry Level'\r\n"
//			+ "				WHEN 12	THEN 'Sub Data Entry Level'\r\n" + "				WHEN 13	THEN 'City'\r\n"
//			+ "        ELSE 'Unknown'\r\n" + "    END AS level_label,\r\n" + "		    CASE u.role_id\r\n"
//			+ "        WHEN 1	THEN 'Admin'\r\n" + "				WHEN 3	THEN 'Approver'\r\n"
//			+ "				WHEN 4	THEN 'Reviewer'\r\n" + "				WHEN 5	THEN 'Viewer'\r\n"
//			+ "				WHEN 7	THEN 'Admin 2'\r\n" + "				WHEN 8	THEN 'Level Admin'\r\n"
//			+ "				WHEN 6	THEN 'Data Entry'\r\n" + "				WHEN 2	THEN 'Data Entry1'\r\n"
//			+ "        ELSE 'Unknown'\r\n" + "    END AS role_id,\r\n" + "    COUNT(*)  \r\n"
//			+ "FROM user_details u \r\n" + "WHERE u.instance_id IN (:instanceIds) \r\n"
//			+ "GROUP BY u.level_id, u.role_id", nativeQuery = true)
//	List<Object[]> getUserDetails(@Param("instanceIds") List<Integer> instanceIds);

}
