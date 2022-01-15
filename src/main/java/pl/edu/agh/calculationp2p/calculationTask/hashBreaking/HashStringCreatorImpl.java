package pl.edu.agh.calculationp2p.calculationTask.hashBreaking;

import java.util.HashMap;
import java.util.Map;

public class HashStringCreatorImpl implements HashStringCreator {
    char[] signs;
    Map<Character, Integer> signsMap;

    public HashStringCreatorImpl(char[] signs)
    {
        this.signs = signs;
        this.signsMap = createCharacterMap(signs);
    }

    @Override
    public String getNext(String string)
    {
        char[] stringAsChar = string.toCharArray();
        int stringSize = stringAsChar.length;
        int signsSize = signs.length;
        for(int i = stringSize-1; i>=0; i--)
        {
            char nowConsideredSign = stringAsChar[i];
            int value = signsMap.get(nowConsideredSign) + 1;
            if(value < signsSize)
            {
                stringAsChar[i] = signs[value];
                return new String(stringAsChar);
            }
            stringAsChar[i] = signs[0];
        }
        StringBuilder newString = new StringBuilder(String.valueOf(signs[0]));
        newString.append(String.valueOf(stringAsChar));
        return newString.toString();
    }

    @Override
    public String getString(long number)
    {
        StringBuilder result = new StringBuilder();
        int length = signs.length;
        result.append(signs[(int) (number%length)]);
        number = number/length;
        while(number > 0)
        {
            long index = number%length;
            if(index == 0)
            {
                result.append(signs[length - 1]);
                number = number/length;
                number = number - 1;
            }
            else
            {
                result.append(signs[(int) (index-1)]);
                number = number/length;
            }
        }
        return result.reverse().toString();
    }

    private Map<Character, Integer> createCharacterMap(char[] signs)
    {
        HashMap<Character, Integer> map = new HashMap<>();
        for(int i = 0; i < signs.length; i++)
        {
            map.put(signs[i], i);
        }
        return map;
    }
}
