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
        components[0] = new ComponentData("<h1>Insurance Registration Form</h1>\n" +
                "\n" +
                "  <form action=\"/submit-registration\" method=\"post\">\n" +
                "    <label for=\"fullName\">Full Name:</label><br>\n" +
                "    <input type=\"text\" id=\"fullName\" name=\"fullName\" required><br>\n" +
                "\n" +
                "    <label for=\"email\">Email:</label><br>\n" +
                "    <input type=\"email\" id=\"email\" name=\"email\" required><br>\n" +
                "\n" +
                "    <label for=\"dob\">Date of Birth:</label><br>\n" +
                "    <input type=\"date\" id=\"dob\" name=\"dob\" required><br>\n" +
                "\n" +
                "    <label for=\"policyType\">Policy Type:</label><br>\n" +
                "    <select id=\"policyType\" name=\"policyType\" required>\n" +
                "      <option value=\"auto\">Auto</option>\n" +
                "      <option value=\"health\">Health</option>\n" +
                "      <option value=\"home\">Home</option>\n" +
                "      <option value=\"life\">Life</option>\n" +
                "    </select><br>\n" +
                "\n" +
                "    <label for=\"agreeTerms\">\n" +
                "      <input type=\"checkbox\" id=\"agreeTerms\" name=\"agreeTerms\" required>\n" +
                "      I agree to the Terms and Conditions\n" +
                "    </label><br>\n" +
                "\n" +
                "    <input type=\"submit\" value=\"Register\">\n" +
                "  </form>\n" +
                "\n" +
                "\n" +
                "  <table *ngIf=\"components && components.length > 0\">\n" +
                "    <tbody>\n" +
                "    <tr *ngFor=\"let ComponentData of components\">\n" +
                "      <td>{{ ComponentData.pageCode }}</td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n");
        return components;
    }


}

class ComponentData{
    String pageCode;
    ComponentData(String pageCode){
        this.pageCode = pageCode;
    }

    public String getPageCode() {
        return pageCode;
    }

    // Setter if you need deserialization
    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }
}


