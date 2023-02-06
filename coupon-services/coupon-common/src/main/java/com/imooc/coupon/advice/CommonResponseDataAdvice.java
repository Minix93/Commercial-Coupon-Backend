package com.imooc.coupon.advice;

import com.imooc.coupon.annotation.IgnoreResponseAdvice;
import com.imooc.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <h1>Common Response</h1>
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * <h2>Determin if the response should be processed</h2>
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // if the class that current method belongs to have annotation of @IgnoreResponseAdvice(), no need to process
        if (returnType.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }

        // if current method have annotation of @IgnoreResponseAdvice(), no need to process
        if (returnType.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }

        //process the response, execute beforeBodyWrite method
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // define the final object of returning
        CommonResponse<Object> response1 = new CommonResponse<>(
                0,""
        );

        // if body is null, not need to set data for response1
        if (body == null){
            return response1;
        } else if (body instanceof CommonResponse){ // body is already instance of CommonResponse, no need to process
            response1 = (CommonResponse<Object>) body;

        }else{
            response1.setData(body);
        }
        return response1;
    }
}
