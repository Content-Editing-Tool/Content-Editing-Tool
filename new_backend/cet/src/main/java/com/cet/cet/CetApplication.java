package com.cet.cet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import javax.xml.ws.BindingProvider;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import com.cet.managementservice.*;

@SpringBootApplication
public class CetApplication {

	public static void main(String[] args) {
        Authenticator myAuth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication("ansar", "BszxQfkLCS2TDmJ".toCharArray());
            }
        };
        Authenticator.setDefault(myAuth);
        SpringApplication.run(CetApplication.class, args);
	}

    //@Bean
    //CommandLineRunner run(ManagementServiceClient client) {
        //return args-> {
            //GetProjects request = new GetProjects();
            //request.setRepository("Kinderbijslag");
            //request.setBranch("Trunk");
            //GetProjectsResponse response = client.getProjects(request);
            //for (String project: response.getGetProjectsResult().getString()) {
                //System.out.println(project);
            //}
        //};
    //}
}
