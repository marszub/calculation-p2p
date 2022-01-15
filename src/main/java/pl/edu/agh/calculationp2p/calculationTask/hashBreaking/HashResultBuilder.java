package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import pl.edu.agh.calculationp2p.calculationTask.interfaces.ResultBuilder;
import pl.edu.agh.calculationp2p.calculationTask.interfaces.TaskData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class HashResultBuilder implements ResultBuilder {
    String hash;
    List<String> result;
    HashStringCreator hashStringCreator;

    public HashResultBuilder(String hash, HashStringCreator hashStringCreator)
    {
        this.hash = hash;
        result = new LinkedList<>();
        this.hashStringCreator = hashStringCreator;
    }

    @Override
    public void reset()
    {
        result = new LinkedList<>();
    }

    @Override
    public void performComputation(TaskData data) {
        HashBreakerDataPackage taskFragmentData = (HashBreakerDataPackage) data;
        performComputationUsingDataOfProperType(taskFragmentData);
    }

    @Override
    public List<String> getResult() {
        return result;
    }

    private void performComputationUsingDataOfProperType(HashBreakerDataPackage data)
    {
        String actual = data.startingString();
        long atomicTaskSize = data.atomicTaskSize();
        for(int i = 0; i < atomicTaskSize; i++)
        {
            if(hash.equals(getMD5(actual)))
            {
                result.add(actual);
            }
            actual = hashStringCreator.getNext(actual);
        }
    }

    public String getMD5(String string)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        md5.update(string.getBytes());
        byte[] digest = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
