package com.navi.javaexample;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting example program");

        if(args.length == 2){
            for(int i =1;  i<=Integer.parseInt(args[0]); i++){
                System.out.println("Factorial of " +i+  " is " + factorial(i));
                sleep(Long.parseLong(args[1]));
            }
        } else if(args.length == 1){
            int input = Integer.parseInt(args[0]);
            System.out.println("Factorial of " +input+  " is " + factorial(input));
        }
        System.out.println("Finished example program");
    }

    private static int factorial(int num){
        if(num == 1) return 1;
        else return num * factorial(num - 1);
    }

    private static void sleep(Long timeInMinutes) {
        try {
            System.out.println("Sleeping for " + timeInMinutes + " minutes");
            TimeUnit.MINUTES.sleep(timeInMinutes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
