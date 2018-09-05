package edu.ptit.vhlee.simplecalc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    public static final char CHAR_ADD = '+';
    public static final char CHAR_SUB = '-';
    public static final char CHAR_MUL = '×';
    public static final char CHAR_DIV = '÷';
    public static final int INDEX_POS = 1;
    public static final int LOW_PRIORITY = 0;
    public static final int MEDIUM_PRIORITY = 1;
    public static final int HIGH_PRIORITY = 2;
    private EditText mEditInput;
    private EditText mEditHistory;
    private int[] mListId = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
            R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
            R.id.button_8, R.id.button_9, R.id.button_dot,
            R.id.button_delete, R.id.button_delete_all, R.id.button_result,
            R.id.button_add, R.id.button_sub, R.id.button_mul, R.id.button_div};

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_0:
                mEditInput.append(getString(R.string.key_0));
                break;
            case R.id.button_1:
                mEditInput.append(getString(R.string.key_1));
                break;
            case R.id.button_2:
                mEditInput.append(getString(R.string.key_2));
                break;
            case R.id.button_3:
                mEditInput.append(getString(R.string.key_3));
                break;
            case R.id.button_4:
                mEditInput.append(getString(R.string.key_4));
                break;
            case R.id.button_5:
                mEditInput.append(getString(R.string.key_5));
                break;
            case R.id.button_6:
                mEditInput.append(getString(R.string.key_6));
                break;
            case R.id.button_7:
                mEditInput.append(getString(R.string.key_7));
                break;
            case R.id.button_8:
                mEditInput.append(getString(R.string.key_8));
                break;
            case R.id.button_9:
                mEditInput.append(getString(R.string.key_9));
                break;
            case R.id.button_dot:
                mEditInput.append(getString(R.string.key_dot));
                break;
            case R.id.button_delete:
                mEditInput.setText(removeLastChar(mEditInput));
                break;
            case R.id.button_delete_all:
                mEditInput.setText("");
                mEditHistory.setText("");
                break;
            case R.id.button_result:
                excuteMath(mEditInput.getText().toString());
                break;
            case R.id.button_add:
                mEditInput.append(getString(R.string.key_add));
                break;
            case R.id.button_sub:
                mEditInput.append(getString(R.string.key_sub));
                break;
            case R.id.button_mul:
                mEditInput.append(getString(R.string.key_mul));
                break;
            case R.id.button_div:
                mEditInput.append(getString(R.string.key_div));
                break;
            default: break;
        }
    }

    private void initViews(View view) {
        mEditInput = view.findViewById(R.id.edit_input);
        mEditHistory = view.findViewById(R.id.edit_history);
        for (int id : mListId) {
            Button button = view.findViewById(id);
            button.setOnClickListener(this);
        }
    }

    private float processEvaluate(char element, float number1, float number2) {
        float result = 0f;
        switch (element) {
            case CHAR_ADD:
                result = number2 + number1;
                break;
            case CHAR_SUB:
                result = number2 - number1;
                break;
            case CHAR_MUL:
                result = number2 * number1;
                break;
            case CHAR_DIV:
                result = number2 / number1;
                break;
            default:
                break;
        }
        return result;
    }

    private String convertResult(String result) {
        float floatResult = Float.parseFloat(result);
        int intResult = (int) floatResult;
        if (floatResult - intResult == 0) return String.valueOf(intResult);
        return String.valueOf(floatResult);
    }

    private void excuteMath(String mathString) {
        mEditHistory.setText(mathString);
        String[] mTemp = convertMath(formatMath(mathString));
        String result = caculatorMath(mTemp);
        mEditInput.setText(convertResult(result));
    }

    private String[] formatMath(String mathString) {
        String formartedMath = "";
        for (int i = 0; i < mathString.length(); i++) {
            char element = mathString.charAt(i);
            if (!isOperator(element))
                formartedMath = formartedMath + element;
            else formartedMath = formartedMath + " " + element + " ";
        }
        formartedMath = formartedMath.trim();
        return formartedMath.split(" ");
    }

    private String[] convertMath(String[] formatedMath) {
        String convertedMath = "";
        Stack<String> stackElement = new Stack<>();
        for (String element : formatedMath) {
            char operator = element.charAt(0);
            if (!isOperator(operator))
                convertedMath = convertedMath + " " + element;
            else {
                while (!stackElement.isEmpty()
                        && priority(stackElement.peek().charAt(0)) >= priority(operator)) {
                    convertedMath = convertedMath + " " + stackElement.peek();
                    stackElement.pop();
                }
                stackElement.push(element);
            }
        }
        while (!stackElement.isEmpty()) {
            convertedMath = convertedMath + " " + stackElement.peek();
            stackElement.pop();
        }
        return convertedMath.split(" ");
    }

    public String caculatorMath(String[] convertedExp) {
        Stack<String> stackElement = new Stack<>();
        for (int i = INDEX_POS; i < convertedExp.length; i++) {
            char operator = convertedExp[i].charAt(0);
            if (!isOperator(operator)) stackElement.push(convertedExp[i]);
            else {
                float number1 = Float.parseFloat(stackElement.pop());
                float number2 = Float.parseFloat(stackElement.pop());
                float result = processEvaluate(operator, number1, number2);
                stackElement.push(Float.toString(result));
            }
        }
        return stackElement.pop();
    }

    private String removeLastChar(EditText editText) {
        String target = editText.getText().toString();
        if (target.equals("")) return target;
        return target.substring(0, target.length() - INDEX_POS);
    }

    private boolean isOperator(char operator) {
        char listOperator[] = {CHAR_ADD, CHAR_SUB, CHAR_MUL, CHAR_DIV};
        if (Arrays.binarySearch(listOperator, operator) >= 0) return true;
        return false;
    }

    private int priority(char operator) {
        if (operator == CHAR_ADD || operator == CHAR_SUB) return MEDIUM_PRIORITY;
        else if (operator == CHAR_MUL || operator == CHAR_DIV) return HIGH_PRIORITY;
        else return LOW_PRIORITY;
    }
}
