package com.np.schoolpgi.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.np.schoolpgi.constants.APPServiceCode;
import com.np.schoolpgi.dao.ExcelSignatureRepository;
import com.np.schoolpgi.dao.InstanceMasterRepository;
import com.np.schoolpgi.dao.MediaUploadRepo;
import com.np.schoolpgi.dao.SurveyMapQuestionRepo;
import com.np.schoolpgi.dao.SurveyMasterRepository;
import com.np.schoolpgi.dao.UserRepository;
import com.np.schoolpgi.dto.request.DownloadExcelRequest;
import com.np.schoolpgi.dto.request.SurveyDataEntryListRequest;
import com.np.schoolpgi.dto.response.DownloadSurveyExcelResponse;
import com.np.schoolpgi.dto.response.SurveyErrorReponse;
import com.np.schoolpgi.exception.SomethingWentWrongException;
import com.np.schoolpgi.model.InstanceMaster;
import com.np.schoolpgi.model.MediaUpload;
import com.np.schoolpgi.model.SurveyMapQuestion;
import com.np.schoolpgi.model.SurveyMaster;
import com.np.schoolpgi.service.DocumentUploadDownloadService;
import com.np.schoolpgi.util.AESEncryption;
import com.np.schoolpgi.util.Converter;
import com.np.schoolpgi.util.ConverterRequestSurveyData;
import com.np.schoolpgi.util.RequestSaveSurveyData;
import com.np.schoolpgi.util.ResultDet;
import com.np.schoolpgi.util.ResultDetRequest;
import com.np.schoolpgi.util.ResultHead;
import com.np.schoolpgi.util.StringUtils;
import com.np.schoolpgi.util.SurveyQuestions;

@Service
public class DocumentUploadDownloadServiceImpl implements DocumentUploadDownloadService {

	final static Logger LOGGER = LogManager.getLogger(DocumentUploadDownloadServiceImpl.class);

	@Autowired
	SurveyMasterServiceImpl serviceImpl;

	@Autowired
	SurveyMasterRepository surveyMasterRepository;

	@Autowired
	ExcelSignatureRepository excelSignatureRepository;

	@Value("${thirdpartydatasource.excels}")
	private String excelFolder;

//	@Value("${thirdpartydatasource.excels}")
//	private String uploadExcelFolder;

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Autowired
	private AESEncryption aesEncryption;
	@Autowired
	SurveyMapQuestionRepo surveyMapQuestionRepo;
	@Autowired
	MediaUploadRepo mediaUploadRepo;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	UserRepository userRepository;

	String signaturePropertyName = "signature";
	int saltLength = 16;

	@Autowired
	InstanceMasterRepository instanceMasterRepository;

