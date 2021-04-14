package com.my.easytest.enums;

import com.my.easytest.util.StrUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public enum JavaBaseType {
    _int("int"),
    _long("long"),
    _float("float"),
    _double("double"),
    _boolean("boolean"),
    _byte("byte"),
    _short("short"),
    _char("char"),
    _String("String"),
    _BIGDECIMAL("BigDecimal"),
    _Byte("Byte"),
    _Short("Short"),
    _Integer("Integer"),
    _Long("Long"),
    _Float("Float"),
    _Double("Double"),
    _Boolean("Boolean"),
    _BigInteger("BigInteger");

    private String msName;
    private static final Map<String, JavaBaseType> nameToEnum = new HashMap();

    private JavaBaseType(String psName) {
        this.msName = psName;
    }

    public Object getInitValueObject() {
        Object res = null;
        JavaBaseType type = fromName(this.msName);
        switch(type.ordinal()) {
            case 0:
                res = Integer.valueOf(0);
                break;
            case 1:
                res = Integer.valueOf(0).longValue();
                break;
            case 2:
                res = Float.valueOf(0.0F);
                break;
            case 3:
                res = Double.valueOf(0.0D);
                break;
            case 4:
                res = Boolean.valueOf(false);
                break;
            case 5:
                res = Byte.valueOf((byte)0);
                break;
            case 6:
                res = Short.valueOf("0");
                break;
            case 7:
                res = "0".toCharArray()[0];
                break;
            case 8:
                res = new String("StringValue");
                break;
            case 9:
                res = BigDecimal.valueOf(Long.valueOf("0"));
                break;
            case 10:
                res = Byte.valueOf("0");
                break;
            case 11:
                res = Short.valueOf("0");
                break;
            case 12:
                res = 0;
                break;
            case 13:
                res = 0L;
                break;
            case 14:
                res = 0.0F;
                break;
            case 15:
                res = 0.0D;
                break;
            case 16:
                res = false;
                break;
            case 17:
                res = BigInteger.valueOf(Long.valueOf("0"));
                break;
            default:
                res = new String("StringValue");
        }

        return res;
    }

    public Object valueObject(String psInputValue) {
        Object res = null;
        JavaBaseType type = fromName(this.msName);
        switch(type.ordinal()) {
            case 0:
                res = StrUtil.isEmpty(psInputValue) ? 0 : Integer.valueOf(psInputValue);
                break;
            case 1:
                res = StrUtil.isEmpty(psInputValue) ? 0L : Integer.valueOf(psInputValue).longValue();
                break;
            case 2:
                res = StrUtil.isEmpty(psInputValue) ? 0.0F : Float.valueOf(psInputValue);
                break;
            case 3:
                res = StrUtil.isEmpty(psInputValue) ? 0.0D : Double.valueOf(psInputValue);
                break;
            case 4:
                res = StrUtil.isEmpty(psInputValue) ? Boolean.FALSE : Boolean.valueOf(psInputValue);
                break;
            case 5:
                res = StrUtil.isEmpty(psInputValue) ? 0 : Byte.valueOf(psInputValue);
                break;
            case 6:
                res = StrUtil.isEmpty(psInputValue) ? Short.valueOf("0") : Short.valueOf(psInputValue);
                break;
            case 7:
                res = psInputValue.toCharArray()[0];
                break;
            case 8:
                res = StrUtil.isEmpty(psInputValue) ? new String("") : new String(psInputValue);
                break;
            case 9:
                res = BigDecimal.valueOf(Long.valueOf(psInputValue));
                break;
            case 10:
                res = Byte.valueOf(psInputValue);
                break;
            case 11:
                res = Short.valueOf(psInputValue);
                break;
            case 12:
                res = StrUtil.isEmpty(psInputValue) ? 0 : Integer.valueOf(psInputValue);
                break;
            case 13:
                res = StrUtil.isEmpty(psInputValue) ? 0L : Long.valueOf(psInputValue);
                break;
            case 14:
                res = StrUtil.isEmpty(psInputValue) ? Float.valueOf("0.0") : Float.valueOf(psInputValue);
                break;
            case 15:
                res = StrUtil.isEmpty(psInputValue) ? 0.0D : Double.valueOf(psInputValue);
                break;
            case 16:
                res = StrUtil.isEmpty(psInputValue) ? Boolean.FALSE : Boolean.valueOf(psInputValue);
                break;
            case 17:
                res = new BigInteger(psInputValue);
                break;
            default:
                res = new String(psInputValue);
        }

        return res;
    }

    public Class<?> getBaseClassType() {
        Class<?> res = null;
        JavaBaseType type = fromName(this.msName);
        switch(type.ordinal()) {
            case 0:
                res = Integer.TYPE;
                break;
            case 1:
                res = Long.TYPE;
                break;
            case 2:
                res = Float.TYPE;
                break;
            case 3:
                res = Double.TYPE;
                break;
            case 4:
                res = Boolean.TYPE;
                break;
            case 5:
                res = Byte.TYPE;
                break;
            case 6:
                res = Short.TYPE;
                break;
            case 7:
                res = Character.TYPE;
                break;
            case 8:
                res = String.class;
                break;
            case 9:
                res = BigDecimal.class;
                break;
            case 10:
                res = Byte.class;
                break;
            case 11:
                res = Short.class;
                break;
            case 12:
                res = Integer.class;
                break;
            case 13:
                res = Long.class;
                break;
            case 14:
                res = Float.class;
                break;
            case 15:
                res = Double.class;
                break;
            case 16:
                res = Boolean.class;
                break;
            case 17:
                res = BigInteger.class;
                break;
            default:
                res = String.class;
        }

        return res;
    }

    public static boolean isJavaBase(String name) {
        return StrUtil.isNotEmpty(name) && nameToEnum.containsKey(name);
    }

    public static JavaBaseType fromName(String name) {
        return (JavaBaseType)nameToEnum.get(name.trim());
    }

    static {
        JavaBaseType[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            JavaBaseType type = var0[var2];
            nameToEnum.put(type.msName, type);
        }

    }
}

