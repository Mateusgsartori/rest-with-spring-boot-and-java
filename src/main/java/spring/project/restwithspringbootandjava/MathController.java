package spring.project.restwithspringbootandjava;

import org.springframework.web.bind.annotation.*;
import spring.project.converters.NumberConverter;
import spring.project.exceptions.UnsupportedMathOperationException;
import spring.project.math.SimpleMath;

@RestController
public class MathController {

    private final SimpleMath math = new SimpleMath();

    @RequestMapping(value="/sum/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double sum(@PathVariable("numberOne") String numberOne,
                      @PathVariable("numberTwo") String numberTwo) throws Exception {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.sum(numberOne, numberTwo);
    }

    @RequestMapping(value="/subtraction/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double subtraction(@PathVariable("numberOne") String numberOne,
                      @PathVariable("numberTwo") String numberTwo) throws Exception {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.subtraction(numberOne, numberTwo);
    }

    @RequestMapping(value="/multiplication/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double multiplication(@PathVariable("numberOne") String numberOne,
                              @PathVariable("numberTwo") String numberTwo) throws Exception {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.multiplication(numberOne, numberTwo);
    }

    @RequestMapping(value="/division/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double division(@PathVariable("numberOne") String numberOne,
                                 @PathVariable("numberTwo") String numberTwo) throws Exception {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.division(numberOne, numberTwo);
    }

    @RequestMapping(value="/mean/{numberOne}/{numberTwo}", method=RequestMethod.GET)
    public Double mean(@PathVariable("numberOne") String numberOne,
                           @PathVariable("numberTwo") String numberTwo) throws Exception {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.mean(numberOne, numberTwo);
    }

    @RequestMapping(value="/sqroot/{number}", method=RequestMethod.GET)
    public Double sqroot(@PathVariable("number") String number) throws Exception {
        if (!NumberConverter.isNumeric(number)) {
            throw new UnsupportedMathOperationException("Please, set a numeric value!");
        }
        return math.sqroot(number);
    }


}
