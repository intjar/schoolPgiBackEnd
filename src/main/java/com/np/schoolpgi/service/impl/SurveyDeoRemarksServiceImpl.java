package com.np.schoolpgi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.schoolpgi.dao.SurveyDeoRemarksRepository;
import com.np.schoolpgi.dto.request.RemarksRequestDto;
import com.np.schoolpgi.dto.request.ReviewDataListReqDto;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.service.SurveyDeoRemarksService;

@Service
public class SurveyDeoRemarksServiceImpl implements SurveyDeoRemarksService {

	final static Logger LOGGER = LogManager.getLogger(SurveyDeoRemarksServiceImpl.class);

	@Autowired
	private SurveyDeoRemarksRepository surveyDeoRemarksRepository;

	@Override
	public String getRemarks(RemarksRequestDto remarksRequestDto) {
		try {

			return surveyDeoRemarksRepository.getRemarks(remarksRequestDto.getSurveyId(),
					remarksRequestDto.getLoginId(), null);

		} catch (Exception e) {
			LOGGER.info(e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	@Override
	public String getSurveyDataListReview(Integer pageNo,Integer pageSize, String sortDir, String sortBy, String searchKey, ReviewDataListReqDto reviewDataListReqDto) {

		try {
			return surveyDeoRemarksRepository.getSurveyDataListReview(pageNo, pageSize, sortDir, sortBy,searchKey,reviewDataListReqDto.getLoginId(),
					reviewDataListReqDto.getInpPg(), reviewDataListReqDto.getYearcode(),null);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String getSurveyDataListReviewDetails(RemarksRequestDto remarksRequestDto) {
		try {

			return surveyDeoRemarksRepository.surveyDataListReviewDetails(remarksRequestDto.getSurveyId(),
					remarksRequestDto.getLoginId(), null);

		} catch (Exception e) {
			LOGGER.info(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	@Override
	public String updSurveyReviewApprove(String req) {
		try {

			return surveyDeoRemarksRepository.updSurveyReviewApprove(req);

		} catch (Exception e) {
			LOGGER.info(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

}
