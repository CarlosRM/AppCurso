package com.example.carlos.appcurso.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlos.appcurso.Domain.FullCalculator;
import com.example.carlos.appcurso.R;

/**
 * Created by Carlos on 25/01/2017.
 */

public class Calculator extends Fragment implements View.OnClickListener{

    BaseActivity activity;
    SharedPreferences preferences;
    View v;
    FullCalculator calculator;
    int openParenthesisCounter;

    String actualExpression;
    String ans;
    String answerExpression;
    HorizontalScrollView horizontalScrollView;
    TextView expressionDisplay;
    TextView resultDisplay;
    Button zeroButton;
    Button oneButton;
    Button twoButton;
    Button threeButton;
    Button fourButton;
    Button fiveButton;
    Button sixButton;
    Button sevenButton;
    Button eightButton;
    Button nineButton;
    Button sumButton;
    Button subsButton;
    Button multiplyButton;
    Button divideButton;
    Button ansButton;
    Button acButton;
    Button deleteButton;
    Button equalButton;
    Button commaButton;
    Button parenthesisButton;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_calculator);
        v = inflater.inflate(R.layout.fragment_calculator,container,false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Calculator");
        initializeViews();

        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            expressionDisplay.setText(savedInstanceState.getString("displayText"));
            resultDisplay.setText(savedInstanceState.getString("resultText"));
            openParenthesisCounter = savedInstanceState.getInt("openedParenthesis");
            actualExpression = savedInstanceState.getString("actualExpression");
            ans = savedInstanceState.getString("ans");
            answerExpression = savedInstanceState.getString("answerExpression");
        }
        setListeners();
        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.restart_memory);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.call_button) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+expressionDisplay.getText().toString()));
            startActivity(intent);
        } else if(id == R.id.browser_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wolframalpha.com"));
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putString("displayText", expressionDisplay.getText().toString());
        outstate.putString("resultText",resultDisplay.getText().toString());
        outstate.putInt("openedParenthesis",openParenthesisCounter);
        outstate.putString("actualExpression",actualExpression);
        outstate.putString("ans",ans);
        outstate.putString("answerExpression",answerExpression);
    }

    public void initializeViews() {
        openParenthesisCounter = 0;
        calculator = new FullCalculator();
        actualExpression = "";
        ans = "";
        answerExpression = "";

        activity = (BaseActivity) getActivity();
        preferences = activity.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        expressionDisplay = (TextView) v.findViewById(R.id.expressionTextView);
        resultDisplay = (TextView) v.findViewById(R.id.resultTextView);
        horizontalScrollView = (HorizontalScrollView) v.findViewById(R.id.horizontalScrollView);
        zeroButton = (Button) v.findViewById(R.id.zeroButton);
        oneButton = (Button) v.findViewById(R.id.oneButton);
        twoButton = (Button) v.findViewById(R.id.twoButton);
        threeButton = (Button) v.findViewById(R.id.threeButton);
        fourButton = (Button) v.findViewById(R.id.fourButton);
        fiveButton = (Button) v.findViewById(R.id.fiveButton);
        sixButton = (Button) v.findViewById(R.id.sixButton);
        sevenButton = (Button) v.findViewById(R.id.sevenButton);
        eightButton = (Button) v.findViewById(R.id.eightButton);
        nineButton = (Button) v.findViewById(R.id.nineButton);
        commaButton = (Button) v.findViewById(R.id.commaButton);
        parenthesisButton = (Button) v.findViewById(R.id.parenthesisButton);
        equalButton = (Button) v.findViewById(R.id.equalButton);
        sumButton = (Button) v.findViewById(R.id.sumButton);
        subsButton = (Button) v.findViewById(R.id.subsButton);
        multiplyButton = (Button) v.findViewById(R.id.multiplyButton);
        divideButton = (Button) v.findViewById(R.id.divideButton);
        deleteButton = (Button) v.findViewById(R.id.deleteButton);
        acButton = (Button) v.findViewById(R.id.acButton);
        ansButton = (Button) v.findViewById(R.id.ansButton);

    }

    public void setListeners() {

        zeroButton.setOnClickListener(this);
        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);
        threeButton.setOnClickListener(this);
        fourButton.setOnClickListener(this);
        fiveButton.setOnClickListener(this);
        sixButton.setOnClickListener(this);
        sevenButton.setOnClickListener(this);
        eightButton.setOnClickListener(this);
        nineButton.setOnClickListener(this);
        sumButton.setOnClickListener(this);
        subsButton.setOnClickListener(this);
        multiplyButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
        ansButton.setOnClickListener(this);
        commaButton.setOnClickListener(this);
        parenthesisButton.setOnClickListener(this);
        equalButton.setOnClickListener(this);
        acButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        expressionDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                if(expressionDisplay.getText().toString().isEmpty()){
                    resultDisplay.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean valueSizeExceeded(){
        String aux = expressionDisplay.getText().toString();
        if(aux.length()<12) return false;
        else {
            for(int i = aux.length()-1;i>=aux.length()-12;--i) {
                if(aux.charAt(i) == '+' || aux.charAt(i) == '-' || aux.charAt(i) == '('
                        || aux.charAt(i) == ')' || aux.charAt(i) == '×'
                        || aux.charAt(i) == '÷') return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.zeroButton:
                digitClickLogic("0");
                break;
            case R.id.oneButton:
                digitClickLogic("1");
                break;
            case R.id.twoButton:
                digitClickLogic("2");
                break;
            case R.id.threeButton:
                digitClickLogic("3");
                break;
            case R.id.fourButton:
                digitClickLogic("4");
                break;
            case R.id.fiveButton:
                digitClickLogic("5");
                break;
            case R.id.sixButton:
                digitClickLogic("6");
                break;
            case R.id.sevenButton:
                digitClickLogic("7");
                break;
            case R.id.eightButton:
                digitClickLogic("8");
                break;
            case R.id.nineButton:
                digitClickLogic("9");
                break;
            case R.id.sumButton:
                expressionDisplay.setText(expressionDisplay.getText().toString() + "+");
                actualExpression = actualExpression + " + ";
                break;
            case R.id.subsButton:
                String auxSubs = expressionDisplay.getText().toString();
                if(auxSubs.isEmpty()) {
                    actualExpression = actualExpression + "0 - ";
                    expressionDisplay.setText(expressionDisplay.getText().toString() + "-");
                } else {
                    char auxCharSubs = auxSubs.charAt(auxSubs.length() - 1);
                    if (!(auxCharSubs >= '0' && auxCharSubs <= '9') && !(auxCharSubs == ')')) {
                        if(auxCharSubs == '('){
                            actualExpression = actualExpression + "0 - ";
                            expressionDisplay.setText(expressionDisplay.getText().toString() + "-");
                        } else {
                            actualExpression = actualExpression + " ( 0 - ";
                            expressionDisplay.setText(expressionDisplay.getText().toString() + "(-");
                            ++openParenthesisCounter;
                        }
                    } else {
                        actualExpression = actualExpression + " - ";
                        expressionDisplay.setText(expressionDisplay.getText().toString() + "-");
                    }
                }
                break;
            case R.id.multiplyButton:
                expressionDisplay.setText(expressionDisplay.getText().toString() + "×");
                actualExpression = actualExpression + " × ";
                break;
            case R.id.divideButton:
                expressionDisplay.setText(expressionDisplay.getText().toString() + "÷");
                actualExpression = actualExpression + " ÷ ";
                break;
            case R.id.ansButton:
                String properAns = expressionDisplay.getText().toString();
                if(properAns.isEmpty()){
                    expressionDisplay.setText(expressionDisplay.getText().toString() + "Ans");
                    actualExpression = actualExpression + "*";
                } else {
                    char properAnsChar = properAns.charAt(properAns.length()-1);
                    if((properAnsChar>='0' && properAnsChar<='9') || properAnsChar == ')' || properAnsChar == 's'){
                        expressionDisplay.setText(expressionDisplay.getText().toString() + "×Ans");
                        actualExpression = actualExpression + " × *";
                    }else{
                        expressionDisplay.setText(expressionDisplay.getText().toString() + "Ans");
                        actualExpression = actualExpression + "*";
                    }
                }
                break;
            case R.id.commaButton:
                expressionDisplay.setText(expressionDisplay.getText().toString() + ",");
                actualExpression = actualExpression + ".";
                break;
            case R.id.acButton:
                expressionDisplay.setText("");
                resultDisplay.setText("");
                actualExpression = "";
                openParenthesisCounter = 0;
                break;
            case R.id.deleteButton:
                boolean isAns = false;
                String aux = expressionDisplay.getText().toString();
                if(!aux.isEmpty()) {
                    char auxChar = aux.charAt(aux.length()-1);
                    if(auxChar == '-') {
                        String secondAux = aux.substring(0,aux.length()-1);
                        if(secondAux.isEmpty())actualExpression = actualExpression.substring(0,actualExpression.length()-4);
                        else {
                            char secondAuxChar = aux.charAt(aux.length()-2);
                            if((secondAuxChar>='0' && secondAuxChar<='9')||secondAuxChar == ')') {
                                actualExpression = actualExpression.substring(0,actualExpression.length()-3);
                            } else actualExpression = actualExpression.substring(0,actualExpression.length()-4);
                        }
                    }
                    else if (auxChar == 's'){
                        //actualExpression = actualExpression.substring(0,actualExpression.length() - ans.length() );
                        isAns = true;
                        actualExpression = actualExpression.substring(0,actualExpression.length()-1);
                    }
                    else if(auxChar == '+' || auxChar == ')' ||
                            auxChar == '(' || auxChar == ')' || auxChar == '×' || auxChar == '÷'){
                        actualExpression = actualExpression.substring(0,actualExpression.length()-3);
                        Log.v("ACTUALEXPRESSION",actualExpression);
                    } else {
                        actualExpression = actualExpression.substring(0,actualExpression.length()-1);
                    }
                    deleteLogic();
                    if(!isAns) aux = aux.substring(0, aux.length() - 1);
                    else aux = aux.substring(0,aux.length() - 3);
                    expressionDisplay.setText(aux);
                }
                break;
            case R.id.parenthesisButton:
                String parenthesis = properParenthesis();
                expressionDisplay.setText(expressionDisplay.getText().toString() + parenthesis);
                if(parenthesis.equals("×(")){
                    actualExpression = actualExpression + " ×  ( ";
                } else actualExpression = actualExpression + " " + parenthesis + " ";
                break;
            case R.id.equalButton:
                if(!actualExpression.isEmpty()) {
                    answerExpression = actualExpression.replaceAll("\\*",ans);
                    String result = calculator.processInput(answerExpression.trim());
                    resultTreatment(result);
                }
                break;
        }
    }

    private void resultTreatment(String result) {
        if (result.equals("Wrong expression")) {
            if(openParenthesisCounter > 0) result = "Wrong use of parentheses";
            if(preferences.getBoolean("toast",false)){
                Toast.makeText(activity,result,Toast.LENGTH_SHORT).show();
            }
            if(preferences.getBoolean("status",false)){
                Intent notificationIntent = new Intent(getActivity(), BaseActivity.class);
                notificationIntent.putExtra("fragmentToOpen","Calculator");
                notificationIntent.putExtra("username",((BaseActivity) getActivity()).getCurrentUser());
                //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //registerReceiver(receiver, new IntentFilter("NOTIFICATION_DELETED"));
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
                Notification notification = new Notification.Builder(getActivity().getApplication().getApplicationContext())
                        .setContentTitle("Error")
                        .setContentText(result)
                        .setSmallIcon(R.drawable.ic_song_notification)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(1337, notification);
            }
            resultDisplay.setText("");
            calculator = new FullCalculator();
        } else if(result.equals("Infinity") || result.equals("NaN")) {
            if(preferences.getBoolean("toast",false)){
                Toast.makeText(activity,"ERROR: Division by zero",Toast.LENGTH_SHORT).show();
            }
            if(preferences.getBoolean("status",false)){
                Intent notificationIntent = new Intent(getActivity(), BaseActivity.class);
                notificationIntent.putExtra("fragmentToOpen","Calculator");
                notificationIntent.putExtra("username",((BaseActivity) getActivity()).getCurrentUser());

                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
                Notification notification = new Notification.Builder(getActivity().getApplication().getApplicationContext())
                        .setContentTitle("Error")
                        .setContentText("Division by zero")
                        .setSmallIcon(R.drawable.ic_song_notification)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(1337, notification);
            }
            resultDisplay.setText("");
            calculator = new FullCalculator();
        } else {
            String replaced = result.replaceAll("\\.",",");
            Log.d("RESULT2",replaced);
            resultDisplay.setText("= " + replaced);
            ans = result;
            Log.d("ANS",ans);
        }
    }

    private void digitClickLogic(String digit) {
        if(valueSizeExceeded()){
            if(preferences.getBoolean("toast",false)){
                Toast.makeText(activity,"Exceeded maximum value length (12)",Toast.LENGTH_SHORT).show();
            }
            if(preferences.getBoolean("status",false)){
                Intent notificationIntent = new Intent(getActivity(), BaseActivity.class);
                notificationIntent.putExtra("fragmentToOpen","Calculator");
                notificationIntent.putExtra("username",((BaseActivity) getActivity()).getCurrentUser());
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
                Notification notification = new Notification.Builder(getActivity().getApplication().getApplicationContext())
                        .setContentTitle("Error")
                        .setContentText("Exceeded maximum value length (12)")
                        .setSmallIcon(R.drawable.ic_song_notification)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1337, notification);
            }
        } else {
            String properAns = expressionDisplay.getText().toString();
            if(properAns.isEmpty()){
                expressionDisplay.setText(expressionDisplay.getText().toString() + digit);
                actualExpression = actualExpression + digit;
            } else {
                char properAnsChar = properAns.charAt(properAns.length()-1);
                if(properAnsChar == 's'){
                    expressionDisplay.setText(expressionDisplay.getText().toString() + "×" + digit);
                    actualExpression = actualExpression + " × " + digit;
                } else {
                    expressionDisplay.setText(expressionDisplay.getText().toString() + digit);
                    actualExpression = actualExpression + digit;
                }
            }
        }
    }

    private String properParenthesis () {
        String auxParenthesis = expressionDisplay.getText().toString();
        String concatenationValue = new String();
        if(auxParenthesis.isEmpty()) {
            concatenationValue = "(";
            ++openParenthesisCounter;
        }
        else {
            char auxChar = auxParenthesis.charAt(auxParenthesis.length() - 1);
            if (auxChar == '(') {
                concatenationValue = "(";
                openParenthesisCounter++;
            } else if ((auxChar >= '0' && auxChar <= '9') || auxChar == 's' || auxChar == ')') {
                if (openParenthesisCounter == 0) {
                    concatenationValue = "×(";
                    ++openParenthesisCounter;
                } else {
                    concatenationValue = ")";
                    --openParenthesisCounter;
                }
            } else if (auxChar == '+' || auxChar == '-' || auxChar == '×' || auxChar == '÷') {
                concatenationValue = "(";
                ++openParenthesisCounter;
            }
        }
        return concatenationValue;
    }

    private void deleteLogic () {
        String expression = expressionDisplay.getText().toString();
        char aux = expression.charAt(expression.length()-1);
        if(aux == ')') ++openParenthesisCounter;
        else if (aux == '(') --openParenthesisCounter;
    }

}

