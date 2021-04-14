package com.my.easytest.enums;

public enum Reason {
    SCRIPT("脚本问题"),
    ENV("环境问题"),
    DATA("数据问题"),
    FRAMEWORK("平台问题"),
    TESTRUNNER("执行机问题"),
    BUG("应用功能缺陷"),
    BUSINESS("业务变更"),
    OUTSIDEENV("网络问题"),
    RSF("RSF调用异常"),
    PLATFORM("平台执行问题"),
    UNKNOWN("其他原因");

    private String name;

    private Reason(String psName) {
        this.name = psName;
    }

    public String getReasonCnName() {
        return this.name;
    }
}
