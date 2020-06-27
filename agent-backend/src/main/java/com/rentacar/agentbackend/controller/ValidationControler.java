package com.rentacar.agentbackend.controller;

import com.github.rkpunjal.sqlsafe.SQLInjectionSafe;
import com.rentacar.agentbackend.dto.request.CreateAgentRequest;
import com.rentacar.agentbackend.dto.request.CreateSimpleUserRequest;
import com.rentacar.agentbackend.dto.request.LoginRequest;
import com.rentacar.agentbackend.dto.request.NewPassordRequest;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * JSON Schema validator and path-param validator
 */
public class ValidationControler {

    private final String SCHEMA_PATH_PREFIX = "/schemes/";

    /**
     * Java Wrapper class
     * @paramerar rest path parametar
     */
    public static class ParamWrapper{

        private @SQLInjectionSafe String parameter;

        public String getParameter() {
            return parameter;
        }

        public void setId(String parameter) {
            this.parameter = parameter;
        }
    }

    void validateAgentJSON(CreateAgentRequest jsonString) throws IOException, ValidationException, JSONException {
        InputStream inputStream = new ClassPathResource(SCHEMA_PATH_PREFIX + "createAgent.json").getInputStream();
        Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        schema.validate(new JSONObject(jsonString));
    }

    void validateSimpleUserJSON(CreateSimpleUserRequest jsonString) throws IOException, ValidationException, JSONException {
        InputStream inputStream = new ClassPathResource(SCHEMA_PATH_PREFIX + "createSimpleUser.json").getInputStream();
        Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        schema.validate(new JSONObject(jsonString));
    }

    void validateLoginRequestJSON(LoginRequest jsonString) throws IOException, ValidationException, JSONException {
        InputStream inputStream = new ClassPathResource(SCHEMA_PATH_PREFIX + "loginRequest.json").getInputStream();
        Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        schema.validate(new JSONObject(jsonString));
    }

    void validateNewPasswordRequestJSON(NewPassordRequest jsonString) throws IOException, ValidationException, JSONException {
        InputStream inputStream = new ClassPathResource(SCHEMA_PATH_PREFIX + "newPassordRequest.json").getInputStream();
        Schema schema = SchemaLoader.load(new JSONObject(new JSONTokener(inputStream)));
        schema.validate(new JSONObject(jsonString));
    }

    public String getSCHEMA_PATH_PREFIX() {
        return SCHEMA_PATH_PREFIX;
    }


}
