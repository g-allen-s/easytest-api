package com.my.easytest.client;

import com.my.easytest.exception.ServiceException;
import com.my.easytest.model.result.TestActionStepResult;
import com.my.easytest.dto.ResultDto;
import com.my.easytest.entity.TestApi;
import com.my.easytest.entity.TestApiAction;
import com.my.easytest.entity.TestApiHeader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Component
@Slf4j
public class RestassuredClient {

    private String method;

    private String requestUrl;

    private RequestSpecification request;

    private Response response;

    private List<TestApiAction> afterRequestSteps;


    final static String ASSERTION_PASSED = "Assertion Passed";

    final static String ASSERTION_FAILED = "Assertion Failed";

    /**
     * 发送请求前的预置操作
     * @param api
     * @param host
     * @param headers
     * @param apiActionSteps
     * @param globalParams
     * @return
     */
    public RestassuredClient preHandle(TestApi api, String host, List<TestApiHeader> headers, List<TestApiAction> apiActionSteps, Map<String, Object> globalParams)
    {
        afterRequestSteps = new ArrayList<TestApiAction>(); // todo
        // 获取请求方式和请求地址
        this.method = api.getApiMethod();
        this.requestUrl = host + api.getApiCode();
        // RestAssured.given对象
        request = RestAssured.given();
        // 设置请求头
        this.setRequestHeaderSimple(headers);
        // 解析步骤
        Map<String, Object> bodyParams = this.dealBodyParams(apiActionSteps, globalParams);
        // 设置Body参数
        if (null != bodyParams)
            request.body(bodyParams);
        return this;
    }

    /**
     * 发送请求
     * @return
     */
    public RestassuredClient send() throws Exception{
        try{
            if (method.equals("GET"))
                response = request
                        .when().log().all()
                        .get(requestUrl)
                        .then().log().all()
                        .extract().response();
            else
                response = request
                        .when().log().all()
                        .post(requestUrl)
                        .then().log().all()
                        .extract().response();
            return this;
        } catch (Exception e) {
            String tips = "发送请求异常 @" + e.getMessage();
            log.error(tips, e);
            throw new ServiceException(tips);
        }
    }

    /**
     * 断言响应结果
     * @param globalParams
     * @return
     */
    public ResultDto assertResponse(Map<String, Object> globalParams) {
        if(Objects.isNull(afterRequestSteps) || afterRequestSteps.size() == 0)
            return ResultDto.success("请求成功！");

        ResultDto resultDto = new ResultDto();
        resultDto.setAsSuccess();
        resultDto.setMessage("请求成功");
        // 解析请求后步骤
        Iterator<TestApiAction> iterator= afterRequestSteps.iterator();
        while(iterator.hasNext()) {
            TestApiAction currentStep = iterator.next();
            String keyWord = currentStep.getStepKeyword();
            if (keyWord.equals("getResponse")) {
                this.doGetResponse(currentStep, globalParams);
                continue;
            }
            TestActionStepResult assertionResult = null;
            if (keyWord.equals("assertEqual"))
                assertionResult = this.doAssertEqual(currentStep, globalParams);
            else if (keyWord.equals("assertNull"))
                assertionResult = this.doAssertNull(currentStep, globalParams);
            else if (keyWord.equals("assertContains"))
                assertionResult = this.doAssertContains(currentStep, globalParams);
            else if (keyWord.equals("assertIsA"))
                assertionResult = this.doAssertIsA(currentStep, globalParams);
            else { /* TODO */ };
            if (assertionResult.getResult().equals(ASSERTION_FAILED)) {
                resultDto.setAsFailure();
                resultDto.setMessage("验证失败，请查看日志！");
                log.error(assertionResult.getErrMsg());
            }
        }
        return resultDto;
    }


