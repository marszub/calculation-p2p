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
            int port = Integer.parseInt(jsonMap.get("server_port").toString());

            return new InetSocketAddress(serverIpAddress, port);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InetSocketAddress getMyAddress() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return null;
            return new InetSocketAddress(jsonMap.get("my_ip_global").toString(), Integer.parseInt(jsonMap.get("my_port_global").toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InetSocketAddress getMySocketAddress() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return null;
            return new InetSocketAddress(jsonMap.get("my_ip_local").toString(), Integer.parseInt(jsonMap.get("my_port_local").toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean getPublicFlag() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return false;
            if (jsonMap.get("public") == null)
                return false;

            return (boolean) jsonMap.get("public");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getMaxConnectingTime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 5000;
            return (int) Float.parseFloat(jsonMap.getOrDefault("max_connection_time", 5).toString()) * 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 5000;
    }

    @Override
    public int getGetProgressRetryTime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 60000;
            return (int) Float.parseFloat(jsonMap.getOrDefault("progress_retry_time", 60).toString()) * 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 60000;
    }

    @Override
    public int getHeartBeatPeriod() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 2000;
            return (int) Float.parseFloat(jsonMap.getOrDefault("heart_beat_period", 2).toString()) * 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 2000;
    }

    @Override
    public int getHeartBeatLifetime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 20000;
            return (int) Float.parseFloat(jsonMap.getOrDefault("heart_beat_lifetime", 20).toString()) * 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 20000;
    }

    @Override
    public String getTaskConfigPath() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return "config/taskConfig.json";
            return jsonMap.getOrDefault("task_config_path", "config/taskConfig.json").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "config/taskConfig.json";
    }

    @Override
    public int numOfCalculationThreads() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 1;
            return Integer.parseInt(jsonMap.getOrDefault("thread_num", 1).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


