package es.test.springboot.controllers;

import es.test.springboot.annotations.Log;
import es.test.springboot.hateoas.HomeModelAssembler;
import es.test.springboot.hateoas.SpeakerModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    private HomeModelAssembler homeModelAssembler;


    @Value("${app.version}")
    private String appVersion;

    @Log
    @GetMapping
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public EntityModel<Map> getStatus(){
            Map map = new HashMap<String, String>();
            map.put("app-version", appVersion);
            return homeModelAssembler.toModel( map);
    }

}
