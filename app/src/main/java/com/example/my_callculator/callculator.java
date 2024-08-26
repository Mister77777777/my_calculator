package com.example.my_callculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Stack;
import java.util.stream.Stream;
import java.util.stream.Collectors;


public class callculator extends AppCompatActivity {
    private TextView tv_result;
    private TextView tv_formula;
    private String formula="";   /*用于存储输入的字符串*/
    private String result="";

    private Stack<Double> value=new Stack<>(); /*操作数栈*/
    private Stack<String> Operator = new Stack<>(); /*运算符栈*/
    private boolean is_result = false;  /*用于标记是否完成计算*/



//    private String firstNum = "";    /*第一个操作数*/
//    private String secondNum = "";   /*第二个操作数*/
//    private String operator = "";    /*运算符*/
//    private String showText = "";
//    private String result = "";
//    private String formula="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_callculator);

        tv_result = findViewById(R.id.tv_result);
        tv_formula = findViewById(R.id.formula);
        findViewById(R.id.delete).setOnClickListener(this::onClick);
        findViewById(R.id.divide).setOnClickListener(this::onClick);
        findViewById(R.id.multiply).setOnClickListener(this::onClick);
        findViewById(R.id.clear).setOnClickListener(this::onClick);
        findViewById(R.id.seven).setOnClickListener(this::onClick);
        findViewById(R.id.eight).setOnClickListener(this::onClick);
        findViewById(R.id.nine).setOnClickListener(this::onClick);
        findViewById(R.id.substract).setOnClickListener(this::onClick);
        findViewById(R.id.four).setOnClickListener(this::onClick);
        findViewById(R.id.five).setOnClickListener(this::onClick);
        findViewById(R.id.six).setOnClickListener(this::onClick);
        findViewById(R.id.delete).setOnClickListener(this::onClick);
        findViewById(R.id.one).setOnClickListener(this::onClick);
        findViewById(R.id.two).setOnClickListener(this::onClick);
        findViewById(R.id.three).setOnClickListener(this::onClick);
        findViewById(R.id.plus).setOnClickListener(this::onClick);
        findViewById(R.id.left_bucket).setOnClickListener(this::onClick);
        findViewById(R.id.right_bucket).setOnClickListener(this::onClick);
        findViewById(R.id.zero).setOnClickListener(this::onClick);
        findViewById(R.id.point).setOnClickListener(this::onClick);
        findViewById(R.id.result).setOnClickListener(this::onClick);
        /*启动对所有按钮的监听*/
    }

    public void onClick(View v) {
        String input;
        input = ((TextView) v).getText().toString();
        if(is_result) {
            formula = result;
            is_result=false;
        }
        if(v.getId()==R.id.clear){
            if(!formula.equals(""))
                formula="";
            else
                result="";
        }else if(v.getId()==R.id.delete){ /*将最后一个字符删除*/
            if (!formula.isEmpty()) {
                String tmp=formula.substring(0,formula.length()-1);
                formula=tmp;
            }
        }else if(v.getId()==R.id.result){
            try{
                formula+="=";
                double x=calResult();
                result=String.valueOf(x);
                is_result=true;
            }catch(Exception ex){
                if(ex.getMessage()==null){
                    result="计算错误！";
                }else{
                    result=ex.getMessage();
                }
            }
        }else{
            formula+=input;
        }
        refreshText();

    }
    private  void refreshText()   /*打印当前结果*/
    {
        tv_formula.setText(formula);
        tv_result.setText(result);
    }

    private void calculate(){  /*处理四则运算*/
        String operator = Operator.pop();
        double secondnum = value.pop();
        double firstnum = value.pop();
        double new_result;

        /*处理基本运算*/

        if(operator.equals("+")){
            new_result=firstnum+secondnum;
            value.push(new_result);
        }else if(operator.equals("-")){
            new_result=firstnum-secondnum;
            value.push(new_result);
        }else if(operator.equals("x")){
            new_result=firstnum*secondnum;
            value.push(new_result);
        }else if(operator.equals("÷")){
            if(secondnum==0)
                throw new ArithmeticException("发生除0错误！");
            new_result=firstnum/secondnum;
            value.push(new_result);
        }
    }

    private Double calResult() {
        HashMap<String, Integer> operator = new HashMap<>();
        operator.put("(", 0);
        operator.put(")", 0);
        operator.put("÷", 2);
        operator.put("x", 2);
        operator.put("-", 1);
        operator.put("+", 1);
        operator.put("=", 0);
        /*建立哈希映射，为每个操作符给予对应的运算级*/
        Operator.push("=");

        int flag = 0;
        int n = formula.length();
        for (int i = 0; i < n; i++) {
            String a = String.valueOf(formula.charAt(i));
            if (!a.matches("[0-9.]")) {
                if (flag != i) {
                    value.push(Double.parseDouble(formula.substring(flag, i)));
                }
                flag = i+1;
                while(!(a.equals("=")&&Operator.peek().equals("="))){
                    if(operator.get(a)>operator.get(Operator.peek())||a.equals("(")){
                        Operator.push(a);  /*将当前操作符压入栈中*/
                        break;
                    }else{
                        if(a.equals(")")){
                            while(!Operator.peek().equals("("))
                                calculate();
                            Operator.pop();
                            break;
                        }
                        calculate();
                    }
                }
            }
        }
        return value.pop(); /*操作数栈中的最后一个值就是计算答案*/
    }

}