package com.example.todo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Load();
    }

    private void Load()
    {
        // Получение экземпляра SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        // Получение всех ключей из SharedPreferences
        Map<String, ?> allData = sharedPreferences.getAll();
        LinearLayout parentLayout = findViewById(R.id.L1);
        // Перебор всех ключей и значений
        for (Map.Entry<String, ?> entry : allData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            View newFrameLayout = LayoutInflater.from(this).inflate(
                    R.layout.custom_item, parentLayout, false);
            newFrameLayout.setId(Integer.valueOf(key));
            parentLayout.addView(newFrameLayout);

            // Находим EditText внутри FrameLayout
            EditText editText = newFrameLayout.findViewById(R.id.newEditText);
            editText.setText(String.valueOf(value));
            editText.setId(View.generateViewId());
            Notes.put( newFrameLayout.getId(), editText.getId());
        }
    }
    private FrameLayout frameLayout;
    private HashMap<Integer,Integer> Notes = new HashMap<Integer,Integer>();
    public void onButtonClick(View view)
    {
        LinearLayout parentLayout = findViewById(R.id.L1);
        View newFrameLayout = LayoutInflater.from(this).inflate(
                R.layout.custom_item, parentLayout, false);
        newFrameLayout.setId(View.generateViewId());

        parentLayout.addView(newFrameLayout);
        // Находим EditText внутри FrameLayout
        EditText editText = newFrameLayout.findViewById(R.id.newEditText);
        editText.setId(View.generateViewId());
        Notes.put( newFrameLayout.getId(), editText.getId());
    }

    public void onButtonClick2(View view)
    {
        // Получение экземпляра SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();
        editor2.clear(); // Очищаем SharedPreferences
        editor2.apply(); // Применяем изменения
        for ( Integer key : Notes.keySet() ) {
            String id = String.valueOf(key);
            Integer id2= Notes.get(key);
            EditText editText = findViewById(id2) ;
            // Сохранение id в SharedPreferences


            editor2.putString(id, editText.getText().toString());

        }
        editor2.apply();
    }

    public void clickCheckBox(View view) {
        // Получаем ссылку на чекбокс
        CheckBox checkBox = (CheckBox) view;

        // Получаем родительский контейнер чекбокса (это FrameLayout)
        FrameLayout parentLayout = (FrameLayout) checkBox.getParent().getParent();
        SharedPreferences sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        // Получаем идентификатор родительского контейнера
        int parentId = parentLayout.getId();

        if (Notes.containsKey(parentId)) {
            // Удаляем FrameLayout из родительского контейнера
            parentLayout.removeView((ViewGroup) checkBox.getParent());
            // Удаляем элемент из HashMap
            Notes.remove(parentId);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(String.valueOf(parentId));
            editor.apply();
        }
    }

    public  void clickEditTxt(View view)
    {
        EditText editText = (EditText) view;
        editText.setText("");
    }

    public void WriteEditTxt()
    {
        // Находим FrameLayout по его ID
        FrameLayout frameLayout = findViewById(R.id.newFrameLayout);
        // Находим LinearLayout внутри FrameLayout
        LinearLayout linearLayout = (LinearLayout) frameLayout.getChildAt(0);
        // Находим ScrollView внутри LinearLayout
        ScrollView scrollView = (ScrollView) linearLayout.getChildAt(0);
        // Находим EditText внутри ScrollView
        EditText editText = (EditText) scrollView.getChildAt(0);
    }

}