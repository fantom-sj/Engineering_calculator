package com.example.engineering_calculator;

import android.util.Log;

import java.util.Stack;

public class Calculator {
    private String result;

    public void onClickITOG(String input_str){
        StringBuilder output = GetExpress(new StringBuilder(input_str));
        double res = Counting(output);
        double drob = res - Math.floor(res);
        if(drob == 0.0)
            result = String.valueOf(Math.round(res));
        else
            result = String.valueOf(res);
    }

    static private StringBuilder GetExpress(StringBuilder input) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operStack = new Stack<Character>();

        for (int i = 0; i < input.length(); i++) {

            if (IsDelimeter(input.charAt(i)))
                continue;

            if (Character.isDigit(input.charAt(i))) {

                while (!IsDelimeter(input.charAt(i)) && !IsOperator(input.charAt(i))) {
                    output.append(input.charAt(i));
                    i++;

                    if (i == input.length()) break;
                }

                output.append(" ");
                i--;
            }

            if (IsOperator(input.charAt(i))) {
                if (input.charAt(i) == '(') {
                    operStack.push(input.charAt(i));
                }
                else if (input.charAt(i) == ')') {
                    char s = operStack.pop();

                    while (s != '(') {
                        output.append(s);
                        output.append(' ');
                        s = operStack.pop();
                    }
                }
                else if (input.charAt(i) == '±'){
                    input.deleteCharAt(i);
                    input.insert(i, "(0-1)*");
                    i--;
                }
                else if (input.charAt(i) == 's'){
                    if (input.charAt(i+1) == 'q'){
                        if (input.charAt(i+3) == '2'){              //Корень sqr2(x)
                            input = SolvExpInFunc(input, "sqr2", i);
                            i--;
                            Log.d("Команда", "корень sqr2(x)");
                        }
                        else if (input.charAt(i+3) == '3'){         //Корень sqr3(x)
                            input = SolvExpInFunc(input, "sqr3", i);
                            i--;
                            Log.d("Команда", "корень sqr3(x)");
                        }
                    }
                    else if (input.charAt(i+1) == 'e'){             //Секанс sec(x)
                        input = SolvExpInFunc(input, "sec", i);
                        i--;
                        Log.d("Команда", "cеканс sec(x)");
                    }
                    else if (input.charAt(i+1) == 'i'){             //Синус sin(x)
                        input = SolvExpInFunc(input, "sin", i);
                        i--;
                        Log.d("Команда", "cинус sin(x)");
                    }
                }
                else if (input.charAt(i) == 'c'){
                    if (input.charAt(i+3) == 'e'){                  //Косеканс cosec(x)
                        input = SolvExpInFunc(input, "cosec", i);
                        i--;
                        Log.d("Команда", "косеканс cosec(x)");
                    }
                    else if (input.charAt(i+1) == 'o'){             //Косинус cos(x)
                        input = SolvExpInFunc(input, "cos", i);
                        i--;
                        Log.d("Команда", "косинус cos(x)");
                    }
                    else if (input.charAt(i+1) == 't'){             //Котангенс ctg(x)
                        input = SolvExpInFunc(input, "ctg", i);
                        i--;
                        Log.d("Команда", "котангенс ctg(x)");
                    }
                }
                else if (input.charAt(i) == 'l'){
                    if (input.charAt(i+1) == 'o'){                  //Логорифм log10(x, y)
                        input = SolvExpInFunc(input, "log", i);
                        i--;
                        Log.d("Команда", "логорифм log10(x)");
                    }
                    else if (input.charAt(i+1) == 'n'){             //Натуральный логорифм ln(x)
                        input = SolvExpInFunc(input, "ln", i);
                        i--;
                        Log.d("Команда", "натуральный логорифм ln(x)");
                    }
                }
                else if (input.charAt(i) == 'e'){         //Экспонента exp(x)
                    input = SolvExpInFunc(input, "exp", i);
                    i--;
                    Log.d("Команда", "экспнента exp(x)");
                }
                else if (input.charAt(i) == 't'){                   //Тангенс tg(x)
                    input = SolvExpInFunc(input, "tg", i);
                    i--;
                    Log.d("Команда", "тангенс tg(x)");
                }
                else { //Если любой другой оператор
                    if (operStack.size() > 0) //Если в стеке есть элементы
                        if (GetPriority(input.charAt(i)) <= GetPriority(operStack.peek())) { //И если приоритет нашего оператора меньше или равен приоритету оператора на вершине стека
                            output.append(operStack.pop()); //То добавляем последний оператор из стека в строку с выражением
                            output.append(' ');
                        }
                    operStack.push(input.charAt(i)); //Если стек пуст, или же приоритет оператора выше - добавляем операторов на вершину стека
                }
            }
        }
        Log.d("input", String.valueOf(input));

