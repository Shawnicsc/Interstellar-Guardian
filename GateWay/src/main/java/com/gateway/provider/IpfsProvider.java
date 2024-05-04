//package com.gateway.provider;
//
//import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * @author Shawn i
// * @version 1.0
// * @description: TODO
// * @date 2024/4/29 20:17
// */
//@Component
//public class IpfsProvider implements FallbackProvider {
//    /**
//     * @description: 返回处理错误的service
//     * @author Shawn i
//     * @date: 2024/3/13 15:57
//     */
//    @Override
//    public String getRoute() {
//        return "user-service";
//    }
//    /**
//     * @description: 返回的逻辑
//     * @author Shawn i
//     * @date: 2024/3/13 15:57
//     */
//    @Override
//    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
//        return new ClientHttpResponse() {
//            @Override
//            public HttpStatus getStatusCode() throws IOException {
//                return HttpStatus.OK;
//            }
//
//            @Override
//            public int getRawStatusCode() throws IOException {
//                return 200;
//            }
//
//            @Override
//            public String getStatusText() throws IOException {
//                return "{code:500,message:\"服务器异常！\"}";
//            }
//
//            @Override
//            public void close() {
//
//            }
//
//            @Override
//            public InputStream getBody() throws IOException {
//                return new ByteArrayInputStream(getStatusText().getBytes());
//            }
//
//            @Override
//            public HttpHeaders getHeaders() {
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                return headers;
//            }
//        };
//
//    }
//}
