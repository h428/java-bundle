package com.demo.boot.ssm.web.handler;

import com.demo.base.component.exception.BaseWebException;
import com.demo.base.component.pojo.bean.ResBean;
import com.demo.base.component.util.AjaxUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    // 统一处理继承于 BaseWebException 的全局异常（包含了状态码）
    @ExceptionHandler(BaseWebException.class)
    public void handleBaseWebException(HttpServletResponse response, BaseWebException ex) throws Exception {
        // 写入响应
        AjaxUtil.writeJsonResponse(response, ex.getStatus(),
                objectMapper.writeValueAsString(new ResBean(ex.getStatus(), ex.getMessage())
                ));

    }

}
