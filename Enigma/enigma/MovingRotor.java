package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ziyue Meng
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
        _name = name;
        _settings = 0;
    }
    @Override
    boolean atNotch() {
        return _notches.indexOf(alphabet().toChar(setting())) > -1;
    }

    @Override
    void advance() {
        set(_permutation.wrap(super.setting() + 1));
    }

    @Override
    boolean rotates() {
        return true;
    }
    /** To store all notches. */
    private String _notches;
    /** To store settings. */
    private int _settings;
    /** To store the name. */
    private final String _name;
    /** To store perm. */
    private Permutation _permutation;
}
