package core.util.comparator;

/**
 * millemax
 * Date: 3/24/12
 * Time: 2:39 PM
 */
public class CharSequenceCmp
{
    public static boolean equals(CharSequence a, CharSequence b, boolean caseSensitive)
    {
        int aLen = a.length();
        int bLen = b.length();
        if (aLen != bLen)
        {
            return false;
        }

        char aChar, bChar;
        for (int i = 0; i < aLen; i++)
        {
            aChar = (caseSensitive) ? Character.toLowerCase(a.charAt(i)) : a.charAt(i);
            bChar = (caseSensitive) ? Character.toLowerCase(b.charAt(i)) : b.charAt(i);
            if (aChar != bChar)
            {
                return false;
            }
        }
        return true;
    }
    
}
