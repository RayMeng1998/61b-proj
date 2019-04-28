package enigma;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ziyue Meng
 */
class Permutation {
    /** Instance attribute that stores the cycles. */
    private String[] _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = ssplit(cycles);

    }
    /** @param cycles
     * Return a String[] that stores cycles without unnecessary information. */
    public String[] ssplit(String cycles) {
        if (!cycles.contains(" ")) {
            cycles = cycles.replaceAll("\\)", "");
            cycles = cycles.replaceAll("\\(", "");
            return new String[] {cycles};
        }
        cycles = cycles.replaceAll(" ", "");
        String[] result = cycles.split("\\)\\(");
        result[0] = result[0].replaceAll("\\(", "");
        result[result.length - 1] = result[result.length - 1]
                .replaceAll("\\)", "");
        return result;
    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] tmp = ssplit(cycle);
        String[] result = new String[tmp.length + _cycles.length];
        for (int i = 0; i < _cycles.length; i++) {
            result[i] = _cycles[i];
        }
        for (int j = _cycles.length; j < result.length; j++) {
            result[j] = tmp[j - _cycles.length];
        }
        _cycles = result;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles == null) {
            return p;
        }
        char changed = p;
        for (String i : _cycles) {
            if (i.contains(String.valueOf(p))) {
                int index = i.indexOf(String.valueOf(p));
                if (index == i.length() - 1) {
                    index = 0;
                } else {
                    index += 1;
                }
                changed = i.charAt(index);
                break;
            }
        }
        return changed;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_cycles == null) {
            return c;
        }
        char changed = c;
        for (String i : _cycles) {
            if (i.contains(String.valueOf(c))) {
                int index = i.indexOf(String.valueOf(c));
                if (index == 0) {
                    index = i.length() - 1;
                } else {
                    index -= 1;
                }
                changed = i.charAt(index);
                break;
            }
        }
        return changed;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int sum = 0;
        for (String i : _cycles) {
            if (i.length() == 1) {
                return false;
            }
            sum += i.length();
        }
        return sum == _alphabet.size();

    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
}
