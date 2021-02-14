package encryption_methods;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class for encrypting and decrypting English text using Polybius Square table.
 *
 * @author namvdo
 */
public class PolybiusSquare {

    private static final Logger logger = Logger.getLogger(PolybiusSquare.class.getName());
    private static final int ODD = 1;
    private static final int INCREMENT_STEP = 2;
    private static final int CELL_CONTAINS_TWO_VALUES_POS = 9;
    private static final int NO_OF_ROWS = 5;
    private static final int NO_OF_COLUMNS = 5;

    private PolybiusSquare() {

    }
    /**
     * Returns the sequence of number after encrypting the given text, each letter is
     * represented by a pair of numbers and each number has the range from 1 to 5, the first number of
     * this pair represents the row, and the second number represents the column in the Polybius table.
     *
     * @param text the text to be encrypted.
     * @return the sequence of number generated with the given text
     */
    public static String encrypt(String text) {
        text = text.toUpperCase();
        String[] words = text.split(" ");
        StringBuilder code = new StringBuilder();
        for (String word : words) {
            StringBuilder num = new StringBuilder();
            char[] letters = word.toCharArray();
            for (int i = 0; i < letters.length; i++) {
                char letter = letters[i];
                int curPos;
                if (letter < 'I') {
                    curPos = letter - 64;
                } else if (!isAdjacentDuplicateSpecialCell(letters, i)) {
                    curPos = CELL_CONTAINS_TWO_VALUES_POS;
                } else if (isAdjacentDuplicateSpecialCell(letters, i)) {
                    continue;
                } else {
                    curPos = letter - 65;
                }
                int curRow = ((int) Math.ceil(curPos / (float) NO_OF_ROWS));
                int curCol = curPos % NO_OF_COLUMNS;
                if (curCol == 0) {
                    curCol = NO_OF_COLUMNS;
                }
                num.append(curRow).append(curCol);
            }
            code.append(num).append(" ");
        }
        return code.toString();
    }

    /**
     * Decrypts more than one sequence separated with a deliminator.
     *
     * @param sequence      the even size sequence number needs decrypting
     * @param splitSequence the sequence used to split array's elements
     * @return the message after decoding with the given sequence number
     */
    public static String decryptSeqWithDelim(String sequence, String splitSequence) {
        StringBuilder msg = new StringBuilder();
        if (sequence.length() % 2 == ODD) {
            throw new IllegalArgumentException("The sequence number size must be even.");
        }
        String[] numbers = sequence.split(splitSequence);
        for (String s : numbers) {
            String decryptedWord = decrypt(s);
            msg.append(decryptedWord);
            msg.append(" ");
        }
        return msg.toString();
    }

    /**
     * Returns the message after decoding using Polybius Square, the Polybius table contains
     * 5 rows and 5 columns with the indices from 1 to 25, each cell represents an English letter,
     * except one cell contains two letters since there are 26 alphabet letters in English.
     * @param code the code to be decrypted
     */
    public static String decrypt(String code) {
        StringBuilder msg = new StringBuilder();
        for (int j = 0; j < code.length() - 1; j += INCREMENT_STEP) {
            int curRowPos = Integer.parseInt(code.charAt(j) + "") - 1;
            int curColPos = Integer.parseInt(code.charAt(j + 1) + "");
            int curPos = curRowPos * NO_OF_ROWS + curColPos;
            if (curPos < CELL_CONTAINS_TWO_VALUES_POS) {
                msg.append((char) (curPos + 64));
            } else if (curPos == CELL_CONTAINS_TWO_VALUES_POS) {
                msg.append("I/J");
            } else {
                msg.append((char) (curPos + 65));
            }
        }
        return msg.toString();
    }

    private static boolean isAdjacentDuplicateSpecialCell(char[] letters, int curPos) {
        return curPos % 2 == 1 && letters[curPos] == 'I' || letters[curPos] == 'J'
                && (letters[curPos - 1] == 'I' || letters[curPos - 1] == 'J');
    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "{0}", encrypt("HELLO THERE HOW ARE YOU"));
        logger.log(Level.INFO, "{0}", decryptSeqWithDelim("2315313134 4423154215 233452 114215 543445", " "));
        logger.log(Level.INFO, "{0}", decrypt("2315313134"));
    }
}
