package com.example.carlos.appcurso.Domain;

import android.util.Log;

/**
 * Created by Carlos on 25/01/2017.
 */

public class Token {
    public static final int UNKNOWN = -1;
    public static final int NUMBER = 0;
    public static final int OPERATOR = 1;
    public static final int LEFT_PARENTHESIS = 2;
    public static final int RIGHT_PARENTHESIS = 3;

    private int type;
    private double value;
    private char operator;
    private int precedence;

    public Token() {
        type = UNKNOWN;
    }

    public Token(String contents) {
        switch(contents) {
            case "+":
                type = OPERATOR;
                operator = contents.charAt(0);
                precedence = 1;
                break;
            case "-":
                type = OPERATOR;
                operator = contents.charAt(0);
                precedence = 1;
                break;
            case "×":
                type = OPERATOR;
                operator = contents.charAt(0);
                precedence = 2;
                break;
            case "÷":
                type = OPERATOR;
                operator = contents.charAt(0);
                precedence = 2;
                break;
            case "(":
                type = LEFT_PARENTHESIS;
                break;
            case ")":
                type = RIGHT_PARENTHESIS;
                break;
            default:
                type = NUMBER;
                try {
                    value = Double.parseDouble(contents);
                    Log.d("TAG",Double.toString(value));
                } catch (Exception ex) {
                    type = UNKNOWN;
                    Log.d("TAG",Double.toString(value));
                }
        }
    }

    public Token(double x) {
        type = NUMBER;
        value = x;
    }

    int getType() { return type; }
    double getValue() { return value; }
    int getPrecedence() { return precedence; }

    Token operate(double a,double b) {
        double result = 0;
        switch(operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '×':
                result = a * b;
                break;
            case '÷':
                result = a / b;
                break;
        }
        return new Token(result);
    }
}
