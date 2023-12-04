package com.cet.cet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List; 
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.HashSet;
import java.util.Map; 

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.json.JSONObject;
import org.json.JSONArray;
import org.javatuples.Pair;

import com.cet.managementservice.*;

@RestController
public class CetController {
    @Autowired ManagementServiceClient client;

    @GetMapping("/attributes")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        ModuleKey module = new ModuleKey();
        module.setName("Kinderbijslag");
        module.setModuleType(ModuleType.fromValue("Interaction"));

        List<ModuleElementKey> entityKeys = client.getModuleElements("Kinderbijslag", "Trunk", "Kinderbijslag", module, ModuleElementType.fromValue("Entity"));
        List<Entity> entities = new ArrayList<Entity>();
        HashMap<String, List<String>> derived = new HashMap<String, List<String>>();

        for (ModuleElementKey entityKey: entityKeys) {
            Entity entity = (Entity) client.getModuleElement("Kinderbijslag", "Trunk", "Kinderbijslag", module, entityKey);
            entities.add(entity);
            derived.put(entity.getName(), new ArrayList<String>());
        }

        for (Entity entity: entities) {
            if (entity.getBaseEntity() != null) {
                derived.get(entity.getBaseEntity()).add(entity.getName());
            }
        }

        JSONObject json = new JSONObject();

        List<ModuleElementKey> attributeKeys = client.getModuleElements("Kinderbijslag", "Trunk", "Kinderbijslag", module, ModuleElementType.fromValue("Attribute"));
        for (ModuleElementKey attributeKey: attributeKeys) {
            Attribute attribute = (Attribute) client.getModuleElement("Kinderbijslag", "Trunk", "Kinderbijslag", module, attributeKey);
            JSONObject info = new JSONObject();
            info.put("questionText", getQuestionText(attribute));

            HashSet<String> tmp = new HashSet<String>();
            dfs(attribute.getEntity(), derived, tmp);
            JSONArray attrEntities = new JSONArray();

            for (String entity: tmp) {
                attrEntities.put(entity);
            }

            info.put("entities", attrEntities);
            json.put(attribute.getEntity() + "." + attribute.getName(), info); 
        }
        return json.toString();
    }

    private JSONObject getQuestionText(Attribute attribute) {
        JSONObject result = new JSONObject();
        result.put("nl", "");
        result.put("en", "");
        for (MultiLingualText mlt: attribute.getQuestionText().getText()) {
            if (mlt.getLanguage().equals("Nederlands")) {
                result.put("nl", mlt.getValue());
            } else if (mlt.getLanguage().equals("English")) {
                result.put("en", mlt.getValue());
            } 
        }
        return result;
    }

    private void dfs(String u, Map<String, List<String>> g, HashSet<String> s) {
        s.add(u);
        for (String v: g.get(u)) {
            dfs(v, g, s);
        }
    }

    @PostMapping("/requestChanges")
    @ResponseStatus(HttpStatus.OK)
    public void requestChanges(@RequestBody String raw) {
        ModuleKey module = new ModuleKey();
        module.setName("Kinderbijslag");
        module.setModuleType(ModuleType.fromValue("Interaction"));

        JSONObject json = new JSONObject(raw);
        List<ModuleElementKey> keys = client.getModuleElements("Kinderbijslag", "Trunk", "Kinderbijslag", module, ModuleElementType.fromValue("Attribute"));

        ArrayOfOperationEntry arrayOfOperationEntry = new ArrayOfOperationEntry();
        List<OperationEntry> operations = arrayOfOperationEntry.getOperationEntry(); 

        for (ModuleElementKey elementKey: keys) {
            AttributeKey attributeKey = (AttributeKey)elementKey;
            String name = attributeKey.getEntity() + "." + attributeKey.getName(); 

            if (json.has(name)) {
                Attribute attribute = (Attribute)client.getModuleElement("Kinderbijslag", "Trunk", "Kinderbijslag", module, attributeKey);
                setQuestionText(attribute, json.getJSONObject(name).getJSONObject("questionText"));

                ModuleElementOperation operation = new ModuleElementOperation();

                operation.setProject("Kinderbijslag");
                operation.setModule(module);
                operation.setOriginalKey(attributeKey);
                operation.setModuleElement(attribute);
                operation.setType(ChangeType.fromValue("Update"));

                OperationEntry operationEntry = new OperationEntry();
                operationEntry.setModuleElementOperation(operation);
                operations.add(operationEntry);
            }
        }

        OperationSet operationSet = new OperationSet();
        operationSet.setOperations(arrayOfOperationEntry);

        client.applyOperations("Kinderbijslag", "CET_request", operationSet);
    }

    private void setQuestionText(Attribute attribute, JSONObject json) {
        for (MultiLingualText mlt: attribute.getQuestionText().getText()) {
            if (mlt.getLanguage().equals("Nederlands")) {
                mlt.setValue(json.getString("nl")); 
            } else if (mlt.getLanguage().equals("English")) {
                mlt.setValue(json.getString("en")); 
            } 
        }
    }

    //private void createRequestChangesBranch() {
        //Branch cetBranch = new Branch();
        //cetBranch.setName("CET_request");
        //client.createFeatureBranch(repository, branch, cetBranch);
    //}

    //@GetMapping("/test")
    //@ResponseStatus(HttpStatus.OK)
    //public void getPageContent() {
        //ModuleKey moduleKey = new ModuleKey();
        //moduleKey.setName("Kinderbijslag");
        //moduleKey.setModuleType(ModuleType.fromValue("Interaction"));

        //ModuleElementKey elementKey = new ModuleElementKey();
        //elementKey.setName("OuderGegevens");
        //elementKey.setModuleElementType(ModuleElementType.fromValue("Page"));
        //Page page = (Page)client.getModuleElement("Kinderbijslag", "Trunk", "Kinderbijslag", moduleKey, elementKey);

        //System.out.println("OK");
        //for (Containment containment: page.getContainments().getContainment()) {
            //retreive(containment);
        //}
        //System.out.println("DONE");
    //}

    //private void retreive(Containment containment) {
        
        //if (containment instanceof AttributeReference) {
            //AttributeReference ref = (AttributeReference) containment;
            //System.out.println(ref.getEntity() + "." + ref.getAttribute());
            //return;
        //}
        
        //if (containment instanceof ContainerContainment) {
            //Container container = ((ContainerContainment)containment).getContainer();

            //ParameterContent pc = container.getParameterContent();
            //if (pc instanceof DefaultContent) {
                //DefaultContent content = (DefaultContent) pc;
                //for (Containment c: content.getContainments().getContainment()) {
                    //retreive(c);
                //}
            //}
        //} else if (containment instanceof ContainerReference) {
            //ContainerReference cr = (ContainerReference)containment;
            //ModuleKey moduleKey = new ModuleKey();
            //moduleKey.setName("Kinderbijslag");
            //moduleKey.setModuleType(ModuleType.fromValue("Interaction"));

            //ModuleElementKey elementKey = new ModuleElementKey();
            //elementKey.setName(cr.getContainer());
            //elementKey.setModuleElementType(ModuleElementType.fromValue("Container"));
            //Container container = (Container)client.getModuleElement("Kinderbijslag", "Trunk", "Kinderbijslag", moduleKey, elementKey); 

            //ParameterContent pc = container.getParameterContent();
            //if (pc instanceof DefaultContent) {
                //DefaultContent content = (DefaultContent) pc;
                //for (Containment c: content.getContainments().getContainment()) {
                    //retreive(c);
                //}
            //}
        //} 

    //}
}
