package com.my.easytest.constants;

public class SCMConstants {
    public static final String SCM_SERVER_DEV = "http://scmdev";
    public static final String SCM_SERVER_SIT = "http://scmsit";
    public static final String SCM_SERVER_PRE = "http://scmpre";
    public static final String SCM_SERVER_XG_PRE = "http://scmxgpre";
    public static final String SCM_SERVER_PST = "http://scmpst";
    public static final String SCM_SERVER_POC = "http://scmpoc";
    public static final String SCM_SERVER_SITE = "http://scmsite";
    public static final String SCM_SERVER_SIT2 = "http://scmsit2";
    public static final String SCM_PROPERTIES = "/scm.properties";
    public static final String SCM_DEV_PROPERTIES = "/scmdev.properties";
    public static final String SCM_SIT_PROPERTIES = "/scmsit.properties";
    public static final String SCM_PRE_PROPERTIES = "/scmpre.properties";
    public static final String SCM_POC_PROPERTIES = "/scmpoc.properties";
    public static final String SCM_XGPRE_PROPERTIES = "/scmxgpre.properties";
    public static final String SCM_PST_PROPERTIES = "/scmpst.properties";
    public static final String SCM_PRD_PROPERTIES = "/scmprd.properties";
    public static final String SCM_SITE_PROPERTIES = "/scmsite.properties";
    public static final String SCM_SIT2_PROPERTIES = "/scmsit2.properties";
    public static final String RSF_ADMIN_URL_DEV = "http://rsfdev.cnsuning.com";
    public static final String RSF_ADMIN_URL_SIT = "http://rsfsit.cnsuning.com";
    public static final String RSF_ADMIN_URL_PRE = "http://rsfpre.cnsuning.com";
    public static final String RSF_ADMIN_URL_XG_PRE = "http://rsfprexg.cnsuning.com";
    public static final String RSF_ADMIN_URL_PRD = "http://rsf.cnsuning.com";
    public static final String RSF_ADMIN_URL_SITE = "http://rsfsite.cnsuning.com";
    public static final String WINDQ_SCM_DEV_PROPERTIES = "/windqscmdev.properties";
    public static final String WINDQ_SCM_SIT_PROPERTIES = "/windqscmsit.properties";
    public static final String WINDQ_SCM_XGPRE_PROPERTIES = "/windqscmxgpre.properties";
    public static final String WINDQ_SCM_XZPRE_PROPERTIES = "/windqscmxzpre.properties";
    public static final String WINDQ_SCM_POC_PROPERTIES = "/windqscmpoc.properties";

    public SCMConstants() {
    }

    public static enum Environment {
        DEV,
        SIT,
        SIT2,
        PRE,
        XGPRE,
        PRD,
        PST,
        POC,
        SITE,
        XZPRE;

        private Environment() {
        }
    }
}
