package codeGeneration;

/**
 * Classe que permet gestionar els registres lliures de la màquina.
 *
 * @author Sergi Simó Bosquet - ls30685
 * @author Esteve Genovard Ferriol - ls30742
 *
 * Date: 7/7/17
 */
public class Registers {

    private boolean[] registers;

    public Registers() {

        registers = new boolean[18];
    }

    public int askForRegister() {

        for (int i = 0; i < registers.length; i++) {
            if (!registers[i]) {
                registers[i] = true;
                return i + 8;
            }
        }

        return -1;
    }

    public void freeRegister(int register) {

        if (register >= 8 && register < 26) registers[register - 8] = false;
    }
}
