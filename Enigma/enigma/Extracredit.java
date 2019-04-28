package enigma;
/** An alphabet of encodable characters. Provides a mapping from characters
 *  to and from indices into the alphabet, that can take anything as an input
 *  @author Ziyue Meng
 */
public class Extracredit extends Alphabet {
    /** to store the input (alphebet in use). */
    private String _input;
    /** @param input Constructor. */
    Extracredit(String input) {
        _input = input;
    }
    /** Returns the size of the alphabet. */
    int size() {
        return _input.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        return _input.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _input.charAt(index);
    }


    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return  _input.indexOf(ch);
    }
}
