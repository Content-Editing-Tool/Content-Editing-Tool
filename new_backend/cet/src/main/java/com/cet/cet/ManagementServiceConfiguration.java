package com.cet.cet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ManagementServiceConfiguration {

  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.cet.managementservice");
    return marshaller;
  }

  @Bean
  public ManagementServiceClient managementServiceClient(Jaxb2Marshaller marshaller) {
    ManagementServiceClient client = new ManagementServiceClient();
    client.setDefaultUri("https://meet-encore.blueriq.com/Studio/Server/Services/ManagementService");
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}
