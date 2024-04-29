package com.np.schoolpgi.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.CharacterIterator;
import java.text.DecimalFormatSymbols;
import java.text.StringCharacterIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("deprecation")
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	private static final String NORMALIZE = "[^a-zA-Z 0-9]+";
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static final Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
	public static final String NEW_LINE = System.getProperty("line.separator");
	private static final String RESTRICTED_CHARS = "[$&<>?#]+";
	public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public static boolean isValidObj(final Object inObj) {
		if ((null == inObj)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isEmpty(final String str) {
		if ((str == null) || (str.trim().length() == 0)) {
			return true;
		} else {
			return false;
		}
	}

	public static String[] tokenizeString(final String str, final char delimiter) {
		final List<String> tokens = new ArrayList<String>();
		if (StringUtils.isEmpty(str)) {
			return null;
		} else {
			final StringCharacterIterator strCharIter = new StringCharacterIterator(str);
			char character = strCharIter.first();
			StringBuffer stringBuffer = new StringBuffer();
			while (character != CharacterIterator.DONE) {
				if (delimiter == character) {
					tokens.add(stringBuffer.toString());
					stringBuffer = new StringBuffer();
				} else {
					stringBuffer.append(character);
				}
				character = strCharIter.next();
			}
			tokens.add(stringBuffer.toString());
		}
		return (String[]) (tokens.toArray(new String[tokens.size()]));
	}

	public static String safeSubString(final String str, final int begin, final int end) {
		String result = "";
		if (!isEmpty(str) && begin >= 0 && end >= 0 && begin < end) {
			if (end <= str.length()) {
				result = str.substring(begin, end);
			} else {
				result = str;
			}
		}
		return result;
	}

	public static String normalize(String text) {
		if (!isEmpty(text)) {
			text = text.replaceAll(NORMALIZE, "");
			text = text.replaceAll(" ", "");
			text = StringUtils.lowerCase(text);
			return text;
		} else
			return text;
	}

	public static String normalizeAsCase(String text) {
		if (!isEmpty(text)) {
			text = text.replaceAll(NORMALIZE, "");
			text = text.replaceAll(" ", "");
			return text;
		} else
			return text;
	}

	public static String normalizeFileNm(String text) {
		if (!isEmpty(text)) {
			// text.replaceAll("[\\W]", "_");
			text.replaceAll("[\\\\:/*?\"<>| ]", "_");
			return text;
		} else
			return text;
	}

	public static String replaceRestrictedChars(String text) {
		if (!isEmpty(text)) {
			text = text.replaceAll(RESTRICTED_CHARS, "");
			return text;
		} else
			return text;
	}
	/*
	 * public static boolean isValidEmailID(final String emailAddress) { boolean
	 * isValid = false; try { if (!isEmpty(emailAddress)) { final InternetAddress[]
	 * address = InternetAddress.parse(emailAddress); if (address != null) { if
	 * (emailAddress.indexOf('@') > -1 && emailAddress.indexOf('.') > -1) { isValid
	 * = true; } } } } catch (AddressException addrEx) {
	 * logger.warn("AddressException in parsing email address : " + emailAddress +
	 * " Exception is : " + addrEx); } catch (Exception ex) {
	 * logger.warn("Exception in parsing email address : " + emailAddress +
	 * " Exception is : " + ex); } return isValid; }
	 */

	public static String getMaskedMobileNo(String inMobileNo) {
		StringBuilder mobileNo = new StringBuilder(inMobileNo);
		Integer[] index = { 2, 3, 5, 6, 7 };
		for (int i = 0; i < index.length; i++) {
			mobileNo.setCharAt(index[i], 'X');
		}
		return mobileNo.toString();
	}

	public static String getMaskedEmailID(String inEmailID) {
		StringBuilder email = new StringBuilder(inEmailID);
		int j = email.indexOf("@");
		for (int i = 1; i < j; i = i + 2) {
			email.setCharAt(i, 'X');
		}
		return email.toString();
	}

	public static boolean isValidInet4Address(String inet4Address) {
		Matcher matcher = pattern.matcher(inet4Address);
		if (!matcher.matches()) {
			// change_vishal
			logger.info(DTF.format(LocalDateTime.now()) + " :: Invalid IP Format for " + inet4Address);
			return false;
		}
		return true;
	}

	public static String getStackTraceAsString(Throwable t) {
		StringBuffer sb = new StringBuffer();
		if (t != null) {
			sb.append("Exception is : ").append(t.getMessage()).append(NEW_LINE);
			StackTraceElement[] stackTrace = t.getStackTrace();
			if (stackTrace != null && stackTrace.length > 0) {
				sb.append("Stack Trace is :").append(NEW_LINE);
				for (StackTraceElement elem : stackTrace) {
					sb.append(elem.toString()).append(NEW_LINE);
				}
			}
		}
		return sb.toString();
	}

	public static boolean isValidLength(final String inStr, final int inMin, final int inMax) {
		final int inStrLength = inStr.length();
		if (inStrLength < inMin || inStrLength > inMax)
			return false;
		else
			return true;
	}

	public static boolean isValidMobile(final String inMobile) {
		Pattern pattern = Pattern.compile("[6-9][0-9]{9}");
		Matcher matcher = pattern.matcher(inMobile);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidPinCode(final String inStr) {
		final String str = inStr.trim();
		final String rule = "[0-9]{6}$";
		final Pattern pattern = Pattern.compile(rule);
		final Matcher matcher = pattern.matcher(str);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	public static boolean compareRegularExp(String inRegex, String str) {
		return Pattern.matches(inRegex, str);
	}

	public static String arrayToStringWithComma(String str[]) {
		String processedString = "";
		processedString = Arrays.toString(str).replace("[", "").replace("]", "").replace(" ", "");
		processedString = "," + processedString + ",";
		return processedString;
	}

	public static String[] commaStringToArray(String str) {
		str = StringUtils.removeStart(str, ",");
		str = StringUtils.removeEnd(str, ",");
		str = str.replace(" ", "");
		// System.out.println( "string is:" + str );
		String str2[] = str.split(",");
		return str2;
	}

	public static String removeCommaStartEnd(String str) {
		if (StringUtils.isNotBlank(str)) {
			str = StringUtils.removeStart(str, ",");
			str = StringUtils.removeEnd(str, ",");
			str.replace(" ", "");
		}
		return str;
	}

	public static String removeET(String inStr) {
		if (isNotBlank(inStr)) {
			char[] charArray = inStr.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (Character.isSpaceChar(charArray[i]) & !Character.isWhitespace(charArray[i])) {
					charArray[i] = ' ';
				}
			}
			return new String(charArray);
		}
		return inStr;
	}

	public static String changeStringToDouble(String inStr) {
		double decimalStr = 0.0;
		if (!StringUtils.isEmpty(inStr)) {
			decimalStr = Double.parseDouble(inStr);
		}
		return String.valueOf(decimalStr);
	}

	// condition:- True means OR, False Means AND
	/**
	 * 
	 * @param obj       String[]
	 * @param condition boolean. <b>true</b> for <i>OR</i> operation, <b>false</b>
	 *                  for <i>AND</i> operation
	 * @return boolean
	 */
	public static boolean checkAllvalidObj(String obj[], boolean condition) {
		boolean flag = false;
		if (condition) {
			for (String str : obj) {
				if (StringUtils.isNotBlank(str)) {
					flag = true;
					break;
				}
			}
		} else {
			for (String str : obj) {
				if (StringUtils.isNotBlank(str)) {
					flag = true;
					continue;
				} else {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	public static Long numericValue(String inString) {
		if (isNumeric(inString)) {
			return Long.parseLong(inString);
		}
		return 0L;
	}

	public static BigDecimal decimalValue(String val) {
		BigDecimal bg = new BigDecimal(0);
		try {
			bg = new BigDecimal(val);
		} catch (Exception ex) {
			logger.error(" Exception :: " + DTF.format(LocalDateTime.now()) + " :: " + ex.getMessage(), ex);
		}
		return bg;
	}

	public static String insertCharacterAtIndex(String inMacAddress, int inIndex, String inCharacter) {
		if (isNotBlank(inMacAddress)) {
			int length = inMacAddress.length();
			int loopIndex = length / inIndex;
			String[] arr = new String[loopIndex];
			int j = 0;
			int k = 0;
			for (int i = 0; i < loopIndex; i++) {
				j = i * inIndex;
				k = j + inIndex;
				arr[i] = substring(inMacAddress, j, k);
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length; i++) {
				sb.append(arr[i]).append(".");
			}
			inMacAddress = sb.substring(0, sb.length() - 1);
		}
		return inMacAddress;
	}

	public static String replaceLast(String text, String searchString, String replacement) {
		if (StringUtils.isNotBlank(text)) {
			int lastIndex = text.lastIndexOf(searchString);
			text = text.substring(0, lastIndex) + replacement + text.substring(lastIndex + 1);
		}
		return text;
	}

	public static boolean isValidCollection(Collection<?> inCollection) {
		if (StringUtils.isValidObj(inCollection) && !inCollection.isEmpty()) {
			return true;
		}
		return false;
	}

	public static String replaceSpace(String str) {
		if (StringUtils.isNotBlank(str)) {
			str = str.trim();
			if (str.contains(" ")) {
				return str.replace(" ", "+");
			}
		}
		return str;
	}

	public static boolean isContains(String inStr1, String inStr2) {
		if (isNotBlank(inStr1) && isNotBlank(inStr2)) {
			return inStr1.replaceAll("\\s+", "").toLowerCase().contains(inStr2.replaceAll("\\s+", "").toLowerCase());
		} else {
			return false;
		}
	}

	public static String toIndianLanguagesStr(String str) {
		if (isNotBlank(str) && StringEscapeUtils.escapeJava(str).contains("\\u")) {
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			for (int i = 0; i < str.length(); i++) {
				int decValue = (int) str.charAt(i);
				if (decValue > 127 && (decValue < 2304 || decValue > 3455)) {
					sb.setCharAt(i, '?');
				}
			}
			return sb.toString();
		}
		return str;
	}

	public static String toUpperCaseFirstLetterOfString(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static float[] convertStringArraytoFloatArray(String[] strArr) {
		float[] floatArray = null;
		if (strArr != null) {
			floatArray = new float[strArr.length];
			for (int i = 0; i < strArr.length; i++) {
				floatArray[i] = Float.parseFloat(strArr[i]);
			}
		}
		return floatArray;
	}

	public static String removeStringFromStartAndEnd(String data, String str) {
		if (StringUtils.isNotBlank(data) && StringUtils.isNotBlank(str)) {
			data = StringUtils.removeStart(data, str);
			data = StringUtils.removeEnd(data, str);
		}
		return data;
	}

	public static int[] convertStringArraytoIntArray(String[] strArr) {
		int[] intArray = null;
		if (strArr != null) {
			intArray = new int[strArr.length];
			for (int i = 0; i < strArr.length; i++) {
				intArray[i] = Integer.parseInt(strArr[i]);
			}
		}
		return intArray;
	}

	public static boolean isStringNumeric(String str) {
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();
		if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign)
			return false;
		boolean isDecimalSeparatorFound = false;
		char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();
		for (char c : str.substring(1).toCharArray()) {
			if (!Character.isDigit(c)) {
				if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
					isDecimalSeparatorFound = true;
					continue;
				}
				return false;
			}
		}
		return true;
	}

	public static String camelCase(String str, String inDelimiter) {
		String normalizedString = null;
		if (!StringUtils.isEmpty(str)) {
			String[] strArr = str.split(inDelimiter);
			StringBuilder builder = new StringBuilder(strArr[0]);
			for (int i = 1; i < strArr.length; i++) {
				builder.append(strArr[i].isEmpty() ? strArr[i]
						: Character.toUpperCase(strArr[i].charAt(0)) + strArr[i].substring(1).toLowerCase());
			}
			normalizedString = builder.toString();
			normalizedString = normalizeAsCase(normalizedString);
		}
		return normalizedString;
	}

	public static List<String> camelCaseList(List<String> inStringList, String inDelimiter) {
		List<String> camelCasedList = null;
		if (StringUtils.isValidObj(inStringList)) {
			camelCasedList = new ArrayList<String>();
			for (String string : inStringList) {
				camelCasedList.add(StringUtils.camelCase(string, inDelimiter));
			}
		}
		return camelCasedList;
	}

	public static Integer[] strArrToIntArr(String[] strArr) {
		Integer[] intArr = null;
		if (StringUtils.isValidObj(strArr)) {
			intArr = new Integer[strArr.length];
			for (int i = 0; i < intArr.length; i++) {
				intArr[i] = Integer.parseInt(strArr[i]);
			}
		}
		return intArr;
	}

//    public static Map<String, AgeMatrixModel> getAgeMatrixOnCurntDate( String[] ageData )
//    {
//        Map<String, AgeMatrixModel> ageMatrix = null;
//        if ( StringUtils.isValidObj( ageData ) )
//        {
//            ageMatrix = new HashMap<>();
//            for ( int i = 0; i < ageData.length; i++ )
//            {
//                StringTokenizer strToken = new StringTokenizer( ageData[i], "|" );
//                if ( strToken.hasMoreTokens() )
//                {
//                    Calendar minAge = Calendar.getInstance();
//                    String className = strToken.nextToken();
//                    minAge.add( Calendar.YEAR, -1 * Integer.parseInt( strToken.nextToken() ) );
//                    Calendar maxAge = Calendar.getInstance();
//                    maxAge.add( Calendar.YEAR, -1 * Integer.parseInt( strToken.nextToken() ) );
//                    AgeMatrixModel ageMatrixModel = new AgeMatrixModel( className, minAge, maxAge );
//                    ageMatrix.put( className, ageMatrixModel );
//                }
//            }
//        }
//        return ageMatrix;
//    }
//    public static Map<String, AgeMatrixModel> getAgeMatrixOnDate( String[] ageData ,Date date)
//    {
//        Map<String, AgeMatrixModel> ageMatrix = null;
//        if ( StringUtils.isValidObj( ageData ) )
//        {
//            ageMatrix = new HashMap<>();
//            for ( int i = 0; i < ageData.length; i++ )
//            {
//                StringTokenizer strToken = new StringTokenizer( ageData[i], "|" );
//                if ( strToken.hasMoreTokens() )
//                {
//                    Calendar minAge = Calendar.getInstance();
//                    minAge.setTime( date );
//                    String className = strToken.nextToken();
//                    minAge.add( Calendar.YEAR, -1 * Integer.parseInt( strToken.nextToken() ) );
//                    Calendar maxAge = Calendar.getInstance();
//                    maxAge.setTime( date );
//                    maxAge.add( Calendar.YEAR, -1 * Integer.parseInt( strToken.nextToken() ) );
//                    AgeMatrixModel ageMatrixModel = new AgeMatrixModel( className, minAge, maxAge );
//                    ageMatrix.put( className, ageMatrixModel );
//                }
//            }
//        }
//        return ageMatrix;
//    }
	// public static Map<String, AgeMatrixModel> getAgeMatrix( String[] ageData )
	// {
	// Map<String, AgeMatrixModel> ageMatrix = null;
	// if ( StringUtils.isValidObj( ageData ) )
	// {
	// ageMatrix = new HashMap<>();
	// for ( int i = 0; i < ageData.length; i++ )
	// {
	// StringTokenizer strToken = new StringTokenizer( ageData[i], "|" );
	// if ( strToken.hasMoreTokens() )
	// {
	// String className = strToken.nextToken();
	// AgeMatrixModel ageMatrixModel = new AgeMatrixModel( className,
	// Integer.parseInt( strToken.nextToken() ),
	// Integer.parseInt( strToken.nextToken() ) );
	// ageMatrix.put( className, ageMatrixModel );
	// }
	// }
	// }
	// return ageMatrix;
	// }

	public static boolean matchPattern(String regex, String data) {
		try {
			// String regex = "^366$|^[0-9]{1,3}$|^[0-9]{1,3}\\,[0-9]{1,3}$";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			// String data = "367";
			Matcher matcher = pattern.matcher(data);
			return matcher.matches();
		} catch (Exception e) {
			logger.error("Getting Exception in matchPattern: ", e);
		}
		return false;
	}

	public static String getExceptionStackTrace(Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		return exceptionAsString;
	}

	public static String removeMultipleSpaces(String str) {
		if (StringUtils.isValidObj(str)) {
			StringTokenizer strToken = new StringTokenizer(str, " ");
			StringBuilder sb = new StringBuilder();
			while (strToken.hasMoreTokens()) {
				String subStr = strToken.nextToken();
				if (StringUtils.isValidObj(subStr) && subStr.length() > 0) {
					sb.append(subStr + " ");
				}
			}
			return sb.toString().trim();
		} else {
			return str;
		}
	}

	public static boolean isAllNumeric(String... inArr) {
		if (isValidObj(inArr) && inArr.length > 0) {
			for (String string : inArr) {
				if (!isNumeric(string)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public static boolean compareLists(List<int[]> list1, List<int[]> list2) {
	    // Check if the sizes of the lists are the same
	    if (list1.size() != list2.size()) {
	        return false;
	    }

	    // Iterate through the lists
	    for (int i = 0; i < list1.size(); i++) {
	        int[] array1 = list1.get(i);
	        int[] array2 = list2.get(i);

	        // Check if the arrays have the same length
	        if (array1.length != array2.length) {
	            return false;
	        }

	        // Compare the elements of the arrays
	        if (!Arrays.equals(array1, array2)) {
	            return false;
	        }
	    }

	    return true;
	}

	public static boolean compareListOfArrays(List<int[]> list1, List<int[]> list2) {
	    // Check if both lists are not null and have the same size
	    if (list1 == null || list2 == null || list1.size() != list2.size()) {
	        return false;
	    }

	    // Iterate through the lists and compare each array
	    for (int i = 0; i < list1.size(); i++) {
	        int[] arr1 = list1.get(i);
	        int[] arr2 = list2.get(i);

	        // Check if arrays are null and have the same length
	        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
	            return false;
	        }

	        // Compare array elements
	        for (int j = 0; j < arr1.length; j++) {
	            if (arr1[j] != arr2[j]) {
	                return false;
	            }
	        }
	    }

	    return true;
	}

    
    public static List<int[]> removeDuplicates(List<int[]> dataEntries) {
        Set<String> seenEntries = new HashSet<>();
        List<int[]> deduplicatedEntries = new ArrayList<>();

        for (int[] entry : dataEntries) {
            String entryString = Arrays.toString(entry);
            if (!seenEntries.contains(entryString)) {
                deduplicatedEntries.add(entry);
                seenEntries.add(entryString);
            }
        }

        return deduplicatedEntries;
    }
}
