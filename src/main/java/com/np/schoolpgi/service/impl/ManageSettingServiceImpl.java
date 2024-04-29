package com.np.schoolpgi.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.RoleLinkMappingRepository;
import com.np.schoolpgi.dao.RoleRepository;
import com.np.schoolpgi.dto.request.RoleLinkMappingUpdateRequest;
import com.np.schoolpgi.dto.request.ViewReqById;
import com.np.schoolpgi.dto.response.ErrorResponse;
import com.np.schoolpgi.dto.response.MessageResponse;
import com.np.schoolpgi.dto.response.ResponseData;
import com.np.schoolpgi.dto.response.ResponseValue;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.LinkList;
import com.np.schoolpgi.model.RoleLinkMappings;
import com.np.schoolpgi.model.Roles;
import com.np.schoolpgi.service.LinkListService;
import com.np.schoolpgi.service.ManageSettingService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.SurveyManageSettingComparator;
import com.np.schoolpgi.util.SurveyManageSettingListComparator;

@Service
public class ManageSettingServiceImpl implements ManageSettingService {
	final static Logger LOGGER = LogManager.getLogger(ManageSettingServiceImpl.class);

	@Autowired
	RoleLinkMappingRepository roleLinkMappingRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	LinkListService linkListService;

	@Autowired
	private AESEncryption aesEncryption;

	private ObjectMapper mapper = new ObjectMapper();

	@Value("${rolelinkmapping.jsonpath}")
	private String rolelinkmapping;

	@Override
	public ResponseEntity<?> manageStting(Integer roleid) throws Exception {
		List<List<LinkList>> filteredList = new ArrayList<>();
		Optional<Roles> roles = roleRepository.findById(roleid);
		if (!roles.isPresent()) {
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
					APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));

			return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

		}

		RoleLinkMappings roleLinkMapping = roleLinkMappingRepository.findByRoleId(roleid);

		if (roleLinkMapping == null) {

			List<LinkList> list = new ArrayList<>();
			list = linkListService.getLinkList();

			if (list == null) {

				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else {
				Collections.sort(list, new SurveyManageSettingListComparator());
				filteredList = filteredlist(list);
				Collections.sort(filteredList, new SurveyManageSettingComparator());
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
						new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), filteredList));
				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

		}

		else {
			if (roleLinkMapping.getJson_url() == null || roleLinkMapping.getLinkIds() == null) {
				List<LinkList> list = new ArrayList<>();
				list = linkListService.getLinkList();
				if (list == null) {

					String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
							APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));

					return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

				} else {
				Collections.sort(list, new SurveyManageSettingListComparator());
					filteredList = filteredlist(list);
					Collections.sort(filteredList, new SurveyManageSettingComparator());
					String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
							new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), filteredList));
					return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
				}
			}

			else {

				List<LinkList> list = new ArrayList<>();
				list = linkListService.getLinkList();
				if (list == null) {

					String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
							APPServiceCode.APP996.getStatusCode(), APPServiceCode.APP996.getStatusDesc()));

					return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

				} else {
					String linkIds = roleLinkMapping.getLinkIds();
					String[] linkIdsList = linkIds.split(",");
					for (int i = 0; i < list.size(); i++) {
						LinkList temp = list.get(i);
						for (String s : linkIdsList) {
							if (s.equals(temp.getId().toString())) {
								temp.setStatus(true);
								break;
							}
						}
						list.set(i, temp);
					}
					Collections.sort(list, new SurveyManageSettingListComparator());
					filteredList = filteredlist(list);
					Collections.sort(filteredList, new SurveyManageSettingComparator());
					String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
							new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), filteredList));
					return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

				}

