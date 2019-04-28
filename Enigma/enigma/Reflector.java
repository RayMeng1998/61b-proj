package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Ziyue Meng
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _settings = 0;
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }
    @Override
    void set(char cposn) {
        if (_permutation.alphabet().toInt(cposn) != 0) {
            throw error("reflector has only one position");
        }
    }
    @Override
    boolean reflecting() {
        return true;
    }
    /** To store the settings. */
    private int _settings;
    /** To store the name. */
    private final String _name;
    /** To store the perm. */
    private Permutation _permutation;

}
