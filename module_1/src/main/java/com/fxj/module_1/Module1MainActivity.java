package com.fxj.module_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fxj.simpleRouterAnnotation.Router;
import com.fxj.simplerouter.SimpleRouter;

@Router(path="module1/main")
public class Module1MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_1_main);
        findViewById(R.id.module1_btn01).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.module1_btn01){
            SimpleRouter.getInstance().navigation(this,"module2/main",null);
        }

    }
}
