package com.np.schoolpgi.service.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.MediaCategoryRepo;
import com.np.schoolpgi.dao.MediaUploadRepo;
import com.np.schoolpgi.dto.request.DeleteRequestDto;
import com.np.schoolpgi.dto.response.ResponseWithPagination;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.MediaCategory;
import com.np.schoolpgi.model.MediaUpload;
import com.np.schoolpgi.model.User;
import com.np.schoolpgi.service.MediaUploadService;

@Service
public class MediaUploadServiceImpl implements MediaUploadService {

	final static Logger LOGGER = LogManager.getLogger(MediaUploadServiceImpl.class);

//	private static String mediauploadpath = "C:/Users/NP/Desktop/pgi_temp/";

	@Autowired
	private MediaUploadRepo mediaUploadRepo;

	@Autowired
	private MediaCategoryRepo mediaCategoryRepo;

	@Value("${mediaupload.path}")
	private String mediauploadpath;

	@Override
	public MediaUpload mdeiaUpload(MultipartFile[] files, Integer categoryId, Integer loginId) {
		try {
			
			String filePath;
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			
			Optional<MediaCategory> category = mediaCategoryRepo.findById(categoryId);
			if(!category.isPresent()) {
				return null;
			}
			
			MediaUpload uploadedFile=new MediaUpload();
			for(MultipartFile file : files) {
				MediaUpload mediaUpload=new MediaUpload();
				File fileFolder = new File(mediauploadpath + category.get().getCategoryName());
				fileFolder.mkdir();
				filePath = mediauploadpath
						+ category.get().getCategoryName() + "/" + timestamp.getTime() + file.getOriginalFilename();
				File f = new File(filePath);
				file.transferTo(f);
				
				mediaUpload.setCategoryId(categoryId);
				mediaUpload.setCategoryName(category.get().getCategoryName());
				mediaUpload.setCreatedAt(new Date());
//				mediaUpload.setCreatedBy(loginId);
				User user = new User();
				user.setUserId(loginId);
				mediaUpload.setUser(user);
				mediaUpload.setFileName(timestamp.getTime()+file.getOriginalFilename());
				mediaUpload.setFilePath(filePath);
				mediaUpload.setFileType(file.getContentType());
				uploadedFile = mediaUploadRepo.save(mediaUpload);
				
			}

			return uploadedFile;
			
		}catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public List<MediaCategory> getMediaCategory() {
		try {
			return mediaCategoryRepo.findAll();
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public void deleteMedia(DeleteRequestDto req) {
		try {
			mediaUploadRepo.deleteById(req.getId());
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public ResponseWithPagination getUploadedFile(Integer userId,Integer pageNum, Integer pageSize, String sortDir, String sortBy, String searchKey) {
		try {
			ResponseWithPagination viewFileResponse = new ResponseWithPagination();
			Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
	                : Sort.by(sortBy).ascending();
			Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
			Page<MediaUpload> mediaPage=null;
			if(userId==1) 
				mediaPage = mediaUploadRepo.findAllFiles(searchKey,pageable);
			else 
				mediaPage = mediaUploadRepo.findAllFilesById(userId,searchKey,pageable);
			
			
			
//			Page<MediaUpload> mediaPage = mediaUploadRepo.findAll(pageable);
			viewFileResponse.setHttpStatus(HttpStatus.OK);
			viewFileResponse.setLast(mediaPage.isLast());
			viewFileResponse.setMessage(APPServiceCode.APP001.getStatusDesc());
			viewFileResponse.setMessageCode(APPServiceCode.APP001.getStatusCode());
			viewFileResponse.setPageNo(mediaPage.getNumber());
			viewFileResponse.setPageSize(mediaPage.getSize());
			viewFileResponse.setResult(mediaPage.getContent());
			viewFileResponse.setSuccess(true);
			viewFileResponse.setTotalElements(mediaPage.getTotalElements());
			viewFileResponse.setTotalPages(mediaPage.getTotalPages());
			return viewFileResponse;
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