	@Override
	public DownloadSurveyExcelResponse downloadSurveyExcel(DownloadExcelRequest id) {
		DownloadSurveyExcelResponse downloadSurExlResp = new DownloadSurveyExcelResponse();
		try {
			List<Integer> SingleRow = new ArrayList<>();

			Optional<SurveyMaster> survey = surveyMasterRepository.findById(id.getSurveyId());

			if (survey.isPresent()) {
				//LOGGER.info("Survey is present of this survey id");
				List<SurveyMapQuestion> questions = surveyMapQuestionRepo
						.findBySurveyMasterIdAndIsthird(id.getSurveyId());
				if (!questions.isEmpty()) {
					//LOGGER.info("Map Question is present of this survey id with isThird");
					SurveyDataEntryListRequest surveyDataEntryListRequest = new SurveyDataEntryListRequest();
					surveyDataEntryListRequest.setSurveyId(id.getSurveyId());
					surveyDataEntryListRequest.setIsThird(1);
					surveyDataEntryListRequest.setLoginId(id.getLoggedInUserId());
					surveyDataEntryListRequest.setInstanceId(0);
					String surveyDataEntryById = serviceImpl.surveyDataEntryById(surveyDataEntryListRequest);
					//LOGGER.info("------------surveyDataEntryById-----------" + surveyDataEntryById);
					if (!StringUtils.isValidObj(surveyDataEntryById)) {
						downloadSurExlResp.setSuccess(false);
						downloadSurExlResp.setErrorCode(APPServiceCode.APP543.getStatusCode());
						downloadSurExlResp.setErrorMessage(APPServiceCode.APP543.getStatusDesc());
						return downloadSurExlResp;
					}
					//LOGGER.info("surveyDataEntryById is present of this survey id, login id and isThird");
					SurveyQuestions[] data = Converter.fromJsonString(surveyDataEntryById);
					ResultDet[] resultDet = data[0].getResultDet();
					ResultHead[] resultH = data[0].getResultHead();

					if (!StringUtils.isValidObj(resultDet) || resultDet.length == 0 || !StringUtils.isValidObj(resultH)
							|| resultH.length == 0 || resultDet[0].getIsThirdParty() != 1
							|| resultH[0].getSurveyID() == 0) {
						downloadSurExlResp.setSuccess(false);
						downloadSurExlResp.setErrorCode(APPServiceCode.APP544.getStatusCode());
						downloadSurExlResp.setErrorMessage(APPServiceCode.APP544.getStatusDesc());
						return downloadSurExlResp;
					}
					//LOGGER.info("-------resultDet - ------" + resultDet.toString());
					
					//String folderPath = "D:/PGI/backend/third_party_data_source/download/";
					String fileName=survey.get().getSurveyName().replaceAll("\\s", "")+ ".xlsx";
					String filepath =excelFolder+"download/"+fileName;
										
					//String filepath = "D:/" + "download/" + survey.get().getSurveyName().replaceAll("\\s", "")+ ".xlsx";
					
					
					File f = new File(filepath);
					
					if (f.getParentFile() != null) {
						f.getParentFile().mkdirs();
						//LOGGER.info("-------- f.getParentFile().mkdirs() --------");
					}
					//LOGGER.info("Download folder has been created");

					Workbook workbook1 = new XSSFWorkbook();
					//LOGGER.info("------------workbook1---------");
					((XSSFWorkbook) workbook1).lockStructure();
					//LOGGER.info("------------((XSSFWorkbook) workbook1).lockStructure()---------");
					Sheet sheet1 = workbook1.createSheet(survey.get().getSurveyName().replaceAll("\\s", ""));
					//LOGGER.info("------Sheet sheet1 = workbook1.createSheet(\"survey_\" + id.getSurveyId());------------");
					Font font = workbook1.createFont();
					//LOGGER.info("------------Font font = workbook1.createFont();---------");
					font.setBold(true);
					CellStyle unlock = workbook1.createCellStyle();
					unlock.setLocked(false);
					unlock.setWrapText(true);
					CellStyle bold = workbook1.createCellStyle();
					//LOGGER.info("-----------CellStyle bold = workbook1.createCellStyle();---------");
					bold.setFont(font);
					bold.setAlignment(HorizontalAlignment.CENTER);
					bold.setWrapText(true);
					Row row = sheet1.createRow(0);
					Cell c = row.createCell(0);
					//LOGGER.info("------------Cell c = row.createCell(0);---------");
					c.setCellValue("S.No.");
					c.setCellStyle(bold);
					c = row.createCell(1);
					c.setCellValue("Question S. No.");
					c.setCellStyle(bold);
					c = row.createCell(2);
					c.setCellValue("Instance Code");
					c.setCellStyle(bold);
					c = row.createCell(3);
					c.setCellValue("Instance Name");
					c.setCellStyle(bold);
					c = row.createCell(4);
					c.setCellValue("Instance Id");
					c.setCellStyle(bold);
					c = row.createCell(5);
					c.setCellValue("Survey ID");
					c.setCellStyle(bold);
					c = row.createCell(6);
					c.setCellValue("Survey Name");
					c.setCellStyle(bold);
					c = row.createCell(7);
					c.setCellValue("Domain Id");
					c.setCellStyle(bold);
					c = row.createCell(8);
					c.setCellValue("Domain");
					c.setCellStyle(bold);
					c = row.createCell(9);
					c.setCellValue("Sub Domain Id");
					c.setCellStyle(bold);
					c = row.createCell(10);
					c.setCellValue("Sub Domain");
					c.setCellStyle(bold);
					c = row.createCell(11);
					c.setCellValue("Question Type");
					c.setCellStyle(bold);
					c = row.createCell(12);
					c.setCellValue("Question/Sub Question Id");
					c.setCellStyle(bold);
					c = row.createCell(13);
					c.setCellValue("Question Code");
					c.setCellStyle(bold);
					c = row.createCell(14);
					c.setCellValue("Question Name");
					c.setCellStyle(bold);
					c = row.createCell(15);
					c.setCellValue("Answer Type");
					c.setCellStyle(bold);
					c = row.createCell(16);
					c.setCellValue("Is Mandatory");
					c.setCellStyle(bold);
					c = row.createCell(17);
					c.setCellValue("Only Numeric");
					c.setCellStyle(bold);
					c = row.createCell(18);
					c.setCellValue("Answer");
					c.setCellStyle(bold);
					c = row.createCell(19);

					//LOGGER.info("-----Before-----CellStyle innercell = workbook1.createCellStyle();--------");
					CellStyle innercell = workbook1.createCellStyle();
					//LOGGER.info("-----After-----CellStyle innercell = workbook1.createCellStyle();--------");
					innercell.setWrapText(true);
					int rowCount = 1;
					int maxMeregeColumn = 19;
					Boolean ismandotary = false;

					//LOGGER.info("-----Going Inside Loop for (int i = 0; i < resultDet.length; i++) -------");
					for (int i = 0; i < resultDet.length; i++) {
						Optional<InstanceMaster> user = instanceMasterRepository
								.findById((int) resultDet[i].getInstanceId());
						//System.out.println(user.get());

						if (user.get() == null) {
							downloadSurExlResp.setSuccess(false);
							downloadSurExlResp.setErrorCode(APPServiceCode.APP510.getStatusCode());
							downloadSurExlResp.setErrorMessage(APPServiceCode.APP510.getStatusDesc());
							return downloadSurExlResp;
						}

						String instanceName = user.get().getInstanceName();
						//LOGGER.info("----Inside For Loop------ i = " + i);
						Row inrow = sheet1.createRow(rowCount++);
						//LOGGER.info("-----Row inrow = sheet1.createRow(rowCount++); -------");
						int sno = i;
						Cell cell = inrow.createCell(0);
						cell.setCellValue(sno + 1);
						cell.setCellStyle(innercell);
						cell = inrow.createCell(1);

						cell.setCellValue(resultDet[sno].getsno());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(2);

						cell.setCellValue(resultDet[sno].getInstanceCode());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(3);

						cell.setCellValue(instanceName);
						cell.setCellStyle(innercell);
						cell = inrow.createCell(4);
						cell.setCellValue(resultDet[i].getInstanceId());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(5);
						ResultHead[] resultHead = data[0].getResultHead();
						cell.setCellValue(resultHead[0].getSurveyID());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(6);
						cell.setCellValue(resultHead[0].getSurveyName());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(7);
						cell.setCellValue(resultDet[sno].getDomainID());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(8);
						cell.setCellValue(resultDet[sno].getDomainName());
						cell.setCellStyle(innercell);
						if (resultDet[sno].getSubDomainID() != null) {
							cell = inrow.createCell(9);
							cell.setCellValue(resultDet[sno].getSubDomainID().toString());
							cell.setCellStyle(innercell);
						}

						if (resultDet[sno].getSubDomainID() != null) {
							cell = inrow.createCell(10);
							cell.setCellValue(resultDet[sno].getSubDomainName());
							cell.setCellStyle(innercell);

						}

						if (resultDet[sno].getSubQuestionID() == 0) {
							cell = inrow.createCell(11);
							cell.setCellValue("Question");
							cell.setCellStyle(innercell);
							if (resultDet[sno].getIsMandatory() == 0) {
								ismandotary = false;
							} else {
								ismandotary = true;
							}
							cell = inrow.createCell(12);
							cell.setCellValue(resultDet[sno].getQuestionID());
							cell.setCellStyle(innercell);
						} else {
							cell = inrow.createCell(11);
							cell.setCellValue("Sub Question");
							cell.setCellStyle(innercell);
							cell = inrow.createCell(12);
							cell.setCellValue(resultDet[sno].getSubQuestionID());
							cell.setCellStyle(innercell);

						}
						cell = inrow.createCell(13);
						cell.setCellValue(resultDet[sno].getCode());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(14);
						cell.setCellValue(resultDet[sno].getQuestion());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(15);
						cell.setCellValue(resultDet[sno].getType());
						cell.setCellStyle(innercell);
						cell = inrow.createCell(16);
						if (ismandotary == true)
							cell.setCellValue("Yes");
						else
							cell.setCellValue("No");
						String type = resultDet[sno].getType();
						cell = inrow.createCell(17);
						// --------------Below Code in Red Zone----------------
						if (resultDet[sno].getQOnlyNumeric() != null
								&& (type.equals("Multiple Text Box") || type.equals("Single Text Box"))) {
							if (resultDet[sno].getSubQuestionID() == 0) {
								if (Boolean.parseBoolean(resultDet[sno].getQOnlyNumeric().toString()) == true) {
									cell.setCellValue("Yes");

								} else
									cell.setCellValue("No");
							} else {
								if (Boolean.parseBoolean(resultDet[sno].getSqOnlyNumeric().toString()) == true) {
									cell.setCellValue("Yes");
								} else
									cell.setCellValue("No");
							}

						}
						if (resultDet[sno].getSqOnlyNumeric() != null
								&& (type.equals("Multiple Text Box") || type.equals("Single Text Box"))) {
							if (Boolean.parseBoolean(resultDet[sno].getSqOnlyNumeric().toString()) == true) {
								cell.setCellValue("Yes");
							} else
								cell.setCellValue("No");
						}

						// ----------------Above Code in Red Zone----------------
						if (type.equals("Drop Down") || type.equals("Check Box")) {
							String[] options = resultDet[sno].getAnsOptions().split("\\|\\|");
							cell = inrow.createCell(18);
							cell.setCellStyle(unlock);
							if (resultDet[sno].getAnswers() == null || resultDet[sno].getAnswers() == "") {
								cell.setCellValue("Select Options:");
							} else {
								cell.setCellValue(resultDet[sno].getAnswers());
							}
							CellRangeAddressList addressList = new CellRangeAddressList(sno + 1, sno + 1, 15, 15);
							DataValidationHelper validationHelper = sheet1.getDataValidationHelper();
							DataValidationConstraint dvConstraint = validationHelper
									.createExplicitListConstraint(options);
							DataValidation validation = validationHelper.createValidation(dvConstraint, addressList);
							validation.setShowErrorBox(true);
							validation.createErrorBox("Invalid Selection",
									"Please select a valid option from the dropdown list.");
							sheet1.addValidationData(validation);
						}
						if (type.equals("Date Time")) {
							cell = inrow.createCell(18);
							DataValidationHelper dvHelper = sheet1.getDataValidationHelper();
							DataValidationConstraint constraint = dvHelper.createDateConstraint(OperatorType.BETWEEN,
									"01-01-1970 00:00:00", "01-01-2050 00:00:00", "dd-MM-yyyy HH:mm:ss");
							CellRangeAddressList rangeList = new CellRangeAddressList(cell.getRowIndex(),
									cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
							DataValidation validation = dvHelper.createValidation(constraint, rangeList);
							validation.setShowErrorBox(true);
							validation.createErrorBox("Date Constraint",
									"Set the date time between 01-01-1970 00:00:00\" to 01-01-2050 00:00:00 and in dd-MM-yyyy HH:mm:ss format ");
							sheet1.addValidationData(validation);
							CreationHelper createHelper = workbook1.getCreationHelper();
							CellStyle dateCellStyle = workbook1.createCellStyle();
							dateCellStyle
									.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
							if (resultDet[sno].getAnswers() == null || resultDet[sno].getAnswers() == "") {
								cell.setCellValue(new Date());
							}

							else {
								cell.setCellValue(
										new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(resultDet[sno].getAnswers()));
							}
//									try {
//										cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//												.parse(resultDet[sno].getAnswers()));
//									} catch (ParseException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}

							dateCellStyle.setLocked(false);
							cell.setCellStyle(dateCellStyle);
						}
						if (type.equals("Single Text Box")) {
							cell = inrow.createCell(18);
							cell.setCellValue("");
							SingleRow.add(sno + 1);
							if (resultDet[sno].getAnswers() != null && resultDet[sno].getAnswers() != "")
								cell.setCellValue(resultDet[sno].getAnswers());
							cell.setCellStyle(unlock);
						}
						if (type.equals("Multiple Choice")) {
							cell = inrow.createCell(18);
							String[] multipleoptions = resultDet[sno].getAnsOptions().split("\\|\\|");
							if (resultDet[sno].getAnswers() == null || resultDet[sno].getAnswers() == "") {
								int cellno = 18;
								for (int j = 0; j < multipleoptions.length; j++) {
									cell = inrow.createCell(cellno);
									cell.setCellValue("Select value");
									String[] list = new String[] { "Select value", multipleoptions[j] };
									CellRangeAddressList addressList = new CellRangeAddressList(sno + 1, sno + 1,
											cellno, cellno);
									DataValidationHelper validationHelper = sheet1.getDataValidationHelper();
									DataValidationConstraint validationConstraint = validationHelper
											.createExplicitListConstraint(list);
									DataValidation validation = validationHelper.createValidation(validationConstraint,
											addressList);
									validation.setShowErrorBox(true);
									sheet1.addValidationData(validation);
									cell.setCellStyle(unlock);
									maxMeregeColumn = Math.max(maxMeregeColumn, cellno);
									cellno++;
								}

							}

//								else
//									cell.setCellValue(resultDet[sno].getAnswers());

						}
						if (type.equals("Single Text Box")) {
							cell = inrow.createCell(18);
							if (resultDet[sno].getSqOnlyNumeric() != null && type.equals("Single Text Box")) {
								if (Boolean.parseBoolean(resultDet[sno].getSqOnlyNumeric().toString()) == true) {
									DataValidationHelper dvHelper = sheet1.getDataValidationHelper();
									DataValidationConstraint constraint = dvHelper
											.createTextLengthConstraint(OperatorType.BETWEEN, "1", "500");
									CellRangeAddressList rangeList = new CellRangeAddressList(cell.getRowIndex(),
											cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
									DataValidation validation = dvHelper.createValidation(constraint, rangeList);
									validation.setShowErrorBox(true);
									validation.createErrorBox("Length Constraint",
											"Answer length should be greater than 1 & less than 500");
									sheet1.addValidationData(validation);
								} else {
									DataValidationHelper dvHelper = sheet1.getDataValidationHelper();
									DataValidationConstraint constraint = dvHelper
											.createTextLengthConstraint(OperatorType.BETWEEN, "3", "500");

									CellRangeAddressList rangeList = new CellRangeAddressList(cell.getRowIndex(),
											cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
									DataValidation validation = dvHelper.createValidation(constraint, rangeList);
									validation.setShowErrorBox(true);
									validation.createErrorBox("Length Constraint",
											"Answer length should be greater than 2 & less than 500");
									sheet1.addValidationData(validation);
								}
							}
							cell.setCellStyle(unlock);
						}

						if (type.equals("Multiple Text Box")) {
							String[] multipleLabels = resultDet[sno].getAnsOptions().split("\\|\\|");
							int cellNo = 18;
							for (int j = 0; j < multipleLabels.length; j++) {
								cell = inrow.createCell(cellNo);
								String ans = multipleLabels[j] + "|<Your Answer>";
								cell.setCellValue(ans);
								cell.setCellStyle(unlock);
								cellNo++;
							}
							maxMeregeColumn = Math.max(maxMeregeColumn, cellNo);
						}
						if (type.equals("File Upload")) {
							cell = inrow.createCell(18);
//									DataValidationHelper dvHelper = sheet1.getDataValidationHelper();
//									DataValidationConstraint constraint = dvHelper
//											.createTextLengthConstraint(OperatorType.BETWEEN, "500", "11");
//									CellRangeAddressList rangeList = new CellRangeAddressList(cell.getRowIndex(),
//											cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
//									DataValidation validation = dvHelper.createValidation(constraint, rangeList);
//									validation.setShowErrorBox(true);
//									validation.createErrorBox("Length Constraint",
//											"Answer length should be less than  500");
//									sheet1.addValidationData(validation);
							cell.setCellStyle(unlock);
						}

//TODO
					}

//--------------------------------width of columns----------------------------------
					int maxWidthInUnits = 50 * 256;
					for (int colIndex = 0; colIndex <= maxMeregeColumn; colIndex++) {
						int currentMaxWidth = sheet1.getColumnWidth(colIndex);

						for (int rowIndex = 0; rowIndex <= sheet1.getLastRowNum(); rowIndex++) {
							Row row1 = sheet1.getRow(rowIndex);
							if (row1 != null) {
								Cell cell = row1.getCell(colIndex);

								if (cell != null && cell.getCellType() == CellType.STRING) {
									String cellValue = cell.getStringCellValue();
									int contentLength = cellValue.length() + 2;
									int widthInUnits = contentLength * 256;
									if (widthInUnits >= currentMaxWidth) {
										currentMaxWidth = widthInUnits;
									}
								}
							}
						}
						currentMaxWidth = Math.min(currentMaxWidth, maxWidthInUnits);
						sheet1.setColumnWidth(colIndex, currentMaxWidth);
					}

//-------------------------------------merge columns(header and single text box)----------------------------------
					int tempMergerColumn = maxMeregeColumn;

					if (tempMergerColumn > 19) {
						CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 18, tempMergerColumn);
						sheet1.addMergedRegion(mergedRegion);

						for (Integer i : SingleRow) {
							CellRangeAddress mergedRegion1 = new CellRangeAddress(i, i, 18, tempMergerColumn);
							sheet1.addMergedRegion(mergedRegion1);
						}

					} else {
						CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, 18, 21);
						sheet1.addMergedRegion(mergedRegion);
						for (Integer i : SingleRow) {
							CellRangeAddress mergedRegion1 = new CellRangeAddress(i, i, 18, 21);
							sheet1.addMergedRegion(mergedRegion1);
						}
					}

					sheet1.protectSheet("hello");
					FileOutputStream file = new FileOutputStream(f);
					workbook1.write(file);
					file.flush();
					file.close();
					workbook1.close();

					
					
					
//					FileInputStream fis = new FileInputStream(filepath);
//					byte[] saltByte = new byte[saltLength];
//					new Random().nextBytes(saltByte);
//					MessageDigest md = MessageDigest.getInstance("SHA-256");
//					int bytesRead;
//					byte[] dataBytes = new byte[1024];
//					while ((bytesRead = fis.read(dataBytes)) != -1) {
//						md.update(dataBytes, 0, bytesRead);
//					}
//
//					byte[] digestBytes = md.digest();
//					Path filePath = Paths.get(filepath);
//					ByteBuffer bb = ByteBuffer.wrap(digestBytes);
//					Files.setAttribute(filePath, "user:" + signaturePropertyName, bb);
//					String signatureSavaed = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(digestBytes)).toString();
//					Optional<ExcelSignature> excelSignature = excelSignatureRepository.findBySurveyId(id.getSurveyId());
//					if (excelSignature.isPresent()) {
//						ExcelSignature e = excelSignature.get();
//						e.setSignature(signatureSavaed.toString());
//						e.setCreatedAt(new Date());
//						e.setCreatedBy(id.getLoggedInUserId());
//						e.setFileUrl(filepath);
//						e.setSurveyId(id.getSurveyId());
//						excelSignatureRepository.save(e);
//					} else {
//						ExcelSignature e = new ExcelSignature();
//						e.setSignature(signatureSavaed);
//						e.setCreatedAt(new Date());
//						e.setCreatedBy(id.getLoggedInUserId());
//						e.setFileUrl(filepath);
//						e.setSurveyId(id.getSurveyId());
//						excelSignatureRepository.save(e);
//					}
					downloadSurExlResp.setSuccess(true);
					downloadSurExlResp.setMessageCode(APPServiceCode.APP001.getStatusCode());
					downloadSurExlResp.setMessage(APPServiceCode.APP001.getStatusDesc());
					downloadSurExlResp.setSurveyId(id.getSurveyId());
					downloadSurExlResp.setFileurl(survey.get().getSurveyName().replaceAll("\\s", "") + ".xlsx");
					return downloadSurExlResp;

				} else {
					downloadSurExlResp.setSuccess(false);
					downloadSurExlResp.setErrorCode(APPServiceCode.APP542.getStatusCode());
					downloadSurExlResp.setErrorMessage(APPServiceCode.APP542.getStatusDesc());
					return downloadSurExlResp;
				}

			} else {
				downloadSurExlResp.setSuccess(false);
				downloadSurExlResp.setErrorCode(APPServiceCode.APP525.getStatusCode());
				downloadSurExlResp.setErrorMessage(APPServiceCode.APP525.getStatusDesc());
				return downloadSurExlResp;
			}

		} catch (

		Exception e) {
			LOGGER.error("----Exception Occure while download Survey Excel-------" + e);
			throw new SomethingWentWrongException(e.getMessage());
		}

	}

	private String formatDateTime(FileTime fileTime) {

		LocalDateTime localDateTime = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		return localDateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
	}

	@Override
	public List<SurveyErrorReponse> uploadSurveyExcel(MultipartFile file, Integer surveyId, Integer loginuserId,
			Integer instanceId, boolean isupload) {
		try {
			List<SurveyErrorReponse> errors = new ArrayList<>();
			Optional<SurveyMaster> survey = surveyMasterRepository.findById(surveyId);
			int isError = 0;
			if (!survey.isPresent()) {
				SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
				surveyErrorReponse.setSuccess(false);
				surveyErrorReponse.setErrorMessage("Survey does not exist...");
				errors.add(surveyErrorReponse);
				isError++;
				return errors;
			}
//			Optional<InstanceMaster> user = instanceMasterRepository.findById(instanceId);
//			if (user.get() == null) {
//				SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
//				surveyErrorReponse.setSuccess(false);
//				surveyErrorReponse.setErrorMessage("Instance does not exist");
//				errors.add(surveyErrorReponse);
//				isError++;
//				return errors;
//			}
//
//			String instanceName = user.get().getInstanceName();
//
//			if (!file.getOriginalFilename().equals(survey.get().getSurveyName().replaceAll("\\s", "") + ".xlsx")) {
//				SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
//				surveyErrorReponse.setSuccess(false);
//				surveyErrorReponse.setErrorMessage("Please! Upload valid file");
//				errors.add(surveyErrorReponse);
//				isError++;
//				return errors;
//			}
			SurveyDataEntryListRequest surveyDataEntryListRequest = new SurveyDataEntryListRequest();
			surveyDataEntryListRequest.setSurveyId(surveyId);
			surveyDataEntryListRequest.setIsThird(1);
			surveyDataEntryListRequest.setLoginId(loginuserId);
			surveyDataEntryListRequest.setInstanceId(instanceId);
			String surveyDataEntryById = serviceImpl.surveyDataEntryById(surveyDataEntryListRequest);
			System.out.println("surveyentrydata============================" + surveyDataEntryById);
			SurveyQuestions[] data = Converter.fromJsonString(surveyDataEntryById);

			ResultDet[] resultDet = data[0].getResultDet();
			ResultHead[] resultHeads = data[0].getResultHead();
			resultHeads[0].setLoginId(loginuserId);
			resultHeads[0].setEventName("S");
			resultHeads[0].setInatanceId(instanceId);

			RequestSaveSurveyData requestSaveSurveyData = new RequestSaveSurveyData();
			requestSaveSurveyData.setResultHead(resultHeads);

			ResultDetRequest[] resultDetRequests = new ResultDetRequest[resultDet.length];

			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			if (survey.get().getSurveyName().length() >= 32) {
				if (!workbook.getSheetName(0)
						.equals(survey.get().getSurveyName().replaceAll("\\s", "").substring(0, 31))) {
					SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
					surveyErrorReponse.setSuccess(false);
					surveyErrorReponse.setErrorMessage("Please! Upload valid excel file");
					errors.add(surveyErrorReponse);
					isError++;
					return errors;
				}
			} else {
				if (!workbook.getSheetName(0).equals(survey.get().getSurveyName().replaceAll("\\s", ""))) {
					SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
					surveyErrorReponse.setSuccess(false);
					surveyErrorReponse.setErrorMessage("Please! Upload valid excel file");
					errors.add(surveyErrorReponse);
					isError++;
					return errors;
				}
			}

			Sheet sheet = workbook.getSheet(workbook.getSheetName(0));
			Iterator<Row> rows = sheet.iterator();
			int rowIdx = 0;
			while (rows.hasNext()) {

				Row currentRow = rows.next();
				if (rowIdx == 0) {
					rowIdx++;
					continue;
				}

				SurveyErrorReponse errorResponse = new SurveyErrorReponse();
				Cell InstanceCode = currentRow.getCell(2);
				double instanceCode = InstanceCode.getNumericCellValue();
				Cell InstanceName = currentRow.getCell(3);
				String instanceName = InstanceName.getStringCellValue();
				Cell InstanceId = currentRow.getCell(4);
				double instanceid = InstanceId.getNumericCellValue();
				Cell SurveyId = currentRow.getCell(4);
				double surveyid = SurveyId.getNumericCellValue();
				Cell SurveyName = currentRow.getCell(6);
				String surveyname = SurveyName.getStringCellValue();
				Cell domainid = currentRow.getCell(7);
				double domainId = domainid.getNumericCellValue();
				Cell domainname = currentRow.getCell(8);
				String domainName = domainname.getStringCellValue();
				Cell Subdomain = currentRow.getCell(9);
				double SubdomainId = Double.parseDouble(Subdomain.getStringCellValue());
				Cell SubdomainName = currentRow.getCell(10);
				String subdomainName = SubdomainName.getStringCellValue();
				Cell QuestionT = currentRow.getCell(11);
				String questionType = QuestionT.getStringCellValue();
				Cell Question = currentRow.getCell(12);
				double questionId = Question.getNumericCellValue();
				Cell QuestionCode = currentRow.getCell(13);
				double questionCode = QuestionCode.getNumericCellValue();
				Cell QuestionName = currentRow.getCell(14);
				String questionName = QuestionName.getStringCellValue();
				Cell answerType = currentRow.getCell(15);
				String answerTypevalue = answerType.getStringCellValue();
				Cell ismandatory = currentRow.getCell(16);
				String ismandatoryValue = ismandatory.getStringCellValue();
				Cell OnlyNumeric = currentRow.getCell(17);
				String onlyNumeric = null;
				Cell answer = currentRow.getCell(18);
				String answers = "";
				Integer position = getQuestionIndex(resultDet, (int) domainId, (int) SubdomainId, (int) questionId,
						questionType, (int) instanceid);
				System.out.println(position);
				errorResponse.setInstanceCode(instanceCode);
				errorResponse.setInstanceId(instanceid);
				errorResponse.setInstanceName(instanceName);
				errorResponse.setSurveyId(surveyid);
				errorResponse.setSurveyName(surveyname);
				errorResponse.setDomain(domainName);
				errorResponse.setSubdomain(subdomainName);
				errorResponse.setQuestionType(questionType);
				errorResponse.setQuestionCode(questionCode);
				errorResponse.setQuestionName(questionName);
				errorResponse.setAnswerType(answerTypevalue);

				System.out.println(answerTypevalue);
				if (position != -1) {
					switch (answerTypevalue) {
					case "Single Text Box":
						if (answer.getCellType() == CellType.STRING) {
							answers = answer.getStringCellValue();
						}
						if (answer.getCellType() == CellType.NUMERIC) {
							double value = (double) answer.getNumericCellValue();
							String sValue = String.valueOf(value);
							String subString = sValue.substring(sValue.indexOf(".") + 1, sValue.length());
							if (!(Integer.valueOf(subString) > 0)) {
								sValue = sValue.substring(0, sValue.indexOf("."));
							}
							answers = String.valueOf(sValue);
						}
						onlyNumeric = OnlyNumeric.getStringCellValue();
						if (ismandatoryValue.equals("Yes") && answers == "") {
							errorResponse.setError("Answer is mandatory");
							errors.add(errorResponse);
							isError++;
							break;
						}
						if (onlyNumeric.equals("Yes")) {
							boolean ans = isAlpha(answers);
							if (ans) {
								errorResponse.setError("Answer should contains only numeric value");
								errors.add(errorResponse);
								isError++;
								break;
							}
						}
						errorResponse.setAnswer(answers);
						errors.add(errorResponse);
						resultDet[position].setAnswers(answers);
						break;
					case "Multiple Choice":
						int column = 16;
						List<String> answerFinal = new ArrayList<String>();
						boolean errorFlagM = false;
						int answerCheck = 0;
						while (answer != null) {

							if (answer.getCellType() == CellType.STRING) {
								if (!answer.getStringCellValue().equals("Select value")) {
									answerFinal.add(answer.getStringCellValue());
									answerCheck++;
								}
							} else if (answer.getCellType() == CellType.NUMERIC) {
								double value = (double) answer.getNumericCellValue();
								String sValue = String.valueOf(value);
								String subString = sValue.substring(sValue.indexOf(".") + 1, sValue.length());
								if (!(Integer.valueOf(subString) > 0)) {
									sValue = sValue.substring(0, sValue.indexOf("."));
								}
								answerFinal.add(String.valueOf(sValue));
								answerCheck++;
							}

							column++;
							answer = currentRow.getCell(column);
						}
						if (ismandatoryValue.equals("Yes") && answerCheck == 0) {
							errorFlagM = true;
							break;
						}

						if (errorFlagM == true) {
							errorResponse.setError("Answer is mandatory");
							isError++;
							errors.add(errorResponse);
							break;
						} else if (!answerFinal.isEmpty()) {
							answers = String.join("||", answerFinal);
						}
						errorResponse.setAnswer(answers);
						errors.add(errorResponse);
						resultDet[position].setAnswers(answers);
						break;
					case "Multiple Text Box":

//						List<String> options = List.of(resultDet[position].getAnsOptions().split("\\|\\|"));
						String[] option = (resultDet[position].getAnsOptions().split("\\|\\|"));
						List<String> options = Arrays.asList(option);
						int columnM = 15;
						List<String> answerFinalM = new ArrayList<String>();
						boolean errorflag = false;
						while (answer != null) {
							if (answer.getCellType() == CellType.STRING) {
								String[] optionAnswer = answer.getStringCellValue().split("\\|");
								if (ismandatoryValue.equals("Yes")) {
									if (optionAnswer.length != 2) {
										errorResponse.setError("Answer is mandatory");
										errors.add(errorResponse);
										isError++;
										errorflag = true;
										break;
									} else {
										if (optionAnswer[1].equals("<Your Answer>")) {
											errorResponse.setError("Answer is mandatory");
											errors.add(errorResponse);
											isError++;
											errorflag = true;
											break;
										}
									}
								}
								if (options.contains(optionAnswer[0])) {
									answerFinalM.add(answer.getStringCellValue());
								}
							}
							columnM++;
							answer = currentRow.getCell(columnM);
						}
						if (!answerFinalM.isEmpty() && errorflag == false) {
							answers = String.join("||", answerFinalM);

						}
						errorResponse.setAnswer(answers);
						errors.add(errorResponse);
						resultDet[position].setAnswers(answers);
						break;
					case "Check Box":
						if (answer.getCellType() == CellType.STRING) {
							answers = answer.getStringCellValue();
						}
						if (answer.getCellType() == CellType.NUMERIC) {
							int value = (int) answer.getNumericCellValue();
							answers = String.valueOf(value);
						}
						if (ismandatoryValue.equals("Yes") && answers.equals("Select Options:")) {
							errorResponse.setError("Answer is mandatory");
							isError++;
							errors.add(errorResponse);
							break;
						}
						errorResponse.setAnswer(answers);
						errors.add(errorResponse);
						resultDet[position].setAnswers(answers);
						break;
					case "File Upload":
						if (answer.getCellType() == CellType.STRING) {
							answers = answer.getStringCellValue();
						}
						if (answer.getCellType() == CellType.NUMERIC) {
							double value = (double) answer.getNumericCellValue();
							String sValue = String.valueOf(value);
							String subString = sValue.substring(sValue.indexOf(".") + 1, sValue.length());
							if (!(Integer.valueOf(subString) > 0)) {
								sValue = sValue.substring(0, sValue.indexOf("."));
							}
							answers = String.valueOf(sValue);
						}
						if (ismandatoryValue.equals("Yes") && answers == "") {
							errorResponse.setError("Answer is mandatory");
							isError++;
							errors.add(errorResponse);
							break;
						}
						List<MediaUpload> mediaUpload = mediaUploadRepo.findByFileName(answers);
						if (mediaUpload.isEmpty()) {
							errorResponse.setError("Please first upload file in media upload");
							isError++;
							errors.add(errorResponse);
							break;
						}
						errorResponse.setAnswer(answers);
						errors.add(errorResponse);
						resultDet[position].setAnswers(answers);
						break;
					}

				}

			}
			System.out.println("sdofkos");
			for (int i = 0; i < resultDet.length; i++) {
				ResultDetRequest resultDetRequest = new ResultDetRequest();
				System.out.println("1");
				resultDetRequest.setAnsOptions(resultDet[i].getAnsOptions());
				System.out.println("2");
				resultDetRequest.setAnswers(resultDet[i].getAnswers());
				System.out.println("3");
				resultDetRequest.setDomainid(resultDet[i].getDomainID());
				System.out.println("sdofkos");
				resultDetRequest.setDomainName(resultDet[i].getDomainName());
				System.out.println("sdofkos");
				resultDetRequest.setQuestionid(resultDet[i].getQuestionID());
				System.out.println("sdofkos");
				resultDetRequest.setSubDomainid(resultDet[i].getSubDomainID());
				System.out.println("5");
				resultDetRequest.setSubDomainName(resultDet[i].getSubDomainName());
				resultDetRequest.setSubQuestionid(resultDet[i].getSubQuestionID());
				resultDetRequest.setThirdParty(1);
				resultDetRequest.setTypeid(resultDet[i].getTypeiD());
				resultDetRequest.setInstanceId(resultDet[i].getInstanceId());
				resultDetRequests[i] = resultDetRequest;
				System.out.println("6");
			}
			System.out.println("out");
			requestSaveSurveyData.setResultDet(resultDetRequests);
			System.out.println("sdofkos");
			RequestSaveSurveyData[] requestSaveSurveyDatas = new RequestSaveSurveyData[data.length];
			System.out.println("sdofkos");
			requestSaveSurveyDatas[0] = requestSaveSurveyData;
			System.out.println("sdofkos");
			System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + requestSaveSurveyDatas);
			if (isError == 0 && isupload) {
				String saveSurveyDataAnswer = ConverterRequestSurveyData.toJsonString(requestSaveSurveyDatas);
				System.out.println("survey data entry string jason-==========" + saveSurveyDataAnswer);
				String response = serviceImpl.insertSurveyDataEntry(saveSurveyDataAnswer);
				System.out.println(response);
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject) parser.parse(response);
				System.out.println(json.get("success"));
				if (json.get("success").toString() == "true") {
					
					
					
					String filepath = excelFolder + "upload/" + survey.get().getSurveyName().replaceAll("\\s", "")+ ".xlsx";
					
					
					File f = new File(filepath);
					if (f.getParentFile() != null) {
						f.getParentFile().mkdirs();
					}
					// Files.createDirectories(Paths.get(excelFolder +"upload/"));
					FileOutputStream fileOut = new FileOutputStream(f);
					workbook.write(fileOut);
					fileOut.flush();
					fileOut.close();
				} else {

					SurveyErrorReponse surveyErrorReponse = new SurveyErrorReponse();
					surveyErrorReponse.setSuccess(false);
					surveyErrorReponse.setErrorMessage(json.get("errorMessage").toString());
					errors.clear();
					errors.add(surveyErrorReponse);
					isError++;
					return errors;
				}

			}
			return errors;
		} catch (FileNotFoundException e) {
			throw new com.np.schoolpgi.exception.FileNotFoundException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e);
			throw new SomethingWentWrongException(e.getMessage());
		}
	}

	static Integer getQuestionIndex(ResultDet[] resultDets, Integer domainId, Integer subdomainId, Integer questionId,
			String questionType, Integer instanceId) {
		for (int i = 0; i < resultDets.length; i++) {
			if (questionType.equals("Question")) {
				if (resultDets[i].getDomainID() == domainId.longValue()
						&& Integer.parseInt(resultDets[i].getSubDomainID().toString()) == subdomainId
						&& resultDets[i].getQuestionID() == questionId.longValue()
						&& resultDets[i].getInstanceId() == instanceId.intValue())
					return i;
			} else if (questionType.equals("Sub Question")) {
				if (resultDets[i].getDomainID() == domainId.longValue()
						&& Integer.parseInt(resultDets[i].getSubDomainID().toString()) == subdomainId
						&& resultDets[i].getSubQuestionID() == questionId.longValue()
						&& resultDets[i].getInstanceId() == instanceId.intValue())
					return i;
			}

		}
		return -1;
	}

	public static boolean isAlpha(String s) {
		return s != null && s.chars().allMatch(Character::isLetter);
	}
}
