package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ziyue Meng
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _args = new String[3];
        _args = args;
        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        ArrayList<String> input = new ArrayList<>();
        int numofsets = 0;
        while (_input.hasNextLine()) {
            String tmp = _input.nextLine();
            if (tmp.indexOf('*') != -1) {
                numofsets += 1;
            }
            input.add(tmp);
        }
        if (numofsets == 0) {
            throw error("No settings");
        }
        for (String i : input) {
            i = i.toUpperCase();
            if (i.indexOf('*') > -1) {
                _mach = readConfig();
                String[] x = mybreak(i);
                String[] myrotors = x[0].split(" ");
                if (_mach.numRotors() != myrotors.length) {
                    throw error("Too few or too much rotors");
                }
                _mach.insertRotors(myrotors);
                if (x[1].length() != myrotors.length - 1) {
                    throw error("Too few or too much settings");
                }
                if (_mach.numRotors() != myrotors.length) {
                    throw error("Wrong rotor name");
                }
                setUp(_mach, x[1]);
                _mach.setPlugboard(new Permutation(x[2], _alphabet));
            } else {
                i = i.replaceAll(" ", "");
                printMessageLine(_mach.convert(i));
            }
        }
    }
    /** @param econf Return a string[]. To breakdown the conf. */
    private String[] mybreak(String econf) {
        econf = econf.replaceAll("\\* ", "");
        econf = econf.replaceFirst(" \\(", "(");
        String plug = "";
        int plugstart = econf.indexOf("(");
        if (plugstart != -1) {
            plug = econf.substring(plugstart);
            econf = econf.substring(0, plugstart);
        }
        int setstart = econf.lastIndexOf(" ") + 1;
        String settings = econf.substring(setstart);
        String myrotors = econf.substring(0, setstart);
        return new String[] {myrotors, settings, plug};
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _config = getInput(_args[0]);
            if (!_config.hasNext()) {
                throw error("No configure found");
            }
            String tmp = _config.nextLine();
            tmp = tmp.toUpperCase();
            if (tmp.charAt(1) == '-') {
                _alphabet = new CharacterRange(tmp.charAt(0),
                        tmp.charAt(tmp.length() - 1));
            } else {
                _alphabet = new Extracredit(tmp);
            }
            if (!_config.hasNext()) {
                throw error("Wrong settings");
            }
            int numRotor = _config.nextInt();
            if (!_config.hasNext()) {
                throw error("Wrong settings");
            }
            int pawls = _config.nextInt();
            if (numRotor <= pawls) {
                throw error("Too many pawls or too few rotors");
            }
            ArrayList<Rotor> allRotors = new ArrayList<>();
            String first = "";
            String second = "";
            String third = "";
            while (_config.hasNext()) {
                String x = _config.next();
                if (first.equals("")) {
                    first = " " + x.toUpperCase();
                } else if (second.equals("")) {
                    second = " " + x.toUpperCase();
                } else if (x.contains("(")) {
                    third = third + " " + x.toUpperCase();
                } else {
                    allRotors.add(readRotor(first + second + third));
                    first = " " + x.toUpperCase();
                    second = "";
                    third = "";
                }
            }
            allRotors.add(readRotor(first + second + third));
            return new Machine(_alphabet, numRotor, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        } catch (IndexOutOfBoundsException excp) {
            throw error("Wrong Alphebet");
        }
    }

    /** @param oneRotor Return a rotor, reading its description from _config. */
    private Rotor readRotor(String oneRotor) {
        try {
            String[] splited = oneRotor.split(" ");
            String name = splited[1];
            String notches = splited[2].substring(1);
            int cyclestart = oneRotor.indexOf("(");
            if (cyclestart == -1 || !oneRotor.contains(")")) {
                throw error("Wrong format");
            }
            String cycles = oneRotor.substring(cyclestart);
            Permutation perm = new Permutation(cycles, _alphabet);
            if (splited[2].charAt(0) == 'M') {
                return new MovingRotor(name, perm, notches);
            } else if (splited[2].charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else if (splited[2].charAt(0) == 'R') {
                return new Reflector(name, perm);
            } else {
                throw error("bad rotor description");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg == null) {
            return;
        }
        for (int i = 0; i < msg.length(); i += 1) {
            if (i % 6 == 0) {
                msg = msg.substring(0, i) + " " + msg.substring(i);
            }
        }
        msg = msg.trim();
        _output.println(msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** Store args. */
    private String[] _args;
    /** Store Machine in use. */
    private Machine _mach;
}
