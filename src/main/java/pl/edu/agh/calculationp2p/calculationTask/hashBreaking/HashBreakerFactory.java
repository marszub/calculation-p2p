package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTask;
import pl.edu.agh.calculationp2p.calculationTask.CalculationTaskFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class HashBreakerFactory implements CalculationTaskFactory
{

    private String content;
    private final TypeReference<LinkedHashMap<String,Object>> typeRef;
    private final ObjectMapper mapper;

    //Default task
    String hash = "7DAF6D81AE80B8930DE2AAF1140B1BE2";
    long taskSize = 10_000_000L;
    long atomicTaskSize = 100_000L;
    int maxStringLength = 10;
    char[] characters = "0123456789".toCharArray();

    public HashBreakerFactory(String file)
    {
        try {
            content = Files.readString(Path.of(file));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        typeRef = new TypeReference<>() {};
        readConfig();
    }

    @Override
    public CalculationTask createTask()
    {
        HashBreakerInit initVariable = prepareInitVariables();
        return new HashBreaker(initVariable);
    }

    private HashBreakerInit prepareInitVariables()
    {
        return new HashBreakerInit(
                hash,
                taskSize,
                atomicTaskSize,
                maxStringLength,
                characters
        );
    }

    private void readConfig() {
        if(content == null)
            return;
        LinkedHashMap<String, Object> jsonMap = null;
        try {
            jsonMap = mapper.readValue(content, typeRef);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(jsonMap != null)
        {
            hash = jsonMap.getOrDefault("hash", hash).toString();
            taskSize = Long.parseLong(jsonMap.getOrDefault("taskSize", taskSize).toString());
            atomicTaskSize = Long.parseLong(jsonMap.getOrDefault("atomicTaskSize", atomicTaskSize).toString());
            maxStringLength = Integer.parseInt(jsonMap.getOrDefault("maxStringLength", maxStringLength).toString());
            characters = jsonMap.getOrDefault("characters", characters).toString().toCharArray();
        }
    }
}
