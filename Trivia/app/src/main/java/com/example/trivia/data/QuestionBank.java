package com.example.trivia.data;

import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question> questionArrayList=new ArrayList<>();
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    public List<Question> getQuestion(final AnswerListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        int len=response.length();
                        for(int i=0;i<len;i++){
                            try {
                                Question question=new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                                questionArrayList.add(question);
//                                Log.d("QuestionBank:","onResponse"+question);
//                               Log.d("JSON","response "+ response.getJSONArray(i).get(0));
//                                 Log.d("JSON","response "+ response.getJSONArray(i).get(1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                      if(callback!=null) callback.processFinished(questionArrayList);
                    }
                },
              new Response.ErrorListener(){
                  @Override
                  public void onErrorResponse(VolleyError error) {

                  }
              }

        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;

    }

}
