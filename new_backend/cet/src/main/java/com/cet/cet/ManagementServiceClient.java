package com.cet.cet;

import java.lang.RuntimeException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.util.List;
import java.util.ArrayList;

import com.cet.managementservice.*;

public class ManagementServiceClient extends WebServiceGatewaySupport {
    private String endpoint = "https://meet-encore.blueriq.com/Studio/Server/Services/ManagementService"; 

    public List<ModuleKey> getModules(String repository, String branch, String project) {
        GetModules request = new GetModules();
        request.setRepository(repository);
        request.setBranch(branch);
        request.setProject(project);

        GetModulesResponse response = (GetModulesResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("GetModules"));
        return response.getGetModulesResult().getModuleKey();
    }

    public ModuleElement getModuleElement(String repository, String branch, String project, ModuleKey module, ModuleElementKey element) {
        GetModuleElement request = new GetModuleElement();
        request.setRepository(repository);
        request.setBranch(branch);
        request.setProject(project);
        request.setModule(module);
        request.setModuleElement(element);

        GetModuleElementResponse response = (GetModuleElementResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("GetModuleElement"));
        return response.getGetModuleElementResult();
    }

    //public List<ModuleElementKey> getAllModuleElements(String repository, String branch, String project, ModuleKey module) {
        //GetAllModuleElements request = new GetAllModuleElements();
        //request.setRepository(repository);
        //request.setBranch(branch);
        //request.setProject(project);
        //request.setModule(module);

        //GetAllModuleElementsResponse response = (GetAllModuleElementsResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("GetAllModuleElements"));
        //return response.getGetAllModuleElementsResult().getModuleElementKey();
    //}


    public List<ModuleElementKey> getModuleElements(String repository, String branch, String project, ModuleKey module, ModuleElementType type) {
        GetModuleElements request = new GetModuleElements();
        request.setRepository(repository);
        request.setBranch(branch);
        request.setProject(project);
        request.setModule(module);
        request.setElementType(type);

        GetModuleElementsResponse response = (GetModuleElementsResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("GetModuleElements"));
        return response.getGetModuleElementsResult().getModuleElementKey();
    }

    public void createFeatureBranch(String repository, String basedOnBranch, Branch branch) {
        CreateFeatureBranch request = new CreateFeatureBranch();
        request.setRepository(repository);
        request.setBasedOnBranch(basedOnBranch);
        request.setBranch(branch);

        getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("CreateFeatureBranch"));
    }

    public void applyOperations(String repository, String branch, OperationSet operationSet) {
        ApplyOperations request = new ApplyOperations();
        request.setRepository(repository);
        request.setBranch(branch);
        request.setOperations(operationSet);

        getWebServiceTemplate().marshalSendAndReceive(endpoint, request, new SoapActionCallback("ApplyOperations"));
    }
}
