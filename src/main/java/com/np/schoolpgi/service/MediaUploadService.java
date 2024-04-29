package com.np.schoolpgi.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.model.MediaCategory;
import com.np.schoolpgi.model.MediaUpload;

public interface MediaUploadService {
	List<MediaCategory> getMediaCategory();
	MediaUpload mdeiaUpload(MultipartFile[] files, Integer categoryId, Integer loginId);
	void deleteMedia(DeleteRequestDto req);
	ResponseWithPagination getUploadedFile(Integer userId,Integer pageNum, Integer pageSize, String sortDir, String sortBy, String searchKey);
}
