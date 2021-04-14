package com.my.easytest.client;

import com.my.easytest.common.Parameter;
import com.my.easytest.config.ConnectorConfig;
import com.my.easytest.exception.ServiceException;
import com.my.easytest.util.StrUtil;
import com.my.easytest.util.WindQUtil;
import com.suning.windq.jms.WindQConnectionFactory;
import com.suning.windq.jms.destination.WindQDestination;
import com.suning.windq.jms.destination.WindQQueue;
import com.suning.windq.jms.destination.WindQTopic;
import net.sf.json.JSONObject;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import javax.jms.*;
import java.util.*;


public class WindQClient {
    private static XMLOutputter moXMLOutputter;
    public static final Map<String, List<String>> ldcRuleMap;


    public static void sendMessage(Parameter poParam) {
        Object oRequest = poParam.getObject("request"); // 消息内容
        String destinationName = poParam.getString("destination"); // 目的地：主题或队列名称
        String model = poParam.getString("model"); // 模型：Topic or Queue
        String sEnvironment = poParam.getString("environment"); // 测试环境
        String sAttachmentProperties = poParam.getString("attachmentProperties"); // 附件
        final String sRequest;
        if (oRequest instanceof Document) {
            sRequest = moXMLOutputter.outputString((Document)oRequest);
        } else {
            sRequest = oRequest.toString();
        }

        if (poParam.isTestRunnerClient()) {
            String env = ConnectorConfig.instance().getStringValue("env");
            if (StrUtil.isEmpty(env)) {
                env = "NJXZ";
            }

            if (!checkLdc(env, sEnvironment)) {
                throw new ServiceException("windq发送失败，请检查tea.properties的env、rsf.ldc字段是否设置正确,有可能设置错误或者跨机房调用");
            }
        }

        final Map<String, String> propertieMap = getProperties(sAttachmentProperties);
        WindQDestination destination = null;
        if (model.equals("Topic")) {
            destination = new WindQTopic(destinationName);
        } else {
            if (!model.equals("Queue")) {
                throw new RuntimeException("发送模型配置错误（Topic/Queue），请检查");
            }

            destination = new WindQQueue(destinationName);
        }

        WindQConnectionFactory oWindQConnectionFactory = null;

        try {
            oWindQConnectionFactory = WindQUtil.getWindQConnectionFactory(sEnvironment);
            JmsTemplate oJmsTemplate = new JmsTemplate();
            oJmsTemplate.setConnectionFactory(oWindQConnectionFactory);
            oJmsTemplate.send((Destination)destination, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage();
                    textMessage.clearProperties();
                    Iterator var3 = propertieMap.keySet().iterator();

                    while(var3.hasNext()) {
                        String key = (String)var3.next();
                        textMessage.setStringProperty(key, (String)propertieMap.get(key));
                    }
                    textMessage.setText(sRequest);
                    return textMessage;
                }
            });
        } catch (Exception var14) {
            throw new RuntimeException("Send Message Failed, for:" + var14);
        } finally {
            if (null != oWindQConnectionFactory) {
                oWindQConnectionFactory.destroy();
            }

        }
    }

    public static boolean checkLdc(String ldc, String env) {
        if (!StrUtil.isEmpty(ldc) && !StrUtil.isEmpty(env)) {
            ldc = ldc.toUpperCase();
            env = env.toUpperCase();
            if (ldc.contains("POC")) {
                ldc = "NJXG_POC";
            }

            if (ldcRuleMap.containsKey(ldc)) {
                List<String> list = (List)ldcRuleMap.get(ldc);
                return list.contains(env);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    static {
        Format oXmlFormatter = Format.getCompactFormat();
        oXmlFormatter.setEncoding("UTF-8");
        oXmlFormatter.setIndent("    ");
        oXmlFormatter.setTextMode(TextMode.PRESERVE);
        moXMLOutputter = new XMLOutputter(oXmlFormatter);
        ldcRuleMap = new HashMap<String, List<String>>() {
            {
                this.put("NJXZ", Arrays.asList("DEV", "SIT", "XZPRE"));
                this.put("NJYH", Arrays.asList("PRD"));
                this.put("NJXG_POC", Arrays.asList("POC"));
                this.put("NJXG", Arrays.asList("XGPRE"));
                this.put("NJXG_PST", Arrays.asList("PST"));
            }
        };
    }

    private static Map<String, String> getProperties(String jsonStr) {
        Map<String, String> propMap = new HashMap();
        if (null != jsonStr && !StrUtil.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = JSONObject.fromObject(jsonStr);
                Iterator iterator = jsonObject.keys();

                while(iterator.hasNext()) {
                    String key = (String)iterator.next();
                    String value = jsonObject.getString(key);
                    propMap.put(key, value);
                }

                return propMap;
            } catch (Exception var6) {
                throw new RuntimeException("jsonObject 解析失败，请检查附件属性的json对象格式:" + var6);
            }
        } else {
            return propMap;
        }
    }


    public static void main(String[] args) {
    }

}
