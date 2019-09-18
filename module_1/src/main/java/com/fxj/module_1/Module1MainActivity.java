package com.fxj.module_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fxj.simpleRouterAnnotation.Router;

@Router(path="module1/main")
public class Module1MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_1_main);
    }
}
