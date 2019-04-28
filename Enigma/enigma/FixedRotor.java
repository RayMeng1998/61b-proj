package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Ziyue Meng
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _settings = 0;
    }
    /** settings. */
    private int _settings;
    /** name. */
    private final String _name;
    /** perm. */
    private Permutation _permutation;
}
