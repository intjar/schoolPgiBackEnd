package com.np.schoolpgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.schoolpgi.model.MediaCategory;

@Repository
public interface MediaCategoryRepo extends JpaRepository<MediaCategory, Integer>{

}
