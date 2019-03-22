package virgil.dailycost;

import android.content.Context;
import android.graphics.YuvImage;
import android.util.Log;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.PortUnreachableException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.helper.ListSortting;
import virgil.dailycost.models.MonthDay;
import virgil.dailycost.models.MonthYear;
import virgil.dailycost.page.MonthList;

import static java.util.Collections.sort;
import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest implements Cloneable{
    @Test
    public void addition_isCorrect(){

        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }).start();
                    }
                }).subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        compositeDisposable.add(Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                System.out.println("3");
                            }
                        }).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                System.out.println("2");
                            }
                        }));
                    }
                })
        );

        Float bookValue = 30000f;
        Float index = 5.6f;
        for (int i = 1 ; i <= 30 ; i++){
            index = index - 300f;
            bookValue = bookValue * (1f + (300f) / (index + 300f));
            System.out.println(i + ", " + index + ", " + bookValue);
        }

    }
}

