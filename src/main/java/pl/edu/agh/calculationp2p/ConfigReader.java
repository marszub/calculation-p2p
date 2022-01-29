package pl.edu.agh.calculationp2p;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigReader implements AppConfig{

    private String content;
    private final TypeReference<LinkedHashMap<String,Object>> typeRef;
    private final ObjectMapper mapper;

    public ConfigReader(String path) throws Exception{
        try {
            content = Files.readString(Path.of(path));
            if(content==null){
                throw new Exception();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        typeRef = new TypeReference<>() {};
    }

    @Override
    public InetSocketAddress getServerAddress() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);

            if (jsonMap == null)
                return null;

            String serverIpAddress = jsonMap.get("server_ip").toString();
            int port = Integer.parseInt(jsonMap.get("port").toString());

            return new InetSocketAddress(serverIpAddress, port);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getMyIpString() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return null;
            return jsonMap.get("my_ip").toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getMaxConnectingTime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return -1;
            return Integer.parseInt(jsonMap.get("max_connection_time").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getGetProgressRetryTime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return -1;
            return Integer.parseInt(jsonMap.get("progress_retry_time").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
