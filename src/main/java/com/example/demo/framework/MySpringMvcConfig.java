package com.example.demo.framework;

import com.example.demo.framework.interceptor.HttpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootConfiguration
public class MySpringMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    public HttpInterceptor httpInterceptor;

    public void addInterceptors(InterceptorRegistry interceptorRegistry){
        interceptorRegistry.addInterceptor(httpInterceptor);
    }

}
