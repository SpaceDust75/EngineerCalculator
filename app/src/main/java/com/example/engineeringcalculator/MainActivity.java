package com.example.engineeringcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.lang.Math;      // 이거 이미 lang에 있다는데 그럼 굳이 import 안해도 괜찮은건가?

import com.example.engineeringcalculator.databinding.ActivityMainBinding;

import androidx.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    boolean isFirstInput = true;
    boolean isOperatorClick = false;
    double ResultNumber = 0;
    double InputNumber = 0;     //InputNumber를 전역변수로 지정해 주는 이유는 아래 clear, equal click 부분에서 초기화되지 않도록!
    String Operator = "=";      //case문에 =을 넣어주었기 때문에 처음에 +로 초기값을 설정해준걸 =로 변경
    String LastOperator = "＋";      //이거 +기호가 키보드에 있는 +인지 유니코드 기호인지 구분 잘해야함. 이왕이면 밑에서 사용한 기호들 복붙해서 사용하는게 좋음
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = activityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());//activitymainbinding 찾아보기
        //setContentView(R.layout.activity_main);

        //Mode변경 버튼 클릭 시 다른 Activity로 이동
        Button ConvertMode2BT = (Button) findViewById(R.id.ConvertMode2BT);
        ConvertMode2BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FreeBuildActivity.class);
                startActivity(intent);
            }
        });
    }

    // 추가 버튼 함수 부분
    // Log, 루트, x의 y제곱, n! (총 4개)
    public void LogButtonClick(View view){      // 자연로그 값 출력(상용로그 x, 상용로그 출력하고 싶다면 자연로그 * 0.434294482)
        InputNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());
        activityMainBinding.MathExpressionTextView.setText("Log" + InputNumber);
        ResultNumber = Math.log(InputNumber);
        activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
    }

    // x의 y제곱은 Operator 클릭으로 넘겼음(case에서 분류 후 처리)
    public void PowButtonClick(View view){

    }

    public void SqrtButtonClick(View view){
        InputNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());
        activityMainBinding.MathExpressionTextView.setText(InputNumber + "의 제곱근");
        ResultNumber = Math.sqrt(InputNumber);
        activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
    }

    public void FactorialButtonClick(View view){
        ResultNumber = 1.0;
        InputNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());
        for(double i = 1.0; i <= InputNumber; i += 1.0){
            ResultNumber = ResultNumber * i;      // fac *= i;
        }
        activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
    }

    //모든 버튼에 대하여 입출력을 지정해주면 코드가 매우 길어짐. 따라서 현재 눌린 버튼에 대하여 그 값을 받아오고 출력함
    public void NumButtonClick(View view){
        if(isFirstInput){
            activityMainBinding.ResultTextView.setText(view.getTag().toString());
            isFirstInput = false;
            if(Operator.equals("=")){
                activityMainBinding.MathExpressionTextView.setText("");     //결국 아무것도 입력을 안하기 위해 ""을 사용한 것이므로 괄호안에 null을 사용해도 상관 없다.
                isOperatorClick = false;
            }
        }else {
            //01을 입력하면 그냥 1이 출력하게끔 하고 싶은데..? 방법 한 번 찾아보자!
            if(activityMainBinding.ResultTextView.getText().toString().equals("0")){
                //Toast 적을 때 new create로 만들어야 context랑 text가 오류가 안남
                Toast.makeText(this, "0으로 시작되는 숫자는 없습니다.", Toast.LENGTH_SHORT).show();
                isFirstInput = true;
            }else {
                activityMainBinding.ResultTextView.append(view.getTag().toString());
            }
        }
    }

    public void ClearButtonClick(View view){
        activityMainBinding.ResultTextView.setText("0");
        activityMainBinding.MathExpressionTextView.setText("");
        ResultNumber = 0;
        Operator = "=";
        //여기서 isFirstInput을 왜 true로 해야하는지 다시 고민하기
        //각 버튼에서 isFirstInput에 대하여 계속 false로 설정해주는 것과 연관있음
        isFirstInput = true;
        isOperatorClick = false;        //이걸 false로 초기화 안해주면 10만 입력 후 =를 누르면 계속 연산됨. 숫자 하나 입력시 =을 누르면 값이 그대로여야만 한다.
    }

    public void PointsButtonClick(View view){
        if(isFirstInput){
            activityMainBinding.ResultTextView.setText("0" + view.getTag().toString());
            isFirstInput = false;
        }else {
            if(activityMainBinding.ResultTextView.getText().toString().contains(".")){
                //Toast 기능에 대하여 다시 찾아보기
                //Toast.makeText(this, "이미 소수점이 존재합니다.", Toast.LENGTH_SHORT).show();
            }else{
                activityMainBinding.ResultTextView.append(view.getTag().toString());
            }
        }
    }

    public void OperatorClick(View view){
        //isFirstInput이 true일 때 예외처리를 시켜주기 위함
        //if안에 들어가있는 부분은 계산 기호를 다른 기호로 바꾸고 싶을 때 이미 하나의 기호를 누른 상태에서 다른 기호를 누르면 바뀌게 해주는 기능
        isOperatorClick = true;
        LastOperator = view.getTag().toString();        //equal 쪽에서 else문 안에 이걸 두면 lastop가 변경되지 않기 때문에 이곳으로 끌고 와서 설정해줌

        if(isFirstInput){
            if(Operator.equals("=")){
                Operator = view.getTag().toString();
                //ResultNumber가 double형이라서 앞에서 저렇게 묶어주는 것
                ResultNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());
                activityMainBinding.MathExpressionTextView.setText(ResultNumber + " " + Operator + " ");
            }else{
                Operator = view.getTag().toString();
                String getMathText = activityMainBinding.MathExpressionTextView.getText().toString();
                String subString = getMathText.substring(0, getMathText.length() - 2);
                activityMainBinding.MathExpressionTextView.setText(subString);
                activityMainBinding.MathExpressionTextView.append(Operator + " ");
            }
        }else{
            //InputNumber를 전역변수로 설정해 주었기 때문에 여기서는 double 선언 생략
            isOperatorClick = true;
            InputNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());

            ArithmeticOperation(ResultNumber, InputNumber, Operator);

            ResultNumber = ArithmeticOperation(ResultNumber, InputNumber, Operator);

            activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
            isFirstInput = true;
            Operator = view.getTag().toString();
            activityMainBinding.MathExpressionTextView.append(InputNumber + " " + Operator + " ");
            //위에 NumButtonClick 부분에서 MathExpressionTextView와 관련된 코드 2줄을 지워줘야한다.
            //그러지 않으면 표현식 TextView에서 이상한 값이 출력된다.
        }
    }

    public void BackSpaceButtonClick(View view){
        if(isFirstInput == false){      //isFirstInput == false랑 !isFirstInput이랑 같은 뜻
            String getResultText = activityMainBinding.ResultTextView.getText().toString();
            //밑에 if else문은 0일 때 백스페이스 버튼을 눌렀을 때 0까지 지워지지 않게 해주기 위함이다.
            if(getResultText.length() > 1){
                String subString = getResultText.substring(0, getResultText.length() - 1);
                activityMainBinding.ResultTextView.setText(subString);
            }else{
                activityMainBinding.ResultTextView.setText("0");
                isFirstInput = true;
            }
        }
    }

    //ArithmeticOperation의 경우 기능을 메소드로 만들어서 다른 곳에서 사용할 수 있게 만들어 준 것(OperatorClick과 EqualButtonClick에서 사용됨)
    //ArithmeticOperation = 사칙연산, 이 경우 다른 간단한 것으로 바꿔도 괜찮을 듯?? 나는 그냥 직관적인게 좋아서 뜻 찾아서 저렇게 이름 해놓은거
    private double ArithmeticOperation(double ResultNumber, double InputNumber, String Operator) {
        switch (Operator){
            case "=":
                ResultNumber = InputNumber;
                break;
            case "＋":
                ResultNumber = ResultNumber + InputNumber;
                break;
            case "－":
                ResultNumber = ResultNumber - InputNumber;
                break;
            case "×":
                ResultNumber = ResultNumber * InputNumber;
                break;
            case "÷":
                ResultNumber = ResultNumber / InputNumber;
                break;
            case "%":
                ResultNumber = ResultNumber % InputNumber;
                break;
            case "^":
                ResultNumber = Math.pow(ResultNumber, InputNumber);
                break;
            /* 이 부분은 디버깅할 때 어디서 오류났는지 확인하기 위함. 만약 디버깅 때 로그가 찍힌다면 ArithmeticOperation에서 사용된 무언가에 문제가 있는 것
            default:
                Log.e("ArithmeticOperation",ResultNumber + " " + InputNumber + " " + Operator);
                break;
             */
        }
        return ResultNumber;//double로 반환한다고 하였는데 return을 안해주면 오류 발생함
    }

    public void EqualButtonClick(View view){
        //if문을 이용하여 LastOperator를 중복 사용하게 해줌
        //즉 =을 두번 누르면 그의 배수의 값이 더해져서 결과가 나옴
        //이건 isFirstInput, 즉 참인 경우에 실행됨(이게 맞나..?)
        if(isFirstInput){
            //만약 처음 입력한 숫자가 출력된 상태에서 =을 두번 이상 누르는 경우 MathExpressionTextView 부분에 중복에서 출력되는걸 막기 위한 if문과 isOperator
            if(isOperatorClick) {  //해석 : 만약 OperatorClick이 true라면
                //if문 안에 코드는 Operator가 클릭되었을 때 동작이 되어야 하는 코드
                activityMainBinding.MathExpressionTextView.setText(ResultNumber + " " + LastOperator + " " + InputNumber + " = ");
                ResultNumber = ArithmeticOperation(ResultNumber, InputNumber, LastOperator);
                activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
            }
        }else {
            InputNumber = Double.parseDouble(activityMainBinding.ResultTextView.getText().toString());

            //ArithmeticOperation에서 괄호 안에 값들(파라미터)을 입력해주지 않으면 오류가 발생한다.
            ArithmeticOperation(ResultNumber, InputNumber, Operator);

            ResultNumber = ArithmeticOperation(ResultNumber, InputNumber, Operator);
            activityMainBinding.ResultTextView.setText(String.valueOf(ResultNumber));
            isFirstInput = true;
            Operator = view.getTag().toString();
            activityMainBinding.MathExpressionTextView.append(InputNumber + " " + Operator + " ");
        }
    }
}