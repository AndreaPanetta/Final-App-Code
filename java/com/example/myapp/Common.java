/*******************************************************************************************
 * Name:            Andrea Panetta
 * Page Name:       Common.java
 * Description:     Used to store some global variables
 *******************************************************************************************/

package com.example.myapp;

import com.example.myapp.Model.Level;
import com.example.myapp.Model.Question;
import com.example.myapp.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static String levelId;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();
}
