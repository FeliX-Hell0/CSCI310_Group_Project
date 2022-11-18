package com.example.csci310_group_project.data.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

public class db {
    static int exist = 0;

    public db(){

    }

    public void clear(){
        exist = 0;
    }


    class getPassword implements Callable{

        private String threadName;
        private String username;
        private String password;
        private int choice;
        Semaphore sem;

        getPassword(Semaphore sem, String name, String username, String password, int choice){
            this.threadName = name;
            this.username = username;
            this.password = password;
            this.choice = choice;
            this.sem = sem;
        }

        @Override
        public synchronized Object call() throws Exception{
            if(choice == 1) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                sem.acquire();
                Log.d("LoginCheck", String.valueOf(exist));
                DocumentReference docRef = db.collection("users").document(username);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if (document.getString("password").equals(password)) {
                                        exist = 1;
                                    } else {
                                        exist = 4;
                                    }
                                } else {
                                    exist = 2;
                                }
                            } else {
                                exist = 3;
                            }
                            Log.d("LoginCheck2", String.valueOf(exist));
                            sem.release();
                    }
                });

            }
            else if(choice == 2){
                for(long i = 0; i < 1000000000; i++){
                    i = i * i;
                }
            }
            else if(choice == 3){
                    sem.acquire();
                    Log.d("LoginCheck5", String.valueOf(exist));
                    int existResult = exist;
                    sem.release();

                    for(long i = 0; i < 1000; i++){
                        existResult = exist;
                    }

                    if (existResult == 2) {
                        throw new Exception("User not exists!");
                    } else if (existResult == 3) {
                        throw new Exception("Database connection error");
                    } else if (existResult == 0) {
                        throw new Exception("Database connection error #0");
                    } else if (existResult == 4) {
                        throw new Exception("password error");
                    } else if (existResult == 1) {
                        return true;
                    }
                }

            return null;
        }


    }

    public int loginUser(String username, String password)throws Exception{
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*
        Semaphore sem = new Semaphore(1);
        Map<String, Object> user = new HashMap<>();
        user.put("username", "abc@abc.com");
        user.put("password", "123456");
        CollectionReference users = db.collection("users");
        users.document("abc@abc.com").set(user);

        getPassword myThread = new getPassword(sem,"password",username,password, 1);
        getPassword delay = new getPassword(sem,"password",username,password, 1);
        getPassword exceptions = new getPassword(sem,"password",username,password, 3);

        FutureTask[] randomNumberTasks = new FutureTask[3];
        randomNumberTasks[0] = new FutureTask(myThread);
        Thread t = new Thread(randomNumberTasks[0]);
        t.start();

        randomNumberTasks[1] = new FutureTask(delay);
        Thread t1 = new Thread(randomNumberTasks[1]);
        t1.start();

        randomNumberTasks[2] = new FutureTask(exceptions);
        Thread t2 = new Thread(randomNumberTasks[2]);
        t2.start();

        try {
            for (int i = 0; i < 3; i++) {
                // As it implements Future, we can call get()
                String temp = (String) randomNumberTasks[i].get();
            }
        } catch (ExecutionException e) {
            throw new Exception(e.getMessage());
        } */

        //checkLoginUser(username, password);
        //TimeUnit.SECONDS.sleep(3);
        /*
        Log.d("LoginCheck4", String.valueOf(exist[0]));
        int existResult = exist[0];

        if(existResult == 2){
            throw new Exception("User not exists!");
        }
        else if(existResult == 3){
            throw new Exception("Database connection error");
        }
        else if(existResult == 0){
            throw new Exception("Database connection error #0");
        }
        else if(existResult == 4){
            throw new Exception("password error");
        }
        else if(existResult == 1){
            return true;
        }*/

        return 0;

    }

    private void checkLoginUser(String username, String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();


            DocumentReference docRef = db.collection("users").document(username);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    int myInt = 0;
                    Log.d("LoginCheck", String.valueOf(myInt));
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if (document.getString("password").equals(password)) {
                                myInt = 1;
                            } else {
                                myInt = 4;
                            }
                        } else {
                            myInt = 2;
                        }
                    } else {
                        myInt = 3;
                    }
                    Log.d("LoginCheck2", String.valueOf(myInt));
                }
            });


    }
}