//				String jsonStr = mapper.writerWithDefaultPrettyPrinter()
//						.writeValueAsString(new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), roleLinkMapping.getJson_url()));
//				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}
		}
	}

	@Override
	public String manageSttingUpdateLinks(RoleLinkMappingUpdateRequest roleLinkMappingUpdateRequest) {

		try {
			//String projectRoot = System.getProperty("user.dir");
			Optional<Roles> roles = roleRepository.findById(roleLinkMappingUpdateRequest.getRoleid());
			if (!roles.isPresent()) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));
				return jsonStr;

			}
			RoleLinkMappings roleLinkMappingSearch = roleLinkMappingRepository
					.findByRoleId(roleLinkMappingUpdateRequest.getRoleid());

			if (roleLinkMappingSearch == null) {
				RoleLinkMappings roleLinkMappings = new RoleLinkMappings();
				roleLinkMappings.setRoleId(roleLinkMappingUpdateRequest.getRoleid());
				roleLinkMappings.setLinkIds(roleLinkMappingUpdateRequest.getLinkIds());
				Date date = new Date();
				TypeReference<List<List<LinkList>>> lists = new TypeReference<List<List<LinkList>>>() {
				};
				LOGGER.info(lists);
				List<List<LinkList>> linkLists = mapper.readValue(roleLinkMappingUpdateRequest.getJsonUrl(), lists);
				System.out.println(linkLists);
				
				
				String filePath = rolelinkmapping + roleLinkMappingUpdateRequest.getRoleid() + ".json";
				
				System.out.println("filePath*********************="+filePath);
				
				File f = new File(filePath);
				if (f.getParentFile() != null) {
					f.getParentFile().mkdirs();
				}
				mapper.writeValue(new File(filePath), linkLists);
				roleLinkMappings.setJson_url(filePath);
				roleLinkMappings.setCreatedAt(date);
				roleLinkMappings.setCreatedBy(roleLinkMappingUpdateRequest.getLoggedInUserId());
				roleLinkMappingRepository.save(roleLinkMappings);
			} else {
				TypeReference<List<List<LinkList>>> lists = new TypeReference<List<List<LinkList>>>() {
				};
				List<List<LinkList>> linkLists = mapper.readValue(roleLinkMappingUpdateRequest.getJsonUrl(), lists);
				System.out.println(linkLists);
				String filePath = rolelinkmapping + roleLinkMappingUpdateRequest.getRoleid() + ".json";
				System.out.println("filePath*********************="+filePath);
				
				File f = new File(filePath);
				if (f.getParentFile() != null) {
					f.getParentFile().mkdirs();
				}
				mapper.writeValue(new File(filePath), linkLists);
				roleLinkMappingSearch.setJson_url(filePath);
				roleLinkMappingSearch.setLinkIds(roleLinkMappingUpdateRequest.getLinkIds());
				Date date = new Date();
				roleLinkMappingSearch.setUpdatedAt(date);
				roleLinkMappingSearch.setUpdatedBy(roleLinkMappingUpdateRequest.getLoggedInUserId());
				roleLinkMappingRepository.save(roleLinkMappingSearch);

			}
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new MessageResponse(true,
					APPServiceCode.APP001.getStatusCode(), APPServiceCode.APP001.getStatusDesc()));

			return jsonStr;
		} catch (Exception e) {
			LOGGER.info(APPServiceCode.APP505.getStatusDesc(), e.getMessage());
			throw new SomethingWentWrongException(e.getMessage());
		}

	}
	
	@Override
	public ResponseEntity<?> getRoleLinks(ViewReqById byId) throws Exception {
		// TODO Auto-generated method stub
		try {
			RoleLinkMappings roleLinkMappingSearch = roleLinkMappingRepository.findByRoleId(byId.getId());
			if (roleLinkMappingSearch == null) {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP109.getStatusCode(), APPServiceCode.APP109.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			}
			
			//String projectRoot = System.getProperty("user.dir");
			String jsonUrl = roleLinkMappingSearch.getJson_url();
			String filePath = rolelinkmapping+jsonUrl;
			
			System.out.println("**********filePath=="+filePath);
			
			File f = new File(filePath);
			if (f.exists()) {
				String jsonStr = null;

				String jsonString = new String(Files.readAllBytes(Paths.get(filePath)));
				if (jsonString != "") {
					jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
							new ResponseValue(true, APPServiceCode.APP001.getStatusDesc(), jsonString));
				} else {
					jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
							APPServiceCode.APP129.getStatusCode(), APPServiceCode.APP129.getStatusDesc()));

				}

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));

			} else {
				String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ErrorResponse(false,
						APPServiceCode.APP129.getStatusCode(), APPServiceCode.APP129.getStatusDesc()));

				return ResponseEntity.ok(new ResponseData(aesEncryption.encrypt(jsonStr)));
			}

		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	List<List<LinkList>> filteredlist(List<LinkList> linkLists) {

		Map<String, List<LinkList>> map = new HashMap<>();
		for (LinkList l : linkLists) {
			String key = l.getOrderId();
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<LinkList>());
			}
			map.get(key).add(l);

		}
		Collection<List<LinkList>> val = map.values();
		List<List<LinkList>> list = new ArrayList<>(val);

		return list;
	}

}