    /* unfinished */
    private void setRequestHeader(List<TestApiHeader> headers)
            throws InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Method[] methods = RequestSpecification.class.getMethods();
        Iterator<TestApiHeader> iterator = headers.iterator();
        while(iterator.hasNext()) {
            TestApiHeader header = iterator.next();
            String headerKey = header.getHeaderKey().replace("-", "");
            //headerKey = headerKey.substring(0, 1).toLowerCase() + headerKey.substring(1);
            for(Method method : methods) {
                if (headerKey.equalsIgnoreCase(method.getName())) {
                    Class[] parameterTypes = method.getParameterTypes();
                    for (Class parameterType : parameterTypes) {
                        if (parameterType == Class.forName("java.lang.String")) {
                            method.invoke(request, header.getHeaderValue());
                            break;
                        }
                        else {}
                    }
                    break;
                }
            }
        }
    }
    private void setRequestHeaderSimple(List<TestApiHeader> headers){
        Iterator<TestApiHeader> iterator = headers.iterator();
        while(iterator.hasNext()) {
            TestApiHeader header = iterator.next();
            if (header.getHeaderKey().equals("Accept")) {
                ContentType contentType = getContentType(header.getHeaderValue());
                request.accept(contentType);
            }
            if (header.getHeaderKey().equals("Content-Type")) {
                ContentType contentType = getContentType(header.getHeaderValue());
                request.contentType(contentType);
            }
        }
    }
    private ContentType getContentType(String name) {
        ContentType[] enumConstants = ContentType.class.getEnumConstants();
        for (ContentType enumConstant : enumConstants) {
            if (translateContentTypeName2EnumConstantName(name).equals(enumConstant.name())) {
                return enumConstant;
            }
        }
        return ContentType.ANY;
    }
    private String translateContentTypeName2EnumConstantName(String name) {
        if (name.contains("json")) return "JSON";
        if (name.contains("xml")) return "XML";
        if (name.contains("html")) return "HTML";
        if (name.contains("text")) return "TEXT";
        if (name.contains("urlencoded")) return "URLENC";
        if (name.contains("stream")) return "BINARY";
        return "ANY";
    }

    private Map<String, Object> dealBodyParams(List<TestApiAction> apiActionSteps, Map<String, Object> globalParams){
        Map<String, Object> bodyParams = new HashMap<String, Object>();
        Iterator<TestApiAction> iterator = apiActionSteps.iterator();
        while(iterator.hasNext()) {
            TestApiAction currentStep = iterator.next();
            String keyWord = currentStep.getStepKeyword();
            if (keyWord.equals("setParam")) {
                this.setUrlParam(currentStep, globalParams);
            } else if (keyWord.equals("setBodyParam")) {
                this.setBodyParam(bodyParams, currentStep, globalParams);
            } else if (keyWord.equals("getResponse")){
                afterRequestSteps.add(currentStep); // 暂存步骤，请求后解析
            } else if (keyWord.startsWith("assert")){
                afterRequestSteps.add(currentStep); // 暂存步骤，请求后解析
            } else continue;
        }
        return bodyParams;
    }
    private void setUrlParam(TestApiAction currentStep, Map<String, Object> globalParams) {
        String stepValue = String.valueOf(currentStep.getStepValue());
        if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
            request.queryParam(currentStep.getStepKey(), globalParams.get(stepValue.substring(1, stepValue.lastIndexOf("%"))));
        } else
            request.queryParam(currentStep.getStepKey(), stepValue);
    }
    private void setBodyParam(Map<String, Object> bodyParams, TestApiAction currentStep, Map<String, Object> globalParams) {
        String stepValue = String.valueOf(currentStep.getStepValue());
        if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
            bodyParams.put(currentStep.getStepKey(), globalParams.get(stepValue.substring(1, stepValue.lastIndexOf("%"))));
        } else if (stepValue.startsWith("int[") && stepValue.endsWith("]")) {
            String tempStr = stepValue.substring(4, stepValue.lastIndexOf("]"));
            String[] strings = tempStr.split(",");
            int[] ints = new int[strings.length];
            for (int i=0; i<strings.length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
            bodyParams.put(currentStep.getStepKey(), ints);
        } else if (stepValue.startsWith("str[") && stepValue.endsWith("]")) {
            String tempStr = stepValue.substring(4, stepValue.lastIndexOf("]"));
            String[] strings = tempStr.split(",");
            bodyParams.put(currentStep.getStepKey(), strings);
        } else
            bodyParams.put(currentStep.getStepKey(), stepValue);
    }

    private void doGetResponse(TestApiAction currentStep, Map<String, Object> globalParams) {
        String stepValue = String.valueOf(currentStep.getStepValue());
        if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
            String targetVariable = stepValue.substring(1, stepValue.lastIndexOf("%"));
            String targetValue = response.then().extract().path(currentStep.getStepKey());
            if(Objects.nonNull(targetValue))
                globalParams.put(targetVariable, targetValue); // todo
        } else return;
    }

    private TestActionStepResult doAssertEqual(TestApiAction currentStep, Map<String, Object> globalParams){
        TestActionStepResult testActionStepResult = new TestActionStepResult();
        testActionStepResult.setAssertionX("assertionEqual");

        String stepKey = String.valueOf(currentStep.getStepKey()); // to get actual
        String actual = response.then().extract().path(stepKey).toString();
        testActionStepResult.setActual(actual);

        String stepValue = String.valueOf(currentStep.getStepValue()); // excepted or to get excepted
        try {
            if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
                String excepted = (String) globalParams.get(stepValue.substring(1, stepValue.lastIndexOf("%")));
                testActionStepResult.setExpected(excepted);
                assertThat(actual, equalTo(excepted));
            } else {
                testActionStepResult.setExpected(stepValue);
                assertThat(actual, equalTo(stepValue));
            }
        } catch (AssertionError e) {
            testActionStepResult.setResult(ASSERTION_FAILED);
            testActionStepResult.setErrMsg(e.getMessage());
            return testActionStepResult;
        }
        testActionStepResult.setResult(ASSERTION_PASSED);
        return testActionStepResult;
    }

    private TestActionStepResult doAssertNull(TestApiAction currentStep, Map<String, Object> globalParams){
        TestActionStepResult testActionStepResult = new TestActionStepResult();
        testActionStepResult.setAssertionX("assertionNull");

        String stepKey = String.valueOf(currentStep.getStepKey()); // to get actual
        Object actual = response.then().extract().path(stepKey);

        String stepValue = String.valueOf(currentStep.getStepValue());
        try {
            if (stepValue.equalsIgnoreCase("true") || stepValue.equalsIgnoreCase("t")) {
                testActionStepResult.setExpected("Object by key {" + stepKey + "} should be null");
                assertThat(actual, nullValue());
            }
            else if (stepValue.equalsIgnoreCase("false") || stepValue.equalsIgnoreCase("f")) {
                testActionStepResult.setExpected("Object by key {" + stepKey + "} should not be null");
                assertThat(actual, notNullValue());
            }
            else {
                testActionStepResult.setErrMsg("SYSTEM_ERROR");
                return testActionStepResult;
            }
        } catch (AssertionError e) {
            testActionStepResult.setResult(ASSERTION_FAILED);
            testActionStepResult.setErrMsg(e.getMessage());
            return testActionStepResult;
        }
        testActionStepResult.setResult(ASSERTION_PASSED);
        return testActionStepResult;
    }

    private TestActionStepResult doAssertContains(TestApiAction currentStep, Map<String, Object> globalParams){
        TestActionStepResult testActionStepResult = new TestActionStepResult();
        testActionStepResult.setAssertionX("assertionContains");

        String stepKey = String.valueOf(currentStep.getStepKey()); // to get actual
        String actual = response.then().extract().path(stepKey).toString();
        testActionStepResult.setActual(actual);

        String stepValue = String.valueOf(currentStep.getStepValue()); // excepted or to get excepted
        try {
            if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
                String excepted = (String) globalParams.get(stepValue.substring(1, stepValue.lastIndexOf("%")));
                testActionStepResult.setExpected(excepted);
                assertThat(actual, containsString(excepted));
            } else {
                testActionStepResult.setExpected(stepValue);
                assertThat(actual, containsString(stepValue));
            }
        } catch (AssertionError e) {
            testActionStepResult.setResult(ASSERTION_FAILED);
            testActionStepResult.setErrMsg(e.getMessage());
            return testActionStepResult;
        }
        testActionStepResult.setResult(ASSERTION_PASSED);
        return testActionStepResult;
    }

    private TestActionStepResult doAssertIsA(TestApiAction currentStep, Map<String, Object> globalParams){
        TestActionStepResult testActionStepResult = new TestActionStepResult();
        testActionStepResult.setAssertionX("assertionIsA");

        String stepKey = String.valueOf(currentStep.getStepKey()); // to get actual
        Object actual = response.then().extract().path(stepKey);
        testActionStepResult.setActual(actual.toString());

        String stepValue = String.valueOf(currentStep.getStepValue()); // excepted or to get excepted
        try {
            if (stepValue.startsWith("%") && stepValue.endsWith("%")) {
                String excepted = (String) globalParams.get(stepValue.substring(1, stepValue.lastIndexOf("%")));
                Class clazz = Class.forName(excepted);
                testActionStepResult.setExpected(excepted);
                assertThat(actual, isA(clazz));
            } else {
                Class clazz = Class.forName(stepValue);
                testActionStepResult.setExpected(stepValue);
                assertThat(actual, isA(clazz));
            }
        } catch (AssertionError e) {
            testActionStepResult.setResult(ASSERTION_FAILED);
            testActionStepResult.setErrMsg(e.getMessage());
            return testActionStepResult;
        } catch (ClassNotFoundException e) {
            testActionStepResult.setResult("Test Failed");
            testActionStepResult.setErrMsg(e.getMessage());
            return testActionStepResult;
        }
        testActionStepResult.setResult(ASSERTION_PASSED);
        return testActionStepResult;
    }

}
