package com.example.login1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginPage extends AppCompatActivity {
    EditText emaillogin,passwordlogin;
    Button loginbtn,regbtn;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emaillogin = (EditText)findViewById(R.id.emaillogin);
        passwordlogin = (EditText)findViewById(R.id.passwordlogin);
        loginbtn = (Button)findViewById(R.id.loginbtn);
        regbtn = (Button)findViewById(R.id.regbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Login().execute();
            }
        });
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    class Login extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Connector x=new Connector();
            con=connector(x.getIp(),x.getUn(),x.getPass(),x.getDb(),x.getPort());
            if (con==null){
                Toast.makeText(LoginPage.this,"Check connection",Toast.LENGTH_LONG).show();
            }
            else{
                try{
                    String sql = "SELECT * FROM register WHERE email = '" + emaillogin.getText() + "' AND password = '" + passwordlogin.getText() + "' ";
                    Statement statement = con.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    if (resultSet.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginPage.this,"Login success",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginPage.this,"Login failed",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception e) {
                    Log.e("SQL error: ",e.getMessage());
                }
            }
            return null;
        }

    }

    protected Connection connector(String server, String user, String pass, String db, String port){
        Connection connection=null;
        String connectionURL= "jdbc:mysql://" + server + ":"+ port +"/" + db;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL, user, pass);
        } catch (Exception e) {
            Log.e("SQL Connection error: ",e.getMessage());
        }
        return connection;
    }
}