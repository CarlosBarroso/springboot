package es.test.springboot.controllers;

import es.test.springboot.annotations.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    @Value("${app.version}")
    private String appVersion;

    @Log
    @GetMapping
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Map getStatus(){
            Map map = new HashMap<String, String>();
            map.put("app-version", appVersion);
            return map;
    }

}
