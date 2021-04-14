package com.my.easytest.util;

import com.my.easytest.exception.ServiceException;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;


public class Utility {
    public static final String regexChars = "$()*+.[?^{|\\ ";

    public Utility() {
    }

    public static void sleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
            // logger.error("Failed to execute action : sleep", var4);
        }

    }

    public static boolean isIn(String str, String[] strs) {
        for(int i = 0; i < strs.length; ++i) {
            if (strs[i].equals(str)) {
                return true;
            }
        }

        return false;
    }

    public static String generateTimestamp(String sFormat, String sYear, String sMonth, String sDay, String sHour, String sMinute, String sSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(1, Integer.parseInt(sYear));
        calendar.add(2, Integer.parseInt(sMonth));
        calendar.add(5, Integer.parseInt(sDay));
        calendar.add(11, Integer.parseInt(sHour));
        calendar.add(12, Integer.parseInt(sMinute));
        calendar.add(13, Integer.parseInt(sSecond));
        SimpleDateFormat oDateFormat = new SimpleDateFormat(sFormat);
        String sTimestamp = oDateFormat.format(calendar.getTime());
        return sTimestamp;
    }

    public static String getTimestamp(String sFormat, String sTime) {
        // ILogger logger = Logger.getLogger();
        SimpleDateFormat oDateFormat = new SimpleDateFormat(sFormat);
        long ts = 0L;

        try {
            ts = oDateFormat.parse(sTime).getTime() / 1000L;
            // logger.info("execute getTimestamp is : " + ts);
        } catch (ParseException var7) {
            // throw ExceptionHandles.build("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var7);
        }

        String timeStamp = String.valueOf(ts);
        return timeStamp;
    }

    public static String getTimestampForMS(String sFormat, String sTime) {
        // ILogger logger = Logger.getLogger();
        SimpleDateFormat oDateFormat = new SimpleDateFormat(sFormat);
        long ts = 0L;

        try {
            ts = oDateFormat.parse(sTime).getTime();
            // logger.info("execute getTimestamp is : " + ts);
        } catch (ParseException var7) {
            // throw ExceptionHandles.build("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var7);
        }

        String timeStamp = String.valueOf(ts);
        return timeStamp;
    }

    public static boolean isNumeric(String string) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    public static String getTimestampFromDate(String format, String time) {
        // ILogger logger = Logger.getLogger();
        SimpleDateFormat dateFormat;
        if (hasChinese(time)) {
            dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        } else {
            dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        }

        long timestamp = 0L;

        try {
            timestamp = dateFormat.parse(time).getTime();
            // logger.info("execute getTimestamp is : " + timestamp);
        } catch (ParseException var7) {
            // logger.error("execute getTimestamp error : " + var7);
            var7.printStackTrace();
        }

        String timeStamp = String.valueOf(timestamp);
        return timeStamp;
    }

    public static Date getDateFromDateString(String format, String time) {
        // ILogger logger = Logger.getLogger();
        SimpleDateFormat dateFormat;
        if (hasChinese(time)) {
            dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        } else {
            dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        }

        long timestamp = 0L;
        Date date = null;

        try {
            date = dateFormat.parse(time);
            // logger.info("get Date is : " + date.getDate());
        } catch (ParseException var8) {
            // logger.error("execute getDate error : " + var8);
            var8.printStackTrace();
        }

        return date;
    }

    public static boolean hasChinese(String str) {
        String regex = "[一-龥]";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return match.find();
    }

    public static String convertTimeFormat(String sFormat_bef, String sTime, String sFormat_aft) {
        SimpleDateFormat df = new SimpleDateFormat(sFormat_bef);

        Date date = new Date();
        try {
            date = df.parse(sTime);
        } catch (ParseException var7) {
            // throw ExceptionHandles.build("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var7);
        }

        SimpleDateFormat df2 = new SimpleDateFormat(sFormat_aft);
        String sTime_aft = df2.format(date);
        return sTime_aft;
    }

    public static String stampMsToDate(String sFormat, String sTimeMs) {
        String dt = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        long lt = new Long(sTimeMs);
        Date date = new Date(lt);

        try {
            dt = simpleDateFormat.format(date);
            return dt;
        } catch (NumberFormatException var8) {
            throw new ServiceException("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var8);
            // throw ExceptionHandles.build("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var8);
        }
    }

    public static String stampToDate(String sFormat, String sTime) {
        String dt = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        long lt = new Long(sTime) * 1000L;
        Date date = new Date(lt);

        try {
            dt = simpleDateFormat.format(date);
            return dt;
        } catch (NumberFormatException var8) {
            throw new ServiceException("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var8);
            //ExceptionHandles.build("源时间数据解析格式错误，请确定源时间数据的格式是否正确：", var8);
        }
    }

    public static boolean verifyString(String sMode, String sExceptText, String sActualText, boolean bMatched) {
        //ILogger logger = Logger.getLogger();
        boolean bIsMatched = false;
        if (sMode.equals("精确匹配")) {
            if (sExceptText.equals("String.Null")) {
                bIsMatched = sActualText == null || sActualText.equals("String.Null");
            } else if (sExceptText.equals("String.Empty")) {
                bIsMatched = sActualText.length() == 0;
            } else {
                bIsMatched = sActualText.equals(sExceptText);
            }
        } else if (sMode.equals("忽略大小写")) {
            bIsMatched = sActualText.equalsIgnoreCase(sExceptText);
        } else if (sMode.equals("模糊匹配")) {
            bIsMatched = sActualText.contains(sExceptText);
        } else if (sMode.equals("多值匹配")) {
            String[] sExceptTextArr = sExceptText.split(",");
            String[] var10 = sExceptTextArr;
            int var9 = sExceptTextArr.length;

            for(int var8 = 0; var8 < var9; ++var8) {
                String exceptText = var10[var8];
                if (exceptText.trim().equals("String.Null")) {
                    if (sActualText == null) {
                        bIsMatched = true;
                        break;
                    }
                } else if (exceptText.trim().equals("String.Empty")) {
                    if (sActualText.length() == 0) {
                        bIsMatched = true;
                        break;
                    }
                } else if (sActualText.equals(exceptText.trim())) {
                    bIsMatched = true;
                    break;
                }
            }
        } else if (sMode.equals("正则表达式匹配")) {
            bIsMatched = sActualText.matches(sExceptText);
        } else {
            int res;
            if (sMode.equals("大于")) {
                // logger.info("期望实际结果" + sActualText + "大于预期结果" + sExceptText + ".");
                res = sActualText.compareTo(sExceptText);
                bIsMatched = res > 0;
            } else if (sMode.equals("小于")) {
                // logger.info("期望实际结果" + sActualText + "小于预期结果" + sExceptText + ".");
                res = sActualText.compareTo(sExceptText);
                bIsMatched = res < 0;
            } else if (sMode.equals("大于等于")) {
                // logger.info("期望实际结果" + sActualText + "大于/等于预期结果" + sExceptText + ".");
                res = sActualText.compareTo(sExceptText);
                bIsMatched = res >= 0;
            } else if (sMode.equals("小于等于")) {
                // logger.info("期望实际结果" + sActualText + "小于/等于预期结果" + sExceptText + ".");
                res = sActualText.compareTo(sExceptText);
                bIsMatched = res <= 0;
            } else {
                bIsMatched = sActualText.equals(sExceptText);
            }
        }

        if (bIsMatched != bMatched) {
            throw new ServiceException("校验文本内容失败, 实际值是[" + sActualText + "], 期望值是[" + sExceptText + "],预期匹配状态是[" + bMatched + "].");
            // throw new VerifyException("校验文本内容失败, 实际值是[" + sActualText + "], 期望值是[" + sExceptText + "],预期匹配状态是[" + bMatched + "].", sActualText, sExceptText);
        } else {
            return true;
        }
    }

    public static void logPrint(String sLogLevel, String sMessage) {
        // ILogger logger = Logger.getLogger();
        if (sLogLevel.equals("info")) {
            // logger.info(sMessage);
        } else if (sLogLevel.equals("debug")) {
            // logger.debug(sMessage);
        } else if (sLogLevel.equals("warn")) {
            // logger.warn(sMessage);
        } else if (sLogLevel.equals("error")) {
            // logger.error(sMessage);
        } else {
            if (!sLogLevel.equals("trace")) {
                throw new ServiceException("不支持： " + sLogLevel + "级别日志打印,请检查！");
                // throw ExceptionHandles.getUnsupportException("不支持： " + sLogLevel + "级别日志打印,请检查！");
            }

            // logger.trace(sMessage);
        }

    }

    public static String getLocalHostIPAddress() {
        // ILogger logger = Logger.getLogger();
        String ipAddr = "";

        try {
            InetAddress addr = InetAddress.getLocalHost();
            ipAddr = addr.getHostAddress();
            // logger.info("获取的本机IP地址是： " + ipAddr);
            return ipAddr;
        } catch (UnknownHostException var3) {
            throw new ServiceException("获取本机IP地址失败，请检查.\r\n", var3);
            // throw ExceptionHandles.build("获取本机IP地址失败，请检查.\r\n", var3);
        }
    }

    public static String splitAndGetLength(String sSourceStr, String sSplitStr) {
        // ILogger logger = Logger.getLogger();
        // logger.trace("处理前分隔符是：" + sSplitStr);
        // logger.trace("regexChars：$()*+.[?^{|\\ ");
        if (!sSplitStr.contains("\\n") && !sSplitStr.contains("\\r") && !sSplitStr.contains("\\s") && !sSplitStr.contains("\\S") && !sSplitStr.contains("\\t") && !sSplitStr.contains("\\v")) {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < sSplitStr.length(); ++i) {
                if ("$()*+.[?^{|\\ ".contains(String.valueOf(sSplitStr.charAt(i)))) {
                    stringBuilder.append("\\").append(sSplitStr.charAt(i));
                } else {
                    stringBuilder.append(sSplitStr.charAt(i));
                }
            }

            sSplitStr = stringBuilder.toString();
            // logger.trace("处理后分隔符是：" + sSplitStr);
        } else {
            // logger.trace("处理后分隔符是：" + sSplitStr);
        }

        String[] strs = sSourceStr.split(sSplitStr, -1);
        String sLength = String.valueOf(strs.length);
        return sLength;
    }

    public static String splitAndGetValue(String sSourceStr, String sSplitStr, Integer sPointer) {
        // ILogger logger = Logger.getLogger();
        // logger.trace("处理前分隔符是：" + sSplitStr);
        if (!sSplitStr.contains("\\n") && !sSplitStr.contains("\\r") && !sSplitStr.contains("\\s") && !sSplitStr.contains("\\S") && !sSplitStr.contains("\\t") && !sSplitStr.contains("\\v")) {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < sSplitStr.length(); ++i) {
                if ("$()*+.[?^{|\\ ".contains(String.valueOf(sSplitStr.charAt(i)))) {
                    stringBuilder.append("\\").append(sSplitStr.charAt(i));
                } else {
                    stringBuilder.append(sSplitStr.charAt(i));
                }
            }

            sSplitStr = stringBuilder.toString();
            // logger.trace("处理后分隔符是：" + sSplitStr);
        } else {
            // logger.trace("处理后分隔符是：" + sSplitStr);
        }

        String[] strs = sSourceStr.split(sSplitStr, -1);
        if (sPointer - 1 <= strs.length - 1) {
            return strs[sPointer - 1];
        } else {
            throw new ServiceException("您输入的位置超出分割后的数组长度，请检查!数组长度为：" + strs.length);
            // throw ExceptionHandles.getUnsupportException("您输入的位置超出分割后的数组长度，请检查!数组长度为：" + strs.length);
        }
    }

    public static String creatRandomNum(int iLen) {
        // int maxNum = true;
        int count = 0;
        char[] str = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer sRandom = new StringBuffer("");
        Random r = new Random();

        while(count < iLen) {
            int i = r.nextInt(36);
            if (i >= 0 && i < str.length) {
                sRandom.append(str[i]);
                ++count;
            }
        }

        return sRandom.toString();
    }

    public static String getEmailAddress(String sInternet) {
        String email = "auto_";
        Random rd1 = new Random();

        for(int i = 0; i < 6; ++i) {
            email = email + String.valueOf(rd1.nextInt(10));
        }

        email = email + sInternet;
        return email;
    }

    public static String getTelephoneNumber() {
        String telephone = "1";
        Random rd1 = new Random();
        telephone = telephone + String.valueOf(rd1.nextInt(3)) + "0000";

        for(int i = 0; i < 5; ++i) {
            telephone = telephone + String.valueOf(rd1.nextInt(10));
        }

        return telephone;
    }

    public static String dataCalc(String sOperator, String sData1, String sData2, String sData3, String rangeString) {
        // ILogger logger = Logger.getLogger();
        BigDecimal v1 = new BigDecimal(sData1);
        BigDecimal v2 = new BigDecimal(sData2);
        int v3 = Integer.parseInt(sData3);
        String sTargetData = "";
        if (sOperator.equals("加")) {
            sTargetData = v1.add(v2).toString();
        } else if (sOperator.equals("减")) {
            sTargetData = v1.subtract(v2).toString();
        } else if (sOperator.equals("乘")) {
            sTargetData = v1.multiply(v2).toString();
        } else if (sOperator.equals("除")) {
            try {
                sTargetData = v1.divide(v2).toString();
                if (rangeString.equals("除法计算")) {
                    BigDecimal Sdata = new BigDecimal(sTargetData);
                    Sdata = Sdata.setScale(v3, 4);
                    sTargetData = Sdata.toString();
                }
            } catch (ArithmeticException var11) {
                // logger.info("出现ArithmeticException错误,请检查是否不可被整除.");
                sTargetData = v1.divide(v2, v3, 4).toString();
            }
        } else {
            if (!sOperator.equals("取余")) {
                throw new ServiceException("找不到运算方式： " + sOperator + ",请检查！");
                // throw ExceptionHandles.getUnsupportException("找不到运算方式： " + sOperator + ",请检查！");
            }

            sTargetData = v1.divideAndRemainder(v2)[1].toString();
        }

        return sTargetData;
    }

    public static String roundValue(String sValue, String sLength) {
        // ILogger logger = Logger.getLogger();
        BigDecimal v1 = new BigDecimal(sValue);
        int length = Integer.parseInt(sLength);
        String sTargetData = "";

        try {
            sTargetData = v1.setScale(length, 4).toString();
            // logger.info("execute setScale is : " + sTargetData);
        } catch (Exception var7) {
            // logger.error("execute setScale error : " + var7);
            var7.printStackTrace();
        }

        return sTargetData;
    }

    public static boolean compareToData(String sData1, String sData2, String sResult_exp) {
        // ILogger logger = Logger.getLogger();

        int result_act;
        try {
            BigDecimal v1 = new BigDecimal(sData1);
            BigDecimal v2 = new BigDecimal(sData2);
            result_act = v1.compareTo(v2);
        } catch (NumberFormatException var8) {
            String message = "数值转换失败，请确保输入的参数值为整型或浮点型数值字符串，且去除了空格.其中data1=" + sData1 + ",data2=" + sData2;
            throw new ServiceException(message, var8);
            // throw ExceptionHandles.build(message, var8);
        }

        String sResult_act;
        if (result_act == 0) {
            sResult_act = "等于";
        } else if (result_act == 1) {
            sResult_act = "大于";
        } else {
            sResult_act = "小于";
        }

        // logger.info(sData1 + "和" + sData2 + "比较的实际结果是: " + sData1 + sResult_act + sData2);
        if (sResult_exp.equals(sResult_act)) {
            // logger.info("成功比较" + sData1 + "和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("不等于") && result_act != 0) {
            // logger.info("成功比较" + sData1 + "和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("大于等于") && result_act >= 0) {
            // logger.info("成功比较" + sData1 + "和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("小于等于") && result_act <= 0) {
            // logger.info("成功比较" + sData1 + "和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else {
            throw new ServiceException("比较" + sData1 + "和" + sData2 + "大小失败,实际值是[" + sResult_act + "]，期望值是[" + sResult_exp + "].");
            // throw ExceptionHandles.getVerifyException("比较" + sData1 + "和" + sData2 + "大小失败,实际值是[" + sResult_act + "]，期望值是[" + sResult_exp + "].");
        }
    }

    public static boolean strcompareToData(String sData1, String sData2, String sResult_exp) {
        // ILogger logger = Logger.getLogger();

        int result_act;
        try {
            BigDecimal v1 = new BigDecimal(sData1);
            BigDecimal v2 = new BigDecimal(sData2);
            result_act = v1.compareTo(v2);
        } catch (NumberFormatException var8) {
            String message = "数值转换失败，请确保输入的参数值为整型或浮点型数值字符串，且去除了空格.其中data1=" + sData1 + ",data2=" + sData2;
            throw new ServiceException(message, var8);
            // throw ExceptionHandles.build(message, var8);
        }

        String sResult_act;
        if (result_act == 0) {
            sResult_act = "等于";
        } else if (result_act == 1) {
            sResult_act = "大于";
        } else {
            sResult_act = "小于";
        }

        // logger.info("字符串长度和" + sData2 + "比较的实际结果是: " + "字符串长度" + sResult_act + sData2);
        if (sResult_exp.equals(sResult_act)) {
            //  logger.info("成功比较字符串长度和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("不等于") && result_act != 0) {
            // logger.info("成功比较字符串长度和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("大于等于") && result_act >= 0) {
            // logger.info("成功比较字符串长度和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else if (sResult_exp.equals("小于等于") && result_act <= 0) {
            // logger.info("成功比较字符串长度和" + sData2 + "大小，期望值与实际值相符.");
            return true;
        } else {
            throw new ServiceException("比较字符串长度和" + sData2 + "大小失败,实际值是[" + sResult_act + "]，期望值是[" + sResult_exp + "].");
            // throw ExceptionHandles.getVerifyException("比较字符串长度和" + sData2 + "大小失败,实际值是[" + sResult_act + "]，期望值是[" + sResult_exp + "].");
        }
    }

    public static boolean compareformvalue(String formstr, String formkey, String expvalue) {
        Boolean result_act = false;
        String sResult_act = null;
        Map<String, String> map = new HashMap();
        // ILogger logger = Logger.getLogger();
        String[] formstrs = formstr.split("[?]");
        String[] strs = formstrs[1].split("&");

        for(int i = 0; i < strs.length; ++i) {
            String[] str = strs[i].split("=");
            map.put(str[0], str[1]);
        }

        Iterator var12 = map.keySet().iterator();

        while(var12.hasNext()) {
            String key = (String)var12.next();
            if (key.equals(formkey)) {
                result_act = true;
                sResult_act = (String)map.get(key);
            }
        }

        if (result_act) {
            if (sResult_act.equals(expvalue)) {
                // logger.info("key值:" + formkey + "所对应的value值是:" + sResult_act + " 与预期值相符");
                return true;
            } else {
                throw new ServiceException("key值:" + formkey + "所对应的value值是:" + sResult_act + " 与预期值不符");
                // throw ExceptionHandles.getVerifyException("key值:" + formkey + "所对应的value值是:" + sResult_act + " 与预期值不符");
            }
        } else {
            throw new ServiceException("不存在key值:" + formkey + " 请确定是否输入有误");
            // throw ExceptionHandles.getVerifyException("不存在key值:" + formkey + " 请确定是否输入有误");
        }
    }

    public static String geturlvalue(String urlstr, String urlkey) {
        Boolean result_act = false;
        String sResult_act = null;
        Map<String, String> map = new HashMap();
        String[] urlstrs = urlstr.split("[?]");
        String[] strs = urlstrs[1].split("&");

        for(int i = 0; i < strs.length; ++i) {
            String[] str = strs[i].split("=");
            map.put(str[0], str[1]);
        }

        Iterator var10 = map.keySet().iterator();

        while(var10.hasNext()) {
            String key = (String)var10.next();
            if (key.equals(urlkey)) {
                result_act = true;
                sResult_act = (String)map.get(key);
            }
        }

        if (result_act) {
            return sResult_act;
        } else {
            throw new ServiceException("不存在key值:" + urlkey + " 请确定是否输入有误");
            // throw ExceptionHandles.getVerifyException("不存在key值:" + urlkey + " 请确定是否输入有误");
        }
    }

    public static void createCSVToSaveData(String sFilePosition, String sFilePath, String sTableHead, String sTableData) {
        // ILogger logger = Logger.getLogger();
        String path = "";

        try {
            if (sFilePosition.equals("本地")) {
                path = sFilePath;
            } else if (sFilePosition.equals("工程")) {
                // path = ProjectRunner.getProjectPath() + sFilePath;
            }

            // logger.info("待生成的CSV文件路径及名称为：" + path);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, true), "GBK"));
            StringBuffer sbAccount = new StringBuffer();
            sbAccount.append(sTableHead).append("\n");
            String[] data = sTableData.split("\\|");

            for(int i = 0; i < data.length; ++i) {
                sbAccount.append(data[i]).append("\n");
            }

            // logger.info("待写入CSV文件中的内容是：" + sbAccount.toString());
            bw.write(sbAccount.toString());
            bw.close();
        } catch (IOException var10) {
            throw new ServiceException("生成及写入CSV文件出现错误，请检查！", var10);
            // throw ExceptionHandles.build("生成及写入CSV文件出现错误，请检查！", var10);
        }
    }

    private static String[][] getPxXy(String imgPath) {
        int[] rgb = new int[3];
        File file = new File(imgPath);
        BufferedImage bi = null;

        try {
            bi = ImageIO.read(file);
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        String[][] list = new String[width - minx][height - miny];

        for(int i = minx; i < width; ++i) {
            for(int j = miny; j < height; ++j) {
                int pixel = bi.getRGB(i, j);
                rgb[0] = (pixel & 16711680) >> 16;
                rgb[1] = (pixel & '\uff00') >> 8;
                rgb[2] = pixel & 255;
                list[i][j] = String.valueOf(rgb[0]) + String.valueOf(rgb[1]) + rgb[2];
            }
        }

        return list;
    }

    public static void main(String[] args) throws ParseException {
        String format = "yyyy-MM-dd HH:mm:ss";
        String sTimeMs = "1524499200000";
        String sTime = "1524499200";
        stampMsToDate(format, sTimeMs);
        stampToDate(format, sTime);
        System.out.println(sTime.substring(sTime.length() - 3));
    }
}
