package com.sp.web.book.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Aspect
@Component
@Log4j2
public class AopLoggingAspect {

    private final HttpServletRequest request;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper

    public AopLoggingAspect(HttpServletRequest request,ObjectMapper objectMapper) {
        this.request = request;
        this.objectMapper = objectMapper;
    }

    @Before("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public void logApiRequest(JoinPoint joinPoint) {

        //take request url
        String url = request.getRequestURI().toString();
        String method = request.getMethod();
        log.info("======= [API 요청] : {} / url : {} ", method, url);

        //take request parameter
        Map<String, String> params = getRequestParams();
        log.info("======= [요청 파라미터]: {}", params);

        // @RequestBody 확인 및 로깅
        Object requestBody = getRequestBody(joinPoint);
        if (requestBody != null) {
            log.info("요청 바디: {}", requestBody);
        }

    }

    /*
     *   요청 후 반환값 로깅
     */
    @AfterReturning(pointcut = "within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)",
            returning = "response")
    public void logApiResponse(JoinPoint joinPoint, Object response) {
        log.info("======= [API 응답]: {}", convertToJson(response));
    }

    /*
     *   HTTP 요청 파라미터를 가져오는 메서드
     * */
    private Map<String,String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paramName = parameterNames.nextElement();
            params.put(paramName , request.getParameter(paramName));
        }
        return params;
    }

    /*
         @RequestBody가 있는 경우, 요청 바디 데이터를 가져오는 메서드
         IntStream.range(0, method.getParameters().length)
            → for 루프를 스트림으로 변환하여 인덱스를 순회.
         filter(i -> method.getParameters()[i].isAnnotationPresent(RequestBody.class))
            → @RequestBody 애너테이션이 존재하는 파라미터만 필터링.
         mapToObj(i -> args[i])
            → 해당 인덱스의 args 값을 매핑.
         findFirst().orElse(null)
            → 첫 번째 일치하는 값 반환, 없으면 null.
     * */
    private Object getRequestBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = getMethod(joinPoint);
        if(method == null) {
            return null;
        }

        return IntStream.range(0, method.getParameters().length)
                .filter(i->method.getParameters()[i].isAnnotationPresent(RequestBody.class))
                .mapToObj(i -> args[i])
                .findFirst()
                .orElse(null);

    }

    private Method getMethod(JoinPoint joinPoint) {
        try {
            return joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(),
                            ((MethodSignature) joinPoint.getSignature()).getParameterTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }

    }

    /*
     *   객체를 JSON 문자열로 변환하는 메서드
     */
    private String convertToJson(Object object) {
        try{
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return "response to json error" + e.getMessage();
        }
    }


}
