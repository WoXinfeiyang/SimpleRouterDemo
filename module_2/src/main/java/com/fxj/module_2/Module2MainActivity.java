package com.fxj.module_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.fxj.simpleRouterAnnotation.Router;
import com.fxj.simplerouter.SimpleRouter;

@Router(path="module2/main")
public class Module2MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_2_main);
        findViewById(R.id.module2_btn01).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.module2_btn01){
            SimpleRouter.getInstance().navigation("MainActivity",null);
        }
    }
}
