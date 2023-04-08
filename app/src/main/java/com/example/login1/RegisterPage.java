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

public class RegisterPage extends AppCompatActivity{
    EditText name,email,password;
    Button registerbtn;
    TextView status;
    Connection con;
    Statement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        registerbtn = (Button)findViewById(R.id.registerbtn);
        status = (TextView)findViewById(R.id.status);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Register().execute();
            }
        });
    }

    class Register extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Connector x=new Connector();
            con=connector(x.getIp(),x.getUn(),x.getPass(),x.getDb(),x.getPort());
            if (con==null){
                Toast.makeText(RegisterPage.this,"Check connection",Toast.LENGTH_LONG).show();
            }
            else{
                try{
                    statement = con.createStatement();
                    String sql = "SELECT email FROM register WHERE email = '" + email.getText() + "'";
                    ResultSet resultSet = statement.executeQuery(sql);

                    if (resultSet.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterPage.this,"Account with email already exists",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        sql = "INSERT INTO register (name,email,password) VALUES ('"+name.getText()+"','"+email.getText()+"','"+password.getText()+"')";
                        statement.executeUpdate(sql);
                        Intent intent = new Intent(RegisterPage.this,LoginPage.class);
                        startActivity(intent);
                        finish();
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
