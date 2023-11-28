package com.cet.cet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List; 
import java.util.ArrayList; 

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.json.JSONObject;
import org.json.JSONArray;
import org.javatuples.Pair;

import com.cet.managementservice.*;

@RestController
public class CetController {
    @Autowired ManagementServiceClient client;
    private String repository = "Kinderbijslag";
    private String branch = "Trunk";
    private String project = "Kinderbijslag";
    private ModuleKey module;
    public List<Pair<AttributeKey, Attribute>> attributes;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void index() {
        initialize();
    }

    @GetMapping("/attributes")
    @ResponseStatus(HttpStatus.OK)
    public String getAttributes() {
        JSONObject data = new JSONObject();
        for (Pair<AttributeKey, Attribute> p: attributes) {
            Attribute attribute = p.getValue1();
            JSONObject properties = new JSONObject();
            JSONObject questionText = new JSONObject();
            for (MultiLingualText mlt: attribute.getQuestionText().getText()) {
                questionText.put("<h2>" + mlt.getLanguage() + "</h2>", "<p>" + mlt.getValue() + "</p>");
            }
            properties.put("<h2>questionText</h2>", questionText);
            data.put("<h1>" + attribute.getName() + "</h1>", properties);
        }
        String response = hackFixBackslashes(data.toString());
        return response;
    }

    private String hackFixBackslashes(String s) {
        String result = s;
        result = StringUtils.replace(result, "<\\/h1>", "</h1>");
        result = StringUtils.replace(result, "<\\/h2>", "</h2>");
        result = StringUtils.replace(result, "<\\/p>", "</p>");
        return result;
    }

    @PostMapping("/requestChanges")
    @ResponseStatus(HttpStatus.OK)
    public void requestChanges(@RequestBody String raw) {
        JSONObject data = new JSONObject(raw);
        List<Pair<AttributeKey, Attribute>> modified = getModified(data);
        for (Pair<AttributeKey, Attribute> p: modified) {
            Attribute attribute = p.getValue1();
        }
        requestChanges(modified);
    }

    private List<Pair<AttributeKey, Attribute>> getModified(JSONObject data) {
        ArrayList<Pair<AttributeKey, Attribute>> result = new ArrayList<Pair<AttributeKey, Attribute>>();

        for (Pair<AttributeKey, Attribute> p: attributes) {
            Attribute attribute = p.getValue1();
            boolean modified = false;
            for (MultiLingualText mlt: attribute.getQuestionText().getText()) {
                String originalText = mlt.getValue();
                String htmlRequestText = data.getJSONObject("<h1>" + attribute.getName() + "</h1>").getJSONObject("<h2>questionText</h2>").getString("<h2>" + mlt.getLanguage() + "</h2>");
                String requestText = StringUtils.replace(htmlRequestText, "<p>", "");
                requestText = StringUtils.replace(requestText, "</p>", "");
                if (!originalText.equals(requestText)) {
                    modified = true;
                    mlt.setValue(requestText);
                }
            }
            if (modified) { 
                result.add(new Pair<AttributeKey, Attribute>(p.getValue0(), attribute));
            }
        }
        return result;
    }

    private void initialize() {
        initModule();
        initAttributes();
    }
    
    private void initModule() {
        List<ModuleKey> modules = client.getModules(repository, branch, project);
        for (ModuleKey m: modules) {
            if (m.getModuleType().value() == "Interaction") {
                module = m;
                return;
            }
        }
    }

    private void initAttributes() {
        attributes = new ArrayList<Pair<AttributeKey, Attribute>>();
        List<ModuleElementKey> keys = client.getAllModuleElements(repository, branch, project, module);
        for (ModuleElementKey key: keys) {
            ModuleElement element = client.getModuleElement(repository, branch, project, module, key);
            if (element instanceof Attribute) {
                attributes.add(new Pair<AttributeKey, Attribute>((AttributeKey)key, (Attribute)element));
           }
       }
    }

    private void createRequestChangesBranch() {
        Branch cetBranch = new Branch();
        cetBranch.setName("CET_request");
        client.createFeatureBranch(repository, branch, cetBranch);
    }

    private void requestChanges(List<Pair<AttributeKey, Attribute>> attributes) {
        //createRequestChangesBranch();

        ArrayOfOperationEntry arrayOfOperationEntry = new ArrayOfOperationEntry();
        List<OperationEntry> operations = arrayOfOperationEntry.getOperationEntry(); 

        for (Pair<AttributeKey, Attribute> p: attributes) {
            AttributeKey key = p.getValue0();
            Attribute attribute = p.getValue1();

            ModuleElementOperation operation = new ModuleElementOperation();

            operation.setProject(project);
            operation.setModule(module);
            operation.setOriginalKey(key);
            operation.setModuleElement(attribute);
            operation.setType(ChangeType.fromValue("Update"));

            OperationEntry operationEntry = new OperationEntry();
            operationEntry.setModuleElementOperation(operation);
            operations.add(operationEntry);
        }

        OperationSet operationSet = new OperationSet();
        operationSet.setOperations(arrayOfOperationEntry);

        client.applyOperations(repository, "CET_request", operationSet);
    }
}
