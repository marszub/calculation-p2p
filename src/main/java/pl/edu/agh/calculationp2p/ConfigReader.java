package pl.edu.agh.calculationp2p;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return false;
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
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
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public int getHeartBeatPeriod() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return -1;
            return Integer.parseInt(jsonMap.get("heart_beat_period").toString());
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public int getHeartBeatLifetime() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return -1;
            return Integer.parseInt(jsonMap.get("heart_beat_lifetime").toString());
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return -1;
    }

    @Override
    public String getTaskConfigPath() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return null;
            return jsonMap.get("task_config_path").toString();
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public int numOfCalculationThreads() {
        LinkedHashMap<String, Object> jsonMap;
        try {
            jsonMap = mapper.readValue(content, typeRef);
            if (jsonMap == null)
                return 1;
            return Integer.parseInt(jsonMap.get("thread_num").toString());
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger("");
            logger.error(e.getMessage());
        }
        return 1;
    }
}


