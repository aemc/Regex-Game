package ca.aemc.rex1;

import java.util.Random;

public class RexModel{
    public static final int SET_SIZE = 3;
    private boolean letter;
    private boolean digit;
    private boolean anchor;
    private String regex;
    private Random rng;

    public RexModel()
    {
        this.regex = "";
        this.digit = this.letter = this.anchor = true;
        this.rng = new Random();
    }

    public void setDigit(boolean digit) {
        this.digit = digit;
    }

    public void setLetter(boolean letter) {
        this.letter = letter;
    }

    public void setAnchor(boolean anchor) {
        this.anchor = anchor;
    }

    public String getRex() {
        return regex;
    }

    public boolean doesMatch(String s) {
        return s.matches(regex);
    }

    public void generate(int repeat) {
        this.regex = "";
        if (anchor == true)
        {
            this.regex += "^";
        }
            for (int i = 0; i < repeat; i++) {
                genDigit();
                genLetter();
            }
        if(anchor == true)
        {
            this.regex += "$";
        }
    }

    private void genDigit() {
        double check = rng.nextDouble();
        String str = "";
        if (digit == true) {
            if (check < 0.5) {
                this.regex += "[0-9]";
            }
            else if (check > 0.5) {
                for(int i=0; i<SET_SIZE;i++)
                {
                    int x = rng.nextInt(9);
                    String y = Integer.toString(x);
                    if(!str.contains(y))
                    {
                        str += y;
                    }
                    else if(str.contains(y))
                    {
                        i--;
                    }
                }
                this.regex += "[" + str + "]";
            }
            genQuantifier();
        }
    }

    private void genLetter() {
        double check = rng.nextDouble();
        double check2 = rng.nextDouble();
        String str = "";
        if (letter == true)
        {
            if (check < 0.5)
            {
                if (check2 <= 0.25)
                {
                    this.regex += "[^a-z]";
                }
                else
                {
                    this.regex += "[a-z]";
                }
            }
            else if (check > 0.5) {
                this.regex += "[";
                if (check2 <= 0.25) {
                    this.regex += "^";
                }
                for (int i = 0; i < SET_SIZE; i++) {
                    int x = 97 + rng.nextInt(26);
                    String y = Character.toString((char) x);
                    if(!str.contains(y))
                    {
                        str += y;
                    }
                    else if(str.contains(y))
                    {
                        i--;
                    }
                }
                this.regex += str;
                this.regex += "]";
            }
            genQuantifier();
        }
    }

    private void genQuantifier() {
        double check = rng.nextDouble();
        if (check < 0.5) {
            this.regex +="{";
            int x = 1 + rng.nextInt(SET_SIZE);
            String y = Integer.toString(x);
            this.regex += y + "}";
        }
        else if (check > 0.5) {
            if (check < 0.66)
                this.regex += "*";
            else if (check < 0.82)
                this.regex += "+";
            else if (check < 0.98)
                this.regex += "?";
        }
    }

    // model test
    public static void main(String[] args) {
        RexModel model = new RexModel();
        model.setAnchor(true);
        model.setLetter(false);
        model.setDigit(false);
        model.generate(2);
        System.out.println("rex: "+ model.getRex());

    }
}