package com.lunch.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.jsoup.Jsoup
import java.util.*


var day_title = "";
var main_st = "";
class MainActivity : AppCompatActivity() {

    class UrlRun() : Runnable{
        fun num_find(str: String): Int {
            var result = 30;
            var num_ls = "123456789"
            for (i in num_ls) {
                var index = str.indexOf(i);
                if (index == -1) {
                    continue;
                }
                if (index < result) {
                    result = index;
                }
            }
            return result;
        }

        @Synchronized
        override fun run() {
            val instance = Calendar.getInstance();
            val year = instance.get(Calendar.YEAR).toString();
            var month = (instance.get(Calendar.MONTH) + 1).toString();
            if (month.toInt() < 10) {
                month = "0$month";
            }
            var date = instance.get(Calendar.DATE).toString();
            if (date.toInt() < 10) {
                date = "0$date";
            }
            var week_day = instance.get(Calendar.DAY_OF_WEEK) - 1;

            var real_date = "${year}.${month}.${date}";


            var doc =
                Jsoup.connect("https://stu.sen.go.kr/sts_sci_md01_001.do?schulCode=B100001369&schulCrseScCode=3&schulKndScCode=03&schYmd=$real_date")
                    .get();
            var tr = doc.select("tr");
            var td = tr[2].select("td");
            var site = td[week_day].toString();
            var strsi = "${site}";
            strsi = strsi.replace("<td class=\"textC\">", "");
            strsi = strsi.replace("<br>", "\n");
            strsi = strsi.replace("</td>", "");

            var arr = strsi.split("\n");
            var st = "";
            for (i in arr) {
                var find_index = num_find(i);
                var plus_st = "";
                if (find_index == 30) {
                    plus_st = i + "\n";
                } else {
                    plus_st = i.substring(0, num_find(i)) + "\n";
                }
                st += plus_st;
            }
            day_title = "${real_date} 동원중학교 급식";
            main_st = st;

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn:Button = findViewById(R.id.btn1)
        var day_text_box: TextView = findViewById(R.id.day);
        var main_text:TextView = findViewById(R.id.main_text);
        btn.setOnClickListener{
            var t = Thread(UrlRun())
            t.start()
            t.join()
            println(day_title);
            println(main_st);
            day_text_box.setTextColor(Color.BLACK);
            main_text.setTextColor(Color.BLACK);
            day_text_box.setText(day_title);
            main_text.setText(main_st);
        }
    }


}