        while (operStack.size() > 0) {
            output.append(operStack.pop());
            output.append(' ');
        }

        return output;
    }

    static private double Counting(StringBuilder input) {
        double res = 0;
        Stack<Double> temp = new Stack<Double>();

        for (int i = 0; i < input.length(); i++) {

            if (Character.isDigit(input.charAt(i))) {
                StringBuilder a = new StringBuilder();

                while (!IsDelimeter(input.charAt(i)) && !IsOperator(input.charAt(i))) {
                    a.append(input.charAt(i));
                    i++;
                    if (i == input.length()) break;
                }
                temp.push(Double.parseDouble(String.valueOf(a)));
                i--;
            }
            else if (IsOperator(input.charAt(i))) {

                double operand_1 = temp.pop();
                double operand_2 = temp.pop();

                switch (input.charAt(i)) {
                    case '+':
                        res = operand_2 + operand_1;
                        break;
                    case '-':
                        res = operand_2 - operand_1;
                        break;
                    case '*':
                        res = operand_2 * operand_1;
                        break;
                    case '/':
                        res = operand_2 / operand_1;
                        break;
                    case '^':
                        res = Double.parseDouble(
                            String.valueOf(Math.pow(Double.parseDouble(String.valueOf(operand_2)),
                            Double.parseDouble(String.valueOf(operand_1)))));
                        break;
                }
                temp.push(res);
            }
        }
        return temp.peek();
    }

    static private Boolean IsDelimeter(char operator) {
        if ((" =".indexOf(operator) != -1))
            return true;
        return false;
    }

    static private Boolean IsOperator(char operator) {
        if (("+-/*^()±sclet".indexOf(operator) != -1))
            return true;
        return false;
    }

    static private byte GetPriority(char operator) {
        switch (operator) {
            case '(':
                return 0;
            case ')':
                return 1;
            case '+':
                return 2;
            case '-':
                return 3;
            case '*':
            case '/':
                return 4;
            case '^':
                return 5;
            default:
                return 6;
        }
    }

    //Вычисление выражения внутри функции
    static private StringBuilder SolvExpInFunc(StringBuilder input, String func, Integer i){
        Log.d("input до удаления", String.valueOf(input));
        int i1 = 0, i2 = i;
        switch (func) {
            case "sqr2":
            case "sqr3":
                i1 = i + 5;
                break;
            case "sec":
            case "sin":
            case "cos":
            case "ctg":
            case "log":
            case "exp":
                i1 = i + 4;
                break;
            case "cosec":
                i1 = i + 6;
                break;
            case "ln":
            case "tg":
                i1 = i + 3;
                break;
        }

        while (input.charAt(i2) != ')')
            i2++;

        String str_exp = input.substring(i1, i2);
        Log.d("str_exp", str_exp);
        Calculator local_calc  = new Calculator();
        local_calc.onClickITOG(str_exp);
        Double res = Double.parseDouble(local_calc.getResult());

        switch (func) {
            case "sqr2":
                res = Math.sqrt(res);
                break;
            case "sqr3":
                res = Math.cbrt(res);
                break;
            case "exp":
                res = Math.exp(res);
                break;
            case "sec":
                res = 1 / Math.cos(res);
                break;
            case "sin":
                res = Math.sin(res);
                break;
            case "cos":
                res = Math.cos(res);
                break;
            case "ctg":
                res = 1 / Math.tan(res);
                break;
            case "log":
                res = Math.log10(res);
                break;
            case "cosec":
                res = 1 / Math.sin(res);
                break;
            case "ln":
                res = Math.log(res);
                break;
            case "tg":
                res = Math.tan(res);
                break;
        }

        res = (double) Math.round(res*100000) / 100000;
        if (res >= 0){
            input.delete(i, i2+1);
            input.insert(i, String.valueOf(res));
        }
        else {
            input.delete(i, i2+1);
            input.insert(i, "0");
            input.insert(i+1, String.valueOf(res));
        }
        Log.d("input после вычисления", String.valueOf(input));

        return input;
    }

    public String getResult(){
        return result;
    }
}
