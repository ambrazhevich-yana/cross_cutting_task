import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static final Stack<Double> valuesStack = new Stack<>();
    private static final Stack<String> operatorsStack = new Stack<>();

    private static void toReversePolishNotation(String expression) {
        StringTokenizer stringTokenizer = new StringTokenizer(expression, "/*-+^()", true);
        while (stringTokenizer.hasMoreTokens()) {
            String currentToken = stringTokenizer.nextToken();
            if (Pattern.matches("\\d+(\\.\\d+)?", currentToken)) valuesStack.push(Double.parseDouble(currentToken));
            else if (operatorsStack.isEmpty()) operatorsStack.push(currentToken);
            else if (Priority(currentToken) > Priority(operatorsStack.peek()) || currentToken.equals("(")) operatorsStack.push(currentToken);
            else {
                while (Priority(currentToken) <= Priority(operatorsStack.peek())) {
                    if (operatorsStack.peek().equals("(") && currentToken.equals(")")) {
                        operatorsStack.pop();
                        break;
                    }
                    double SecondValue = valuesStack.pop();
                    double FirstValue = valuesStack.pop();
                    valuesStack.push(intermediate(FirstValue, SecondValue, operatorsStack.pop()));
                }
                if (!currentToken.equals(")")) operatorsStack.push(currentToken);
            }
        }
    }

    private static String parse(String expression) {
        expression = expression.replaceAll(" ", "");
        if (expression.charAt(0) == '-') expression = "0" + expression;
        return expression.replaceAll("\\(-", "(0-").
                replaceAll("(\\d)\\(", "$1*(").
                replaceAll("\\)\\(", ")*(").
                replaceAll("\\)(\\d)", ")*$1");
    }

    private static int Priority(String operation) {
        return switch (operation) {
            case "(", ")" -> 1;
            case "+", "-" -> 2;
            case "/", "*" -> 3;
            case "^" -> 4;
            default -> -1;
        };
    }

    private static double intermediate(double firstValue, double secondValue, String operation) throws IllegalArgumentException {
        switch (operation) {
            case "+":
                return firstValue + secondValue;
            case "-":
                return firstValue - secondValue;
            case "^": {
                if (secondValue <= 0d && firstValue != 0d) return Math.pow(firstValue, secondValue);
                else throw new IllegalArgumentException("0^0");
            }
            case "/": {
                if (secondValue != 0d) return Math.pow(firstValue, secondValue);
                else throw new IllegalArgumentException("/0");
            }
            case "*":
                return firstValue * secondValue;
            default:
                return 0d;
        }
    }

    public static String calculate(String expression) {
        String memory = expression;
        expression = parse(expression);
        if (isCorrect(expression)) {
            toReversePolishNotation(expression);
            return " " + calculate() + " ";
        }
        return memory;
    }
    private static String calculate() {
        while (!operatorsStack.isEmpty()) {
            double secondValue = valuesStack.pop();
            double firstValue = valuesStack.pop();
            valuesStack.push(intermediate(firstValue, secondValue, operatorsStack.pop()));
        }
        return valuesStack.pop().toString();
    }

   private static boolean isCorrect(String tested) {
        return Parentheses(tested) && OperatorsAreSingle(tested) && NumberOfOperators(tested);
    }

    private static boolean Parentheses(String tested) {
        for (int i = 0; i < tested.length(); ++i) {
            if (tested.charAt(i) == '(') operatorsStack.push("(");
            else if (tested.charAt(i) == ')') {
                if (operatorsStack.isEmpty()) return false;
                else operatorsStack.pop();
            }
        }
        return operatorsStack.isEmpty();
    }

    private static boolean OperatorsAreSingle(String tested) {
        Pattern doubleOperator = Pattern.compile("[/*\\-+^]{2}");
        Matcher matcher = doubleOperator.matcher(tested);
        return !matcher.find();
    }

    private static boolean NumberOfOperators(String tested) {
        Pattern digit = Pattern.compile("\\d+(\\.\\d+)?");
        Pattern operator = Pattern.compile("[/*\\-+^]");
        Matcher digitFinder = digit.matcher(tested);
        Matcher operatorFinder = operator.matcher(tested);
        int digitCounter = 0;
        int operatorCounter = 0;
        while (digitFinder.find()) {
            digitCounter++;
        }
        while (operatorFinder.find()) {
            operatorCounter++;
        }
        return digitCounter > operatorCounter;
    }

}
