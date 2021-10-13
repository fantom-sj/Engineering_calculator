package com.example.engineering_calculator;

import java.util.Stack;

public class Calculater {
    private String result;

    public void onClickPlus(String input_str){
        StringBuilder output = GetExpression(new StringBuilder(input_str)); //Преобразовываем выражение в постфиксную запись
        result = String.valueOf(Counting(output)); //Возвращаем результат
    }

    static private StringBuilder GetExpression(StringBuilder input) {
        StringBuilder output = new StringBuilder(); //Строка для хранения выражения
        Stack<Character> operStack = new Stack<Character>(); //Стек для хранения операторов

        for (int i = 0; i < input.length(); i++) { //Для каждого символа в входной строке
            //Разделители пропускаем
            if (IsDelimeter(input.charAt(i)))
                continue; //Переходим к следующему символу

            //Если символ - цифра, то считываем все число
            if (Character.isDigit(input.charAt(i))) { //Если цифра
                //Читаем до разделителя или оператора, что бы получить число
                while (!IsDelimeter(input.charAt(i)) && !IsOperator(input.charAt(i))) {
                    output.append(input.charAt(i)); //Добавляем каждую цифру числа к нашей строке
                    i++; //Переходим к следующему символу

                    if (i == input.length()) break; //Если символ - последний, то выходим из цикла
                }

                output.append(" "); //Дописываем после числа пробел в строку с выражением
                i--; //Возвращаемся на один символ назад, к символу перед разделителем
            }

            //Если символ - оператор
            if (IsOperator(input.charAt(i))) { //Если оператор
                if (input.charAt(i) == '(') //Если символ - открывающая скобка
                    operStack.push(input.charAt(i)); //Записываем её в стек
                else if (input.charAt(i) == ')') { //Если символ - закрывающая скобка
                    //Выписываем все операторы до открывающей скобки в строку
                    char s = operStack.pop();

                    while (s != '(') {
                        output.append(s);
                        output.append(' ');
                        s = operStack.pop();
                    }
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

        //Когда прошли по всем символам, выкидываем из стека все оставшиеся там операторы в строку
        while (operStack.size() > 0) {
            output.append(operStack.pop());
            output.append(' ');
        }

        return output; //Возвращаем выражение в постфиксной записи
    }

    static private double Counting(StringBuilder input) {
        double result = 0; //Результат
        Stack<Double> temp = new Stack<Double>(); //Dhtvtyysq стек для решения

        for (int i = 0; i < input.length(); i++) { //Для каждого символа в строке
            //Если символ - цифра, то читаем все число и записываем на вершину стека
            if (Character.isDigit(input.charAt(i))) {
                StringBuilder a = new StringBuilder();

                while (!IsDelimeter(input.charAt(i)) && !IsOperator(input.charAt(i))) { //Пока не разделитель
                    a.append(input.charAt(i)); //Добавляем
                    i++;
                    if (i == input.length()) break;
                }
                temp.push(Double.parseDouble(String.valueOf(a))); //Записываем в стек
                i--;
            }
            else if (IsOperator(input.charAt(i))) //Если символ - оператор
            {
                //Берем два последних значения из стека
                double a = temp.pop();
                double b = temp.pop();

                switch (input.charAt(i)) { //И производим над ними действие, согласно оператору
                    case '+': result = b + a; break;
                    case '-': result = b - a; break;
                    case '*': result = b * a; break;
                    case '/': result = b / a; break;
                    case '^': result = Double.parseDouble(String.valueOf(Math.pow(Double.parseDouble(String.valueOf(b)),
                            Double.parseDouble(String.valueOf(a))))); break;
                }
                temp.push(result); //Результат вычисления записываем обратно в стек
            }
        }
        return temp.peek(); //Забираем результат всех вычислений из стека и возвращаем его
    }

    //Метод возвращает true, если проверяемый символ - разделитель ("пробел" или "равно")
    static private Boolean IsDelimeter(char c) {
        if ((" =".indexOf(c) != -1))
            return true;
        return false;
    }

    //Метод возвращает true, если проверяемый символ - оператор
    static private Boolean IsOperator(char с) {
        if (("+-/*^()".indexOf(с) != -1))
            return true;
        return false;
    }

    //Метод возвращает приоритет оператора
    static private byte GetPriority(char s) {
        switch (s) {
            case '(': return 0;
            case ')': return 1;
            case '+': return 2;
            case '-': return 3;
            case '*':
            case '/':
                return 4;
            case '^': return 5;
            default: return 6;
        }
    }

    public String getResult(){
        return result;
    }
}
