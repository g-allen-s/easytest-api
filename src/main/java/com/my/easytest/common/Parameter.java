package com.my.easytest.common;

import com.my.easytest.exception.ParameterException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Parameter {
    private Map<String, Object> moParams = new HashMap();

    public Parameter() {
    }

    public void setParam(String psKey, Object psValue) {
        this.moParams.put(psKey, psValue);
    }

    public int getInt(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to integer with param<" + psKey + ">.");
        }
    }

    public Integer getNoneEmptyInt(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return !sValue.equals("") && !sValue.equals("null") && !sValue.equals("N/A") ? Integer.parseInt(sValue) : null;
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to integer with param<" + psKey + ">.");
        }
    }

    public Object getObject(String psKey) {
        Object oValue = this.moParams.get(psKey);
        if (oValue == null) {
            throw new ParameterException("Param<" + psKey + "> is not exist.");
        } else {
            return oValue;
        }
    }

    public String getString(String psKey, String defaultString) {
        String sValue = defaultString;

        try {
            sValue = this.getString(psKey);
        } catch (Exception var5) {
        }

        return sValue;
    }

    public String getString(String psKey) {
        try {
            return new String(this.getObject(psKey).toString().getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            return this.getObject(psKey).toString();
        }
    }

    public String getNoneEmptyString(String psKey) {
        String sValue = this.getString(psKey);
        return !sValue.equals("null") && !sValue.equals("N/A") ? sValue : null;
    }

    public float getFloat(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Float.parseFloat(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to float with param<" + psKey + ">.");
        }
    }

    public long getLong(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Long.parseLong(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to long with param<" + psKey + ">.");
        }
    }

    public Long getNoneEmptyLong(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return !sValue.equals("") && !sValue.equals("null") && !sValue.equals("N/A") ? Long.parseLong(sValue) : null;
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to long with param<" + psKey + ">.");
        }
    }

    public double getDouble(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Double.parseDouble(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to double with param<" + psKey + ">.");
        }
    }

    public short getShort(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Short.parseShort(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to short with param<" + psKey + ">.");
        }
    }

    public byte getByte(String psKey) {
        String sValue = this.getString(psKey);

        try {
            return Byte.parseByte(sValue);
        } catch (NumberFormatException var4) {
            throw new ParameterException("Failed to convert the value<" + sValue + "> to byte with param<" + psKey + ">.");
        }
    }

    public boolean getBoolean(String psKey) {
        String sValue = this.getString(psKey);
        return Boolean.valueOf(sValue);
    }

    public Boolean getNoneEmptyBoolean(String psKey) {
        String sValue = this.getString(psKey);
        return !sValue.equals("") && !sValue.equals("null") && !sValue.equals("N/A") ? Boolean.valueOf(sValue) : null;
    }

    public Boolean containsKey(String psKey) {
        return this.moParams.containsKey(psKey);
    }

    public Map<String, Object> getParams() {
        return this.moParams;
    }

    public void setParams(Map<String, Object> poParams) {
        this.moParams = poParams;
    }

    public Parameter clone() {
        Parameter newParam = new Parameter();
        Map<String, Object> newMap = new HashMap();
        newMap.putAll(this.getParams());
        newParam.setParams(newMap);
        return newParam;
    }

    public boolean isTestRunnerClient() {
        return this.containsKey("client_type") && "TestRunner".equals(this.moParams.get("client_type"));
    }

    public boolean isBusinessTask() {
        return this.containsKey("plan_type") && "Business".equals(this.moParams.get("plan_type"));
    }

    public String toString() {
        Map<String, Object> map = this.getParams();
        String string = "{";
        Iterator it = map.entrySet().iterator();

        while(it.hasNext()) {
            Entry e = (Entry)it.next();
            if (!e.getKey().equals("testrunner_path") && !e.getKey().equals("plan_type")) {
                string = string + "\"" + e.getKey() + "\":";
                if (e.getKey().toString().equals("password")) {
                    string = string + "\"*****\",";
                } else if (e.getValue().toString().indexOf("{") == 0) {
                    string = string + "[";
                    string = string + e.getValue().toString().replaceAll("'", "\"");
                    string = string + "],";
                } else if (e.getValue().toString().indexOf("[") == 0) {
                    string = string + e.getValue().toString().replaceAll("'", "\"") + ",";
                } else if (e.getValue().toString().indexOf("<?xml") == 0) {
                    //string = string + FileUtil.xml2JSON(e.getValue().toString()) + ",";
                } else {
                    string = string + "\"" + e.getValue().toString().replaceAll("'", "\\\"").replaceAll("\"", "\\\\\"") + "\",";
                }
            }
        }

        if (string.lastIndexOf(",") != -1) {
            string = string.substring(0, string.lastIndexOf(","));
        }

        string = string + "}";
        return string;
    }

}
