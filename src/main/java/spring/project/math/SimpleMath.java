package spring.project.math;

import spring.project.converters.NumberConverter;

public class SimpleMath {

    public double sum(String numberOne, String numberTwo){

        return NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTwo);
    }

    public double subtraction(String numberOne, String numberTWo){

        return NumberConverter.convertToDouble(numberOne) - NumberConverter.convertToDouble(numberTWo);

    }

    public double multiplication(String numberOne, String numberTWo){

        return NumberConverter.convertToDouble(numberOne) * NumberConverter.convertToDouble(numberTWo);

    }

    public double division(String numberOne, String numberTWo){

        return NumberConverter.convertToDouble(numberOne) / NumberConverter.convertToDouble(numberTWo);

    }

    public double mean(String numberOne, String numberTWo){

        return (NumberConverter.convertToDouble(numberOne) + NumberConverter.convertToDouble(numberTWo))/2;

    }

    public double sqroot(String number){

        double converterdNumber = NumberConverter.convertToDouble(number);
        return Math.sqrt(converterdNumber);

    }
}
