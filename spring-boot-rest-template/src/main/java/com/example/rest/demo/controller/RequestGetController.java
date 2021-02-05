package com.example.rest.demo.controller;

import com.example.rest.demo.dto.UserDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jingLv
 * @date 2021/02/02
 */
@RestController
public class RequestGetController {

    private final RestTemplate template;

    public RequestGetController(RestTemplate template) {
        this.template = template;
    }

    @GetMapping("/getForObject")
    public UserDTO getForObject() {
        // 远程访问的Url
        String url = "http://60.205.228.49:8088/v1/info/user";
        // 请求入参
        Map<String, Object> params = new HashMap<>(16);
        // 请求，并返回接口实体数据
        return template.getForObject(url, UserDTO.class, params);
    }

    @GetMapping("/getForEntity")
    public Map<String, Object> getForEntity() {
        String url = "http://60.205.228.49:8088/v1/info/user";
        // 请求入参
        Map<String, Object> params = new HashMap<>(16);
        // ResponseEntity 包装返回结果
        ResponseEntity<HashMap> responseEntity = template.getForEntity(url, HashMap.class, params);
        // 返回状态码包装类
        HttpStatus statusCode = responseEntity.getStatusCode();
        // 返回状态码
        int statusCodeValue = responseEntity.getStatusCodeValue();
        // Http返回头信息
        HttpHeaders headers = responseEntity.getHeaders();
        // 返回请求结果
        return responseEntity.getBody();
    }
}
