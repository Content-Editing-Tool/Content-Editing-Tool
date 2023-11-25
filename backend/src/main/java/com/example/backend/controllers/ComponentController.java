package com.example.backend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/component")
@RestController
public class ComponentController {

    @GetMapping("/components")
    public ComponentData[] getComponents() {
        ComponentData[] components = new ComponentData[1];
        components[0] = new ComponentData("Component 1");
        return components;
    }


}

class ComponentData{
    String name;
    ComponentData(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Setter if you need deserialization
    public void setName(String name) {
        this.name = name;
    }
}


