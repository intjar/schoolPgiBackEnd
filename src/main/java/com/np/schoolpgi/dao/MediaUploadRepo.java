package com.np.schoolpgi.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.MediaUpload;

@Repository
public interface MediaUploadRepo extends JpaRepository<MediaUpload, Long>{

	@Query(value = "SELECT * FROM public.media_upload mu, user_details u WHERE mu.created_by=u.u_id "
			+ "AND (LOWER(u.name) LIKE LOWER(CONCAT('%',:searchKey,'%')) OR LOWER(mu.file_name) LIKE LOWER(CONCAT('%',:searchKey,'%')))", nativeQuery = true)
	Page<MediaUpload> findAllFiles(@Param("searchKey") String searchKey ,Pageable pageable);
	
	@Query(value = "SELECT * FROM public.media_upload mu, user_details u WHERE mu.created_by=:userId AND mu.created_by=u.u_id "
			+ "AND (LOWER(u.name) LIKE LOWER(CONCAT('%',:searchKey,'%')) OR LOWER(mu.file_name) LIKE LOWER(CONCAT('%',:searchKey,'%')))", nativeQuery = true)
	Page<MediaUpload> findAllFilesById(@Param("userId") Integer userId,@Param("searchKey") String searchKey ,Pageable pageable);

	List<MediaUpload> findByFileName(String fileName);
}
