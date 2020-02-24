package PostfixNotation;

import java.nio.charset.CharsetEncoder;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Project: [P3]AplicatiiStiveCozi
 * Author: mihai
 * Date: 2/23/2020
 */
public class PostfixNotation {
    // Operators: associativity + precedence
    public enum Operator{
        INCREASE ("left", 11),
        DECREASE ("left", 11),
        MULTIPLICATION ("left", 12),
        DIVISION ("left", 12),
        EXPONENTIATION ("right", 13);

        private final String associativity;
        private final int precedence;

        Operator(String associativity, int precedence){
            this.associativity = associativity;
            this.precedence = precedence;
        }


        // get associativity - READ ONLY
        public String operatorAssociativity(){
            return this.associativity;
        }


        // get precedence - READ ONLY
        public int operatorPrecedence(){
            return this.precedence;
        }
    }


    // Get Associativity for each operator
    public static String getAssociativity(Character c) throws Exception {
        switch (c){
            case '+':
                return Operator.INCREASE.operatorAssociativity();
            case '-':
                return Operator.DECREASE.operatorAssociativity();
            case '*':
                return Operator.MULTIPLICATION.operatorAssociativity();
            case '/':
                return Operator.DIVISION.operatorAssociativity();
            case '^':
                return Operator.EXPONENTIATION.operatorAssociativity();
            default:
                throw new Exception("Invalid operator!");
        }
    }


    // Get Precedence for each operator
    public static int getPrecedence(Character c) throws Exception {
        switch (c){
            case '+':
                return Operator.INCREASE.operatorPrecedence();
            case '-':
                return Operator.DECREASE.operatorPrecedence();
            case '*':
                return Operator.MULTIPLICATION.operatorPrecedence();
            case '/':
                return Operator.DIVISION.operatorPrecedence();
            case '^':
                return Operator.EXPONENTIATION.operatorPrecedence();
            default:
                throw new Exception("Invalid operator!");
        }
    }


    // Convert from INFIX NOTATION ---> POSTFIX NOTATION
    public static Deque<Character> postfixNotation(String infixNotation) throws Exception {
        Deque<Character> postfixQueue = new LinkedList<>();
        Deque<Character> stackOp = new LinkedList<>();

        for (Character character : infixNotation.toCharArray()){
            if (Character.isDigit(character)){
                postfixQueue.addLast(character);
            }else if (character == '+' || character == '-' || character == '*' || character == '/' || character == '^'){
                while ((!stackOp.isEmpty()) &&
                        (stackOp.getFirst() != '(') &&
                        (getPrecedence(character) < getPrecedence(stackOp.getFirst()) ||
                                getPrecedence(character) == getPrecedence(stackOp.getFirst()) &&
                                        getAssociativity(stackOp.getFirst()).equalsIgnoreCase("left"))) {

                    postfixQueue.addLast(stackOp.removeFirst());
                }
                stackOp.addFirst(character);
            }else if (character == '('){
                stackOp.addFirst(character);
            }else if (character == ')') {
                char removedChar = ' ';
                while (!stackOp.isEmpty() && stackOp.getFirst() != '(') {
                    removedChar = stackOp.removeFirst();
                    postfixQueue.addLast(removedChar);

                    if (stackOp.isEmpty() && removedChar != '('){
                        throw new Exception("Wrong brackets!");
                    }
                }
                stackOp.removeFirst();
            }
            System.out.println("Postfix notation Queue: " + postfixQueue);
            System.out.println("Operators stack: " + stackOp);
        }

        while (!stackOp.isEmpty()){
            char removedChar = stackOp.removeFirst();
            postfixQueue.addLast(removedChar);

            if (removedChar == '('){
                throw new Exception("Wrong brackets!");
            }
        }
        return postfixQueue;
    }


    // Calculate the result
    public static int calculationOfResult(char operator, int op1, int op2) throws Exception {
        switch (operator){
            case '+':
                return (op2 + op1);
            case '-':
                return (op2 - op1);
            case '*':
                return (op2 * op1);
            case '/':
                return (op2 / op1);
            case '^':
                int result = 1;

                for (int i = 1; i <= op1; i++){
                    result *= op2;
                }
                return result;
            default:
                throw new Exception("Wrong operator!");
        }
    }


    // Evaluate the POSTFIX NOTATION
    public static int expressionEvaluation(Deque<Character> postfixNotation) throws Exception {
        Deque<Integer> stackOp = new LinkedList<>();
        int op1 = 0;
        int op2 = 0;
        int result = 0;
        int finalResult = 0;

        // iterate through "postfixNotation"
        for (Character character : postfixNotation){
            if (Character.isDigit(character)) {
                int charToInteger = Integer.parseInt(String.valueOf(character));
                stackOp.addFirst(charToInteger);
            }else if (character == '+' || character == '-' || character == '*' || character == '/' || character == '^') {
                if (stackOp.size() >= 2){
                    op1 = stackOp.removeFirst();
                    op2 = stackOp.removeFirst();
                }else {
                    throw new Exception("Wrong postfix notation!");
                }

                // result in "int"
                result = calculationOfResult(character, op1, op2);
                stackOp.addFirst(result);
            }
        }

        finalResult = stackOp.removeFirst();
        if (!stackOp.isEmpty()){
            throw new Exception("Wrong postfix notation!");
        }
        return finalResult;
    }



    public static void main(String[] args) throws Exception {

        // POSTFIX NOTATION
        String infixNotation = "3+(2+1)*2^3^2-8/(5-1*2/2)";
        String infixNotation1 = "2 + 1";

        System.out.println("~~~ POSTFIX NOTATION ~~~");
        Deque<Character> postfixNotation = postfixNotation(infixNotation);
        System.out.println("First infixNotation:\n" + postfixNotation);
        System.out.println();

        Deque<Character> postFixNotation1 = postfixNotation(infixNotation1);
        System.out.println("Second infixNotation:\n" + postFixNotation1);
        System.out.println("\n");


        // EXPRESSION EVALUATION
        System.out.println("Expression Evaluation: " + expressionEvaluation(postfixNotation));
    }
}
