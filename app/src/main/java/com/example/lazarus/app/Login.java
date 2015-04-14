package com.example.lazarus.app;

        import android.content.DialogInterface;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.content.Intent;
        import android.widget.Button;


public class Login extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void clickFunction(View v)
    {
        Intent intent = new Intent(this, SignUp.class);
        Intent intent_user = new Intent(this, UserMenu.class);

        switch(v.getId()){
            case R.id.login_button:
                startActivity(intent_user);
                break;
            case R.id.link_to_register:
                startActivity(intent);
                break;
        }
    }
}
