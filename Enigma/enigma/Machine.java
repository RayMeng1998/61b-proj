package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ziyue Meng
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotor = allRotors;
        _plugboard = new Permutation("", alpha);
        myRotors = new ArrayList<Rotor>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        try {
            for (int i = 0; i < rotors.length; i++) {
                for (int j = 0; j < rotors.length; j++) {
                    if (rotors[i].equals(rotors[j]) && i != j) {
                        throw error("Duplicated rotor");
                    }
                }
            }
            myRotors = new ArrayList<>();
            for (String j : rotors) {
                for (Rotor i : _allRotor) {
                    if (i.name().toUpperCase().equals(j)) {
                        myRotors.add(i);
                        break;
                    }
                }
            }
            _mRotor = new Rotor[myRotors.size()];
            _mRotor = myRotors.toArray(new Rotor[myRotors.size()]);
            if (!_mRotor[0].reflecting()) {
                throw error("first must be reflector");
            }
        } catch (IndexOutOfBoundsException excp) {
            throw error("Wrong num of rotors");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        int i = 0;
        for (Rotor j : myRotors) {
            if (i != 0) {
                if (!_alphabet.contains(setting.charAt(i - 1))) {
                    throw error("Wrong format");
                }
                j.set(setting.charAt(i - 1));
            }
            i++;
        }
        _mRotor = myRotors.toArray(new Rotor[myRotors.size()]);
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        c = _plugboard.permute(_plugboard.wrap(c));
        if (_mRotor[_mRotor.length - 1].atNotch()
                && _mRotor[_mRotor.length - 2].rotates()) {
            _mRotor[_mRotor.length - 1].advance();
            _mRotor[_mRotor.length - 2].advance();
        }  else {
            _mRotor[_mRotor.length - 1].advance();
            for (int i = _mRotor.length - 2; i > 1; i--) {
                if (_mRotor[i].atNotch()
                        && _mRotor[i].rotates() && _mRotor[i - 1].rotates()) {
                    _mRotor[i].advance();
                    _mRotor[i - 1].advance();
                }
            }
        }
        for (int i = _mRotor.length - 1; i >= 0; i--) {
            c = _mRotor[i].convertForward(c);
        }
        for (int i = 1; i < _mRotor.length; i++) {
            c = _mRotor[i].convertBackward(c);
        }
        return _plugboard.permute(c);


    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.toUpperCase();
        char[] mes = msg.toCharArray();
        int[] mess = new int[mes.length];
        for (int i = 0; i < mes.length; i++) {
            mess[i] = _alphabet.toInt(mes[i]);
            mess[i] = convert(mess[i]);
            mes[i] = _alphabet.toChar(mess[i]);
        }

        return String.valueOf(mes);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** NUMBER of my rotors. */
    private int _numRotors;
    /** NUMBER of my PRAWL. */
    private int _pawls;
    /** all of my rotors. */
    private Collection<Rotor> _allRotor;
    /** plugboard in use. */
    private Permutation _plugboard;
    /** rotors in use. */
    private ArrayList<Rotor> myRotors;
    /** backward rotors. */
    private Rotor[] _mRotor;
}
